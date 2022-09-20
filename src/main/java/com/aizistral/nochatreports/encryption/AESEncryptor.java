package com.aizistral.nochatreports.encryption;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import static com.aizistral.nochatreports.encryption.Encryption.*;
import static javax.crypto.Cipher.*;

public class AESEncryptor extends Encryptor<AESEncryption> {
	private final SecretKey key;
	private final Cipher encryptor, decryptor;

	protected AESEncryptor(String key) throws InvalidKeyException {
		this(new SecretKeySpec(decodeBinaryKey(key), "AES"));
	}

	protected AESEncryptor(SecretKey key) throws InvalidKeyException {
		try {
			this.key = key;

			Cipher encryptor = Cipher.getInstance(this.key.getAlgorithm() + "/CFB8/NoPadding");
			encryptor.init(ENCRYPT_MODE, this.key, new IvParameterSpec(key.getEncoded()));
			this.encryptor = encryptor;

			Cipher decryptor = Cipher.getInstance(this.key.getAlgorithm() + "/CFB8/NoPadding");
			decryptor.init(DECRYPT_MODE, this.key, new IvParameterSpec(key.getEncoded()));
			this.decryptor = decryptor;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
			throw new RuntimeException(ex);
		} catch (InvalidAlgorithmParameterException ex) {
			throw new InvalidKeyException(ex);
		}
	}

	@Override
	public String encrypt(String message) {
		try {
			long nonce = RANDOM.nextLong();
			byte[] iv = new byte[16];
			new Random(nonce).nextBytes(iv);

			this.encryptor.init(ENCRYPT_MODE, this.key, new IvParameterSpec(iv));
			byte[] encrypted = this.encryptor.doFinal(message.getBytes());

			return BASE64_ENCODER.encodeToString(ByteBuffer.allocate(encrypted.length + 8).putLong(nonce).put(encrypted).array());
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String decrypt(String message) {
		try {
			ByteBuffer buffer = ByteBuffer.wrap(BASE64_DECODER.decode(message));
			int size = buffer.capacity();
			long nonce = buffer.getLong();
			byte[] encrypted = new byte[size - 8];
			buffer.get(encrypted);
			byte[] iv = new byte[16];
			new Random(nonce).nextBytes(iv);

			this.decryptor.init(DECRYPT_MODE, this.key, new IvParameterSpec(iv));
			return new String(this.decryptor.doFinal(encrypted), StandardCharsets.UTF_8);
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public AESEncryption getAlgorithm() {
		return AES;
	}

	@Override
	public String getKey() {
		return encodeBinaryKey(this.key.getEncoded());
	}

}
