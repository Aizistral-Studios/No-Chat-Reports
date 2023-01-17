package com.aizistral.nochatreports.common.modules.encryption;

import java.security.InvalidKeyException;

public class AESECBAlgorithm extends AESAlgorithm {

	protected AESECBAlgorithm() {
		super("ECB", "PKCS5Padding", false);
	}

	@Override
	public AESECBEncryptor getProcessor(String key) throws InvalidKeyException {
		return new AESECBEncryptor(key);
	}

	@Override
	public AESECBEncryptor getRandomProcessor() {
		try {
			return this.getProcessor(this.getRandomKey());
		} catch (InvalidKeyException ex) {
			throw new RuntimeException(ex);
		}
	}

}
