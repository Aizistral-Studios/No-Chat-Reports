package com.aizistral.nochatreports.encryption;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import net.minecraft.util.StringUtil;

public class AESCFB8Encryption extends AESEncryption {

	protected AESCFB8Encryption() {
		super("CFB8", "NoPadding", true);
	}

	@Override
	public AESCFB8Encryptor getProcessor(String key) throws InvalidKeyException {
		return new AESCFB8Encryptor(key);
	}

	@Override
	public AESCFB8Encryptor getRandomProcessor() {
		try {
			return this.getProcessor(this.getRandomKey());
		} catch (InvalidKeyException ex) {
			throw new RuntimeException(ex);
		}
	}

}