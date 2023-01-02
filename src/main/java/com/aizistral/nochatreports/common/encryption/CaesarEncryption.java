package com.aizistral.nochatreports.common.encryption;

import java.security.InvalidKeyException;
import java.security.SecureRandom;

public class CaesarEncryption extends Encryption {

	protected CaesarEncryption() {
		super("caesar", "Caesar");
	}

	@Override
	public String getRandomKey() {
		return String.valueOf(1 + new SecureRandom().nextInt(1024));
	}

	@Override
	public String getDefaultKey() {
		return "3";
	}

	@Override
	public boolean supportsPassphrases() {
		return false;
	}

	@Override
	public String getPassphraseKey(String passphrase) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean validateKey(String key) {
		try {
			int value = Integer.valueOf(key);
			return value > 0 && value < 1025;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	@Override
	public CaesarEncryptor getProcessor(String key) throws InvalidKeyException {
		return new CaesarEncryptor(key);
	}

	@Override
	public CaesarEncryptor getRandomProcessor() {
		try {
			return this.getProcessor(this.getRandomKey());
		} catch (InvalidKeyException ex) {
			throw new RuntimeException(ex);
		}
	}

}
