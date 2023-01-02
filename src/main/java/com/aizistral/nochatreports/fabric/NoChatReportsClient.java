package com.aizistral.nochatreports.fabric;

import com.aizistral.nochatreports.common.platform.events.ClientEvents;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

@Environment(EnvType.CLIENT)
public class NoChatReportsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->
		ClientEvents.PLAY_READY.invoker().handle(handler, client));

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
		ClientEvents.DISCONNECT.invoker().handle(client));
	}

}
