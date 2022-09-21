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
import net.minecraft.util.Tuple;

import static com.aizistral.nochatreports.encryption.Encryption.*;
import static javax.crypto.Cipher.*;

public class AESCFB8Encryptor extends AESEncryptor<AESCFB8Encryption> {

	protected AESCFB8Encryptor(String key) throws InvalidKeyException {
		super(key, Encryption.AES_CFB8);
	}

	protected AESCFB8Encryptor(SecretKey key, String mode, String padding, boolean iv) throws InvalidKeyException {
		super(key, Encryption.AES_CFB8);
	}

	@Override
	protected Tuple<IvParameterSpec, byte[]> generateIV() throws UnsupportedOperationException {
		long nonce = RANDOM.nextLong();
		byte[] iv = new byte[16];
		new Random(nonce).nextBytes(iv);
		return new Tuple<>(new IvParameterSpec(iv), ByteBuffer.allocate(8).putLong(nonce).array());
	}

	@Override
	protected Tuple<IvParameterSpec, byte[]> splitIV(byte[] message) throws UnsupportedOperationException {
		ByteBuffer buffer = ByteBuffer.wrap(message);
		int size = buffer.capacity();
		long nonce = buffer.getLong();
		byte[] encrypted = new byte[size - 8];
		buffer.get(encrypted);
		byte[] iv = new byte[16];
		new Random(nonce).nextBytes(iv);
		return new Tuple<>(new IvParameterSpec(iv), encrypted);
	}

}
