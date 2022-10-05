package com.aizistral.nochatreports.core;

import java.util.Optional;

import javax.annotation.Nullable;

import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.encryption.Encryptor;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;

public class EncryptionUtil {

	public static Optional<Component> tryDecrypt(Component component) {
		var optional = NCRConfig.getEncryption().getEncryptor();
		if (optional.isEmpty())
			return Optional.empty();

		Encryptor<?> encryption = optional.get();
		Component copy = recreate(component);
		ComponentContents contents = copy.getContents();
		boolean decryptedSomething = false;

		if (contents instanceof TranslatableContents translatable) {
			for (Object arg : translatable.args) {
				if (arg instanceof MutableComponent mutable
						&& mutable.getContents() instanceof LiteralContents literal) {
					var decrypted = tryDecrypt(literal.text(), encryption);
					if (decrypted.isPresent()) {
						mutable.contents = new LiteralContents(decrypted.get());
						decryptedSomething = true;
					}
				}
			}
		} else {
			decryptedSomething = tryDecrypt(copy, encryption);
		}

		return Optional.ofNullable(decryptedSomething ? copy : null);
	}

	public static boolean tryDecrypt(Component component, Encryptor<?> encryptor) {
		boolean decryptedSiblings = false;
		for (Component sibling : component.getSiblings()) {
			if (tryDecrypt(sibling, encryptor)) {
				decryptedSiblings = true;
			}
		}

		if (component.getContents() instanceof LiteralContents literal) {
			var decrypted = tryDecrypt(literal.text(), encryptor);

			if (decrypted.isPresent()) {
				((MutableComponent)component).contents = new LiteralContents(decrypted.get());
				return true;
			}
		}

		return decryptedSiblings;
	}

	public static Optional<String> tryDecrypt(String message, Encryptor<?> encryptor) {
		try {
			String[] splat = message.contains(" ") ? message.split(" ") : new String[] { message };
			String decryptable = splat[splat.length-1];

			String decrypted = encryptor.decrypt(decryptable);

			if (decrypted.startsWith("#%"))
				return Optional.of(message.substring(0, message.length() - decryptable.length()) + decrypted.substring(2, decrypted.length()));
			else
				return Optional.empty();
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	public static Component recreate(Component component) {
		return Component.Serializer.fromJson(Component.Serializer.toStableJson(component));
	}

}
