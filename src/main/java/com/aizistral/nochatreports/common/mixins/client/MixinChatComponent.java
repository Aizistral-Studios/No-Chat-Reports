package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.EncryptionUtil;

import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

@Mixin(ChatComponent.class)
public class MixinChatComponent {
	private static final GuiMessageTag.Icon ENCRYPTED_ICON = GuiMessageTag.Icon.valueOf("CHAT_NCR_ENCRYPTED");
	private boolean lastMessageEncrypted;
	private Component lastMessageOriginal;

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
	private synchronized GuiMessageTag modifyGUITag(GuiMessageTag tag) {
		if (!NCRConfig.getEncryption().showEncryptionIndicators() || !this.lastMessageEncrypted)
			return tag;

		this.lastMessageEncrypted = false;
		Component tooltip = Component.empty().append(Component.translatable("tag.nochatreports.encrypted",
				Component.literal(NCRConfig.getEncryption().getAlgorithm().getName())
				.withStyle(ChatFormatting.BOLD))).append(CommonComponents.NEW_LINE).append(
						Component.translatable("tag.nochatreports.encrypted_original",
								this.lastMessageOriginal));

		return new GuiMessageTag(0x8B3EC7, ENCRYPTED_ICON, tooltip, "Encrypted");
	}

	@ModifyArg(index = 0, method = "addMessage(Lnet/minecraft/network/chat/Component;"
			+ "Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ComponentRenderUtils;"
					+ "wrapComponents(Lnet/minecraft/network/chat/FormattedText;I"
					+ "Lnet/minecraft/client/gui/Font;)Ljava/util/List;", ordinal = 0))
	private FormattedText modifyGUIMessage(FormattedText msg) {
		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info("Adding chat message, structure: " +
					Component.Serializer.toStableJson((Component) msg));
		}

		var decrypted = EncryptionUtil.tryDecrypt((Component) msg);

		decrypted.ifPresentOrElse(component -> {
			this.lastMessageOriginal = EncryptionUtil.recreate((Component) msg);
			this.lastMessageEncrypted = true;
		}, () -> this.lastMessageEncrypted = false);

		return this.lastMessageEncrypted ? decrypted.get() : msg;
	}

}
