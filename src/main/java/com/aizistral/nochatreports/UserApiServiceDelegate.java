package com.aizistral.nochatreports;

import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.response.KeyPairResponse;

import java.util.UUID;
import java.util.concurrent.Executor;

import org.jetbrains.annotations.Nullable;

public class UserApiServiceDelegate implements UserApiService {
	protected final UserApiService d;

	public UserApiServiceDelegate(UserApiService d) {
		this.d = d;
	}

	@Override
	public UserProperties properties() {
		return this.d.properties();
	}

	@Override
	public boolean isBlockedPlayer(UUID playerID) {
		return this.d.isBlockedPlayer(playerID);
	}

	@Override
	public void refreshBlockList() {
		this.d.refreshBlockList();
	}

	@Override
	public TelemetrySession newTelemetrySession(Executor executor) {
		return this.d.newTelemetrySession(executor);
	}

	@Nullable
	@Override
	public KeyPairResponse getKeyPair() {
		return this.d.getKeyPair();
	}
}
