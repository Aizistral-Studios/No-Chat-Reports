package com.aizistral.nochatreports.common.modules.encryption;

import java.security.InvalidKeyException;

public class CaesarEncryptor extends Encryptor<CaesarAlgorithm> {
	private static final char PARAGRAPH = '\u00a7', PARAGRAPH_PLACEHOLDER = '\uffef',
			DELETE = '\u007f', DELETE_PLACEHOLDER = '\ufff0';
	private final int shift;

	protected CaesarEncryptor(int shift) throws InvalidKeyException {
		if (!Algorithm.CAESAR.validateKey(String.valueOf(shift)))
			throw new InvalidKeyException();
		this.shift = shift;
	}

	protected CaesarEncryptor(String key) throws InvalidKeyException {
		this.shift = fromString(key);
	}

	@Override
	public String encrypt(String message) {
		char[] chars = ensureUTF8(message).toCharArray();

		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) (chars[i] + this.shift);

			if (chars[i] == PARAGRAPH) {
				chars[i] = PARAGRAPH_PLACEHOLDER;
			} else if (chars[i] == DELETE) {
				chars[i] = DELETE_PLACEHOLDER;
			}
		}

		return new String(chars);
	}

	@Override
	public String decrypt(String message) {
		char[] chars = ensureUTF8(message).toCharArray();

		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == PARAGRAPH_PLACEHOLDER) {
				chars[i] = PARAGRAPH;
			} else if (chars[i] == DELETE_PLACEHOLDER) {
				chars[i] = DELETE;
			}

			chars[i] = (char) (chars[i] - this.shift);
		}

		return new String(chars);
	}

	@Override
	public CaesarAlgorithm getAlgorithm() {
		return Algorithm.CAESAR;
	}

	@Override
	public String getKey() {
		return String.valueOf(this.shift);
	}

	private static int fromString(String key) throws InvalidKeyException {
		if (!Algorithm.CAESAR.validateKey(key))
			throw new InvalidKeyException(key);
		return Integer.valueOf(key);
	}

}
