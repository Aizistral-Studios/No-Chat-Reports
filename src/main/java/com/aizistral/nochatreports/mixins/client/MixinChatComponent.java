package com.aizistral.nochatreports.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At.Shift;

import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.config.NCRConfigClient;
import com.aizistral.nochatreports.encryption.Encryptor;

import net.minecraft.ChatFormatting;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;

@Mixin(ChatComponent.class)
public class MixinChatComponent {
	private static final GuiMessageTag.Icon ENCRYPTED_ICON = GuiMessageTag.Icon.valueOf("CHAT_NCR_ENCRYPTED");
	private boolean lastMessageEncrypted;
	private Component lastMessageOriginal;

	@ModifyVariable(method = "addRecentChat", at = @At("HEAD"), argsOnly = true)
	private String onAddRecentChat(String message) {
		if (!message.startsWith("/"))
			return NCRConfig.getEncryption().getEncryptor().map(e -> e.decrypt(message)).orElse(message);
		else
			return message;
	}

	@ModifyVariable(method = "addMessage(Lnet/minecraft/network/chat/Component;"
			+ "Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ComponentRenderUtils;"
					+ "wrapComponents(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/client/gui/Font;"
					+ ")Ljava/util/List;", ordinal = 0, shift = Shift.AFTER), argsOnly = true)
	private GuiMessageTag modifyGUITag(GuiMessageTag tag) {
		if (this.lastMessageEncrypted) {
			this.lastMessageEncrypted = false;
			Component tooltip = Component.empty().append(Component.translatable("tag.nochatreports.encrypted",
					Component.literal(NCRConfig.getEncryption().getAlgorithm().getName())
					.withStyle(ChatFormatting.BOLD))).append(CommonComponents.NEW_LINE).append(
							Component.translatable("tag.nochatreports.encrypted_original",
									this.lastMessageOriginal));
			return new GuiMessageTag(0x8B3EC7, ENCRYPTED_ICON, tooltip, "Encrypted");
		} else
			return tag;
	}

	@ModifyArg(index = 0, method = "addMessage(Lnet/minecraft/network/chat/Component;"
			+ "Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ComponentRenderUtils;"
					+ "wrapComponents(Lnet/minecraft/network/chat/FormattedText;I"
					+ "Lnet/minecraft/client/gui/Font;)Ljava/util/List;", ordinal = 0))
	private FormattedText modifyGUIMessage(FormattedText msg) {
		var optional = NCRConfig.getEncryption().getEncryptor();

		if (optional.isEmpty())
			return msg;
		Encryptor<?> encryption = optional.get();
		this.lastMessageOriginal = Component.Serializer.fromJson(Component.Serializer.toStableJson(((Component) msg)));
		ComponentContents contents = ((Component)msg).getContents();

		if (contents instanceof TranslatableContents translatable) {
			for (Object arg : translatable.args) {
				if (arg instanceof MutableComponent mutable
						&& mutable.getContents() instanceof LiteralContents literal) {
					try {
						String decrypted = encryption.decrypt(literal.text());
						if (decrypted.startsWith("#%")) {
							mutable.contents = new LiteralContents(decrypted.substring(2, decrypted.length()));
							this.lastMessageEncrypted = true;
						}
					} catch (Exception ex) {
						continue;
					}
				}
			}
		}

		return msg;
	}

}
