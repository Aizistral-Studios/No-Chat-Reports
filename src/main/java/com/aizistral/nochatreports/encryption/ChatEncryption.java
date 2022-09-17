package com.aizistral.nochatreports.encryption;

import java.util.Base64;
import java.util.List;

public abstract class ChatEncryption {
	protected static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
	protected static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

	protected ChatEncryption() {
		// NO-OP
	}

	public abstract String encrypt(String message);

	public abstract String decrypt(String message);

	public abstract String getAlgorithm();

	public abstract String getKey();

}
