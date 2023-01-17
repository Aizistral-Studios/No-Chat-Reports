package com.aizistral.nochatreports.common.modules.encryption;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public abstract class Algorithm {
	private static final List<Algorithm> REGISTERED = new ArrayList<>();
	protected static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
	protected static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

	public static final AESCFB8Algorithm AES_CFB8 = new AESCFB8Algorithm();
	public static final AESGCMAlgorithm AES_GCM = new AESGCMAlgorithm();
	public static final AESECBAlgorithm AES_ECB = new AESECBAlgorithm();
	public static final CaesarAlgorithm CAESAR = new CaesarAlgorithm();

	private final String id, name;

	protected Algorithm(String id, String name) {
		this.id = id;
		this.name = name;

		if (REGISTERED.stream().filter(e -> e.getID().equals(id) || e.getName().equals(name))
				.findAny().isPresent())
			throw new IllegalStateException("Duplicate encryption algorithm registered! ID: " + this.getID() +
					", Name: " + this.getName());

		REGISTERED.add(this);
	}

	public String getID() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public abstract String getRandomKey();

	public abstract String getDefaultKey();

	public abstract boolean supportsPassphrases();

	public abstract String getPassphraseKey(String passphrase) throws UnsupportedOperationException;

	public abstract boolean validateKey(String key);

	public abstract Encryptor<?> getProcessor(String key) throws InvalidKeyException;

	public abstract Encryptor<?> getRandomProcessor();

	public static List<Algorithm> getRegistered() {
		return Collections.unmodifiableList(REGISTERED);
	}

}
