package com.aizistral.nochatreports.common.encryption;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import net.minecraft.util.Tuple;

public class AESGCMEncryptor extends AESEncryptor<AESGCMEncryption> {

	protected AESGCMEncryptor(String key) throws InvalidKeyException {
		super(key, Encryption.AES_GCM);
	}

	protected AESGCMEncryptor(SecretKey key) throws InvalidKeyException {
		super(key, Encryption.AES_GCM);
	}

	@Override
	protected Tuple<AlgorithmParameterSpec, byte[]> generateIV() throws UnsupportedOperationException {
		byte[] iv = new byte[12];
		RANDOM.nextBytes(iv);
		return new Tuple<>(new GCMParameterSpec(96, iv), iv);
	}

	@Override
	protected Tuple<AlgorithmParameterSpec, byte[]> splitIV(byte[] message) throws UnsupportedOperationException {
		byte[] iv = new byte[12];
		byte[] msg = new byte[message.length - 12];
		ByteBuffer.wrap(message).get(iv).get(msg);
		return new Tuple<>(new GCMParameterSpec(96, iv), msg);
	}

}
