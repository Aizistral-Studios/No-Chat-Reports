package com.aizistral.nochatreports.encryption;

public abstract class ChatEncryption {

	protected ChatEncryption() {
		// NO-OP
	}

	public abstract String encrypt(String message);

	public abstract String decrypt(String message);

	public abstract String getAlgorithm();

	public abstract String getKey();

}
