package com.aizistral.nochatreports.common.platform.events;

import java.util.Collection;
import java.util.function.Function;

import com.aizistral.nochatreports.common.platform.events.particular.Disconnect;
import com.aizistral.nochatreports.common.platform.events.particular.PlayReady;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientEvents {
	public static final PlatformEvent<PlayReady> PLAY_READY = PlatformEvent.create(callbacks ->
	(handler, client) -> callbacks.forEach(callback -> callback.handle(handler, client)));

	public static final PlatformEvent<Disconnect> DISCONNECT = PlatformEvent.create(callbacks ->
	client -> callbacks.forEach(callback -> callback.handle(client)));

	private ClientEvents() {
		throw new IllegalStateException("Can't touch this");
	}
}
