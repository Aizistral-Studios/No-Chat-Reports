package com.aizistral.nochatreports.mixins.client;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At.Shift;

import com.aizistral.nochatreports.NoChatReports;
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
		if (this.lastMessageEncrypted) {
			this.lastMessageEncrypted = false;
			Component tooltip = Component.empty().append(Component.translatable("tag.nochatreports.encrypted",
					Component.literal(NCRConfig.getEncryption().getAlgorithm().getName())
					.withStyle(ChatFormatting.BOLD))).append(CommonComponents.NEW_LINE).append(
							Component.translatable("tag.nochatreports.encrypted_original",
									this.lastMessageOriginal));

			//System.out.println("Last message original: " + last);
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
		if (NCRConfig.getCommon().enableDebugLog()) {
			NoChatReports.LOGGER.info("Adding chat message, structure: " +
					Component.Serializer.toStableJson((Component) msg));
		}

		var optional = NCRConfig.getEncryption().getEncryptor();
		if (optional.isEmpty())
			return msg;

		Encryptor<?> encryption = optional.get();
		Component copy = this.recreate((Component) msg);
		this.lastMessageOriginal = this.recreate(copy);
		ComponentContents contents = copy.getContents();

		if (contents instanceof TranslatableContents translatable) {
			for (Object arg : translatable.args) {
				if (arg instanceof MutableComponent mutable
						&& mutable.getContents() instanceof LiteralContents literal) {
					String decrypted = this.tryDecrypt(literal.text(), encryption);
					if (decrypted != null) {
						mutable.contents = new LiteralContents(decrypted);
						this.lastMessageEncrypted = true;
					}
				}
			}
		} else {
			this.lastMessageEncrypted = this.tryDecrypt(copy, encryption);
		}

		return copy;
	}

	private boolean tryDecrypt(Component component, Encryptor<?> encryptor) {
		boolean decryptedSiblings = false;
		for (Component sibling : component.getSiblings()) {
			if (this.tryDecrypt(sibling, encryptor)) {
				decryptedSiblings = true;
			}
		}

		if (component.getContents() instanceof LiteralContents literal) {
			String decrypted = this.tryDecrypt(literal.text(), encryptor);

			if (decrypted != null) {
				((MutableComponent)component).contents = new LiteralContents(decrypted);
				return true;
			}
		}

		return decryptedSiblings;
	}

	@Nullable
	private String tryDecrypt(String message, Encryptor<?> encryptor) {
		try {
			String[] splat = message.contains(" ") ? message.split(" ") : new String[] { message };
			String decryptable = splat[splat.length-1];

			String decrypted = encryptor.decrypt(decryptable);

			if (decrypted.startsWith("#%"))
				return message.substring(0, message.length() - decryptable.length()) + decrypted.substring(2, decrypted.length());
			else
				return null;
		} catch (Exception ex) {
			return null;
		}
	}

	private Component recreate(Component component) {
		return Component.Serializer.fromJson(Component.Serializer.toStableJson(component));
	}

}
