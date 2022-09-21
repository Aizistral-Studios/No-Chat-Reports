package com.aizistral.nochatreports.encryption;

import java.security.InvalidKeyException;

public class CaesarEncryptor extends Encryptor<CaesarEncryption> {
	private final int shift;

	protected CaesarEncryptor(int shift) throws InvalidKeyException {
		if (!Encryption.CAESAR.validateKey(String.valueOf(shift)))
			throw new InvalidKeyException();
		this.shift = shift;
	}

	protected CaesarEncryptor(String key) throws InvalidKeyException {
		this.shift = fromString(key);
	}

	@Override
	public String encrypt(String message) {
		char[] chars = message.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) (chars[i] + this.shift);
		}

		return new String(chars);
	}

	@Override
	public String decrypt(String message) {
		char[] chars = message.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) (chars[i] - this.shift);
		}

		return new String(chars);
	}

	@Override
	public CaesarEncryption getAlgorithm() {
		return Encryption.CAESAR;
	}

	@Override
	public String getKey() {
		return String.valueOf(this.shift);
	}

	private static int fromString(String key) throws InvalidKeyException {
		if (!Encryption.CAESAR.validateKey(key))
			throw new InvalidKeyException(key);
		return Integer.valueOf(key);
	}

}
