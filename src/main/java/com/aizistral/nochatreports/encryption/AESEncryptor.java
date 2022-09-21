package com.aizistral.nochatreports.encryption;

import static com.aizistral.nochatreports.encryption.Encryption.AES_CFB8;
import static com.aizistral.nochatreports.encryption.Encryption.BASE64_DECODER;
import static com.aizistral.nochatreports.encryption.Encryption.BASE64_ENCODER;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Random;

import javax.annotation.Nullable;
import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.minecraft.util.Tuple;

public abstract class AESEncryptor<T extends AESEncryption> extends Encryptor<T> {
	private final T encryption;
	private final SecretKey key;
	private final Cipher encryptor, decryptor;
	private final boolean useIV;

	protected AESEncryptor(String key, T encryption) throws InvalidKeyException {
		this(new SecretKeySpec(decodeBinaryKey(key), "AES"), encryption);
	}

	protected AESEncryptor(SecretKey key, T encryption) throws InvalidKeyException {
		this.encryption = encryption;
		this.useIV = encryption.requiresIV();
		String mode = encryption.getMode();
		String padding = encryption.getPadding();

		try {
			this.key = key;

			Cipher encryptor = Cipher.getInstance(this.key.getAlgorithm() + "/" + mode + "/" + padding);
			if (this.useIV) {
				encryptor.init(ENCRYPT_MODE, this.key, this.generateIV().getA());
			} else {
				encryptor.init(ENCRYPT_MODE, this.key);

			}
			this.encryptor = encryptor;

			Cipher decryptor = Cipher.getInstance(this.key.getAlgorithm() + "/" + mode + "/" + padding);
			if (this.useIV) {
				decryptor.init(DECRYPT_MODE, this.key, this.generateIV().getA());
			} else {
				decryptor.init(DECRYPT_MODE, this.key);
			}
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
			if (this.useIV) {
				var tuple = this.generateIV();

				this.encryptor.init(ENCRYPT_MODE, this.key, tuple.getA());
				byte[] encrypted = this.encryptor.doFinal(message.getBytes());

				return BASE64_ENCODER.encodeToString(ByteBuffer.allocate(encrypted.length + tuple.getB().length).put(tuple.getB()).put(encrypted).array());
			} else
				return BASE64_ENCODER.encodeToString(this.encryptor.doFinal(message.getBytes()));
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String decrypt(String message) {
		try {
			if (this.useIV) {
				var tuple = this.splitIV(BASE64_DECODER.decode(message));

				this.decryptor.init(DECRYPT_MODE, this.key, tuple.getA());
				return new String(this.decryptor.doFinal(tuple.getB()), StandardCharsets.UTF_8);
			} else
				return new String(this.decryptor.doFinal(BASE64_DECODER.decode(message)), StandardCharsets.UTF_8);
		} catch (AEADBadTagException ex) {
			return "???";
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public String getKey() {
		return encodeBinaryKey(this.key.getEncoded());
	}

	@Override
	public T getAlgorithm() {
		return this.encryption;
	}

	protected abstract Tuple<AlgorithmParameterSpec, byte[]> generateIV() throws UnsupportedOperationException;

	protected abstract Tuple<AlgorithmParameterSpec, byte[]> splitIV(byte[] message) throws UnsupportedOperationException;

}
