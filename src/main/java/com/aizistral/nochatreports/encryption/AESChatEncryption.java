package com.aizistral.nochatreports.encryption;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.minecraft.server.network.ServerLoginPacketListenerImpl;

public class AESChatEncryption extends ChatEncryption {
	private static final Base64.Encoder ENCODER = Base64.getEncoder();
	private static final Base64.Decoder DECODER = Base64.getDecoder();
	private final SecretKey key;
	private final Cipher encryptor, decryptor;

	public AESChatEncryption(String key) throws InvalidKeyException {
		this(new SecretKeySpec(key.getBytes(), "AES"));
	}

	public AESChatEncryption(SecretKey key) throws InvalidKeyException {
		try {
			this.key = key;

			Cipher encryptor = Cipher.getInstance(this.key.getAlgorithm() + "/CFB8/NoPadding");
			encryptor.init(Cipher.ENCRYPT_MODE, this.key, new IvParameterSpec(key.getEncoded()));
			this.encryptor = encryptor;

			Cipher decryptor = Cipher.getInstance(this.key.getAlgorithm() + "/CFB8/NoPadding");
			decryptor.init(Cipher.DECRYPT_MODE, this.key, new IvParameterSpec(key.getEncoded()));
			this.decryptor = decryptor;
		} catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String encrypt(String message) {
		try {
			return ENCODER.encodeToString(this.encryptor.doFinal(message.getBytes()));
		} catch (IllegalBlockSizeException | BadPaddingException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String decrypt(String message) {
		try {
			return new String(this.decryptor.doFinal(DECODER.decode(message)), StandardCharsets.UTF_8);
		} catch (IllegalBlockSizeException | BadPaddingException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String getAlgorithm() {
		return this.key.getAlgorithm() + "Base64";
	}

	@Override
	public String getKey() {
		return new String(this.key.getEncoded(), StandardCharsets.UTF_8);
	}

	public static AESChatEncryption withRandomKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128);
			SecretKey key = keyGenerator.generateKey();

			return new AESChatEncryption(key);
		} catch (NoSuchAlgorithmException | InvalidKeyException ex) {
			throw new RuntimeException(ex);
		}
	}

}
