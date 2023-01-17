package com.aizistral.nochatreports.common.modules.encryption;

import java.security.InvalidKeyException;

public class AESGCMAlgorithm extends AESAlgorithm {

	protected AESGCMAlgorithm() {
		super("GCM", "NoPadding", true);
	}

	@Override
	public AESGCMEncryptor getProcessor(String key) throws InvalidKeyException {
		return new AESGCMEncryptor(key);
	}

	@Override
	public AESGCMEncryptor getRandomProcessor() {
		try {
			return this.getProcessor(this.getRandomKey());
		} catch (InvalidKeyException ex) {
			throw new RuntimeException(ex);
		}
	}

}
