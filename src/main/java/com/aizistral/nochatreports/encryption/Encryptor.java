package com.aizistral.nochatreports.encryption;

import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public abstract class Encryptor<T extends Encryption> {
	protected static final SecureRandom RANDOM = new SecureRandom();

	protected Encryptor() {
		// NO-OP
	}

	public abstract String encrypt(String message);

	public abstract String decrypt(String message);

	public abstract T getAlgorithm();

	public abstract String getKey();

	protected static String encodeBinaryKey(byte[] key) {
		return Encryption.BASE64_ENCODER.encodeToString(key);
	}

	protected static byte[] decodeBinaryKey(String key) throws InvalidKeyException {
		try {
			return Encryption.BASE64_DECODER.decode(key);
		} catch (Exception ex) {
			throw new InvalidKeyException(ex);
		}
	}

	protected static byte[] mergeBytes(byte[] array1, byte[] array2) {
		byte[] result = Arrays.copyOf(array1, array1.length + array2.length);
		System.arraycopy(array2, 0, result, array1.length, array2.length);
		return result;
	}

}
