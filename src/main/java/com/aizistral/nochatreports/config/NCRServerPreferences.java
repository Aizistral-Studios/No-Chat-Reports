package com.aizistral.nochatreports.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.aizistral.nochatreports.NoChatReportsClient;
import com.aizistral.nochatreports.config.JSONConfig;
import com.aizistral.nochatreports.config.NCRServerPreferences;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.aizistral.nochatreports.core.SigningMode;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;

import net.minecraft.client.multiplayer.resolver.ServerAddress;

public class NCRServerPreferences extends JSONConfig {
	protected static final String FILE_NAME = "NoChatReports/NCR-ServerPreferences.json";
	protected HashMap<ServerAddress, SigningMode> signingModes = new HashMap<>();

	protected NCRServerPreferences() {
		super(FILE_NAME);
	}

	@Override
	public NCRServerPreferences getDefault() {
		return new NCRServerPreferences();
	}

	public SigningMode getModeRaw(@Nullable ServerAddress address) {
		return this.signingModes.get(address);
	}

	public SigningMode getModeUnresolved(@Nullable ServerAddress address) {
		if (!NoChatReportsClient.areSigningKeysPresent())
			return SigningMode.NEVER_FORCED;
		else if (ServerSafetyState.isInSingleplayer())
			return SigningMode.NEVER;
		else if (ServerSafetyState.isOnRealms())
			return SigningMode.ALWAYS;
		else if (address == null)
			return SigningMode.DEFAULT;

		SigningMode mode = this.getModeRaw(address);
		return mode != null ? mode : SigningMode.DEFAULT;
	}

	public SigningMode getMode(@Nullable ServerAddress address) {
		return this.getModeUnresolved(address).resolve();
	}

	public boolean hasMode(@Nullable ServerAddress address, SigningMode mode) {
		return this.getMode(address) == mode;
	}

	public boolean hasModeCurrent(SigningMode mode) {
		return this.hasMode(ServerSafetyState.getLastServer(), mode);
	}

	public void setMode(@Nullable ServerAddress address, @Nullable SigningMode mode) {
		if (address == null)
			return;

		if (mode == null || mode == SigningMode.DEFAULT) {
			this.signingModes.remove(address);
		} else {
			this.signingModes.put(address, mode);
		}
	}

}
