package com.aizistral.nochatreports.config;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.aizistral.nochatreports.encryption.AESCFB8Encryptor;
import com.aizistral.nochatreports.encryption.Encryption;
import com.aizistral.nochatreports.encryption.Encryptor;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;

import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.util.StringUtil;

public class NCRConfigEncryption extends JSONConfig {
	protected static final String FILE_NAME = "NoChatReports/NCR-Encryption.json";
	protected boolean skipWarning = false, enableEncryption = false;
	protected String encryptionKey = Encryption.AES_CFB8.getDefaultKey(), encryptionPassphrase = "",
			algorithmName = Encryption.AES_CFB8.getName();
	private transient Encryption algorithm;
	private transient boolean isValid = false;

	protected NCRConfigEncryption() {
		super(FILE_NAME);
	}

	@Override
	public NCRConfigEncryption getDefault() {
		return new NCRConfigEncryption();
	}

	@Override
	protected void uponLoad() {
		this.algorithm = Encryption.getRegistered().stream().filter(e -> e.getName().equals(this.algorithmName))
				.findFirst().orElseThrow(() -> new RuntimeException("Invalid encryption algorithm specified in " + FILE_NAME + ": " + this.algorithmName));
		this.validate();
	}

	private void validate() {
		this.isValid = this.algorithm.validateKey(this.encryptionKey);
	}

	public void toggleEncryption() {
		this.enableEncryption = !this.enableEncryption;
	}

	public void setAlgorithm(Encryption encryption) {
		this.algorithm = encryption;
		this.validate();
		this.saveFile();
	}

	public void setEncryptionKey(String key) {
		this.encryptionKey = key;
		this.validate();
		this.saveFile();
	}

	public void setEncryptionPassphrase(String pass) {
		this.encryptionPassphrase = pass;
		this.saveFile();
	}

	public void disableWarning() {
		this.skipWarning = true;
	}

	public boolean isWarningDisabled() {
		return this.skipWarning;
	}

	public boolean isEnabled() {
		return this.enableEncryption;
	}

	public String getEncryptionKey() {
		return this.encryptionKey;
	}

	public String getEncryptionPassphrase() {
		return this.encryptionPassphrase;
	}

	public boolean isValid() {
		return this.isValid;
	}

	public boolean isEnabledAndValid() {
		return this.isEnabled() && this.isValid();
	}

	public Encryption getAlgorithm() {
		return this.algorithm;
	}

	/**
	 * @return Chat encryptor, if encryption is enabled and user specified valid key in config.
	 */

	public Optional<Encryptor<?>> getEncryptor() {
		if (!this.isEnabledAndValid())
			return Optional.empty();

		try {
			return Optional.of(this.algorithm.getProcessor(this.encryptionKey));
		} catch (InvalidKeyException ex) {
			throw new RuntimeException(ex); // shouldn't happen due to prior validation
		}
	}

}
