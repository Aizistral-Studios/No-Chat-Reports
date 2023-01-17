package com.aizistral.nochatreports.common.modules.encryption;

import java.security.InvalidKeyException;

public class AESCFB8Algorithm extends AESAlgorithm {

	protected AESCFB8Algorithm() {
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