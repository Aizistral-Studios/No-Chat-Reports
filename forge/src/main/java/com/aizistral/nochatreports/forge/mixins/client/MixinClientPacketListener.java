package com.aizistral.nochatreports.forge.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.platform.events.ClientEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;

@Mixin(value = ClientPacketListener.class)
public class MixinClientPacketListener {

	@Inject(method = "onDisconnect", at = @At("HEAD"))
	private void handleDisconnection(Component reason, CallbackInfo info) {
		ClientEvents.DISCONNECT.invoker().handle(Minecraft.getInstance());
	}

}
