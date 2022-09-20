package com.aizistral.nochatreports.encryption;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public abstract class Encryption {
	private static final List<Encryption> REGISTERED = new ArrayList<>();
	protected static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
	protected static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

	public static final AESEncryption AES = new AESEncryption();

	protected Encryption() {
		if (REGISTERED.stream().filter(e -> e.getID().equals(this.getID()) || e.getName().equals(this.getName()))
				.findAny().isPresent())
			throw new IllegalStateException("Duplicate encryption algorithm registered! ID: " + this.getID() +
					", Name: " + this.getName());
		REGISTERED.add(this);
	}

	public abstract String getRandomKey();

	public abstract String getDefaultKey();

	public abstract boolean supportsPassphrases();

	public abstract String getPassphraseKey(String passphrase) throws UnsupportedOperationException;

	public abstract boolean validateKey(String key);

	public abstract String getName();

	public abstract String getID();

	public abstract Encryptor<?> getProcessor(String key) throws InvalidKeyException;

	public abstract Encryptor<?> getRandomProcessor();

	public static List<Encryption> getRegistered() {
		return Collections.unmodifiableList(REGISTERED);
	}

}
