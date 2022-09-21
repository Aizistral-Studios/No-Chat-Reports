package com.aizistral.nochatreports.encryption;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import net.minecraft.util.Tuple;

public class AESECBEncryptor extends AESEncryptor<AESECBEncryption> {

	protected AESECBEncryptor(String key) throws InvalidKeyException {
		super(key, Encryption.AES_ECB);
	}

	protected AESECBEncryptor(SecretKey key, String mode, String padding, boolean iv) throws InvalidKeyException {
		super(key, Encryption.AES_ECB);
	}

	@Override
	protected Tuple<IvParameterSpec, byte[]> generateIV() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Tuple<IvParameterSpec, byte[]> splitIV(byte[] message) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

}
