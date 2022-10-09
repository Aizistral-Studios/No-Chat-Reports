package com.aizistral.nochatreports.encryption;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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

	protected static byte[] toBytes(String string) {
		return string.getBytes(StandardCharsets.UTF_8);
	}

	protected static String fromBytes(byte[] bytes) {
		return new String(bytes, StandardCharsets.UTF_8);
	}

	protected static String ensureUTF8(String string) {
		return fromBytes(toBytes(string));
	}

	protected static String encodeBase64(String string) {
		return encodeBase64(toBytes(string));
	}

	protected static String encodeBase64(byte[] bytes) {
		return fromBytes(Encryption.BASE64_ENCODER.encode(bytes)).replace('/', '\\');
	}

	protected static byte[] encodeBase64Bytes(String string) {
		return toBytes(encodeBase64(toBytes(string)));
	}

	protected static String decodeBase64(String string) {
		return fromBytes(decodeBase64Bytes(string));
	}

	protected static byte[] decodeBase64Bytes(String string) {
		return Encryption.BASE64_DECODER.decode(toBytes(string.replace('\\', '/')));
	}

	protected static String encodeBinaryKey(byte[] key) {
		return fromBytes(Encryption.BASE64_ENCODER.encode(key));
	}

	protected static byte[] decodeBinaryKey(String key) throws InvalidKeyException {
		try {
			return Encryption.BASE64_DECODER.decode(toBytes(key));
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
