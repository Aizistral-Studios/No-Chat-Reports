package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.EncryptionUtil;
import java.util.Optional;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatHud.class)
public class MixinChatComponent {
	private static final MessageIndicator.Icon ENCRYPTED_ICON = MessageIndicator.Icon.valueOf("CHAT_NCR_ENCRYPTED");
	private boolean lastMessageEncrypted;
	private Text lastMessageOriginal;

	@ModifyVariable(method = "addRecentChat", at = @At("HEAD"), argsOnly = true)
	private String onAddRecentChat(String message) {
		if (NCRConfig.getEncryption().isEnabledAndValid())
			return NCRConfig.getEncryption().getLastMessage();
		else
			return message;
	}

	@ModifyVariable(method = "addMessage(Lnet/minecraft/network/chat/Component;"
			+ "Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ComponentRenderUtils;"
					+ "wrapComponents(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/client/gui/Font;"
					+ ")Ljava/util/List;", ordinal = 0, shift = Shift.AFTER), argsOnly = true)
	private synchronized MessageIndicator modifyGUITag(MessageIndicator tag) {
		if (!NCRConfig.getEncryption().showEncryptionIndicators() || !this.lastMessageEncrypted)
			return tag;

		this.lastMessageEncrypted = false;
		Text tooltip = Text.empty().append(Text.translatable("tag.nochatreports.encrypted",
				Text.literal(NCRConfig.getEncryption().getAlgorithm().getName())
				.formatted(Formatting.BOLD))).append(ScreenTexts.LINE_BREAK).append(
						Text.translatable("tag.nochatreports.encrypted_original",
								this.lastMessageOriginal));

		return new MessageIndicator(0x8B3EC7, ENCRYPTED_ICON, tooltip, "Encrypted");
	}

	@ModifyArg(index = 0, method = "addMessage(Lnet/minecraft/network/chat/Component;"
			+ "Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ComponentRenderUtils;"
					+ "wrapComponents(Lnet/minecraft/network/chat/FormattedText;I"
					+ "Lnet/minecraft/client/gui/Font;)Ljava/util/List;", ordinal = 0))
	private StringVisitable modifyGUIMessage(StringVisitable msg) {
		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info("Adding chat message, structure: " +
					Text.Serializer.toSortedJsonString((Text) msg));
		}

		var decrypted = EncryptionUtil.tryDecrypt((Text) msg);

		decrypted.ifPresentOrElse(component -> {
			this.lastMessageOriginal = EncryptionUtil.recreate((Text) msg);
			this.lastMessageEncrypted = true;
		}, () -> this.lastMessageEncrypted = false);

		return this.lastMessageEncrypted ? decrypted.get() : msg;
	}

}
