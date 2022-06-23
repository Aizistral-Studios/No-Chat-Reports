package com.aizistral.nochatreports;

import com.mojang.authlib.minecraft.UserApiService;

public class NoBanUserApiService extends UserApiServiceDelegate {
	public NoBanUserApiService(UserApiService d) {
		super(d);
	}

	@Override
	public UserProperties properties() {
		return OFFLINE_PROPERTIES;
	}
}
