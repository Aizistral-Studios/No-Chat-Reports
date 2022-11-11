package com.aizistral.nochatreports.encryption;

import net.minecraft.util.Tuple;

import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;

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
