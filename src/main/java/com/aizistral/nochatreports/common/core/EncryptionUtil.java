package com.aizistral.nochatreports.common.core;

import java.util.Optional;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.encryption.Encryptor;

public class EncryptionUtil {

	public static Optional<Text> tryDecrypt(Text component) {
		var optional = NCRConfig.getEncryption().getEncryptor();
		if (optional.isEmpty())
			return Optional.empty();

		Encryptor<?> encryption = optional.get();
		Text copy = recreate(component);
		TextContent contents = copy.getContent();

		return Optional.ofNullable(tryDecrypt(copy, encryption) ? copy : null);
	}

	public static boolean tryDecrypt(Text component, Encryptor<?> encryptor) {
		boolean decryptedSiblings = false;
		for (Text sibling : component.getSiblings()) {
			if (tryDecrypt(sibling, encryptor)) {
				decryptedSiblings = true;
			}
		}

		if (component.getContent() instanceof LiteralTextContent literal) {
			var decrypted = tryDecrypt(literal.string(), encryptor);

			if (decrypted.isPresent()) {
				((MutableText)component).content = new LiteralTextContent(decrypted.get());
				return true;
			}
		} else if (component.getContent() instanceof TranslatableTextContent translatable) {
			for (Object arg : translatable.args) {
				if (arg instanceof MutableText mutable) {
					if (tryDecrypt(mutable, encryptor)) {
						decryptedSiblings = true;
					}
				}
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

	public static Text recreate(Text component) {
		return Text.Serializer.fromJson(Text.Serializer.toSortedJsonString(component));
	}

}
