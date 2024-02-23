package com.aizistral.nochatreports.neoforge.mixins.client;

import com.aizistral.nochatreports.common.platform.events.ClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientCommonPacketListenerImpl.class)
public class MixinClientPacketListener {

	@Inject(method = "onDisconnect", at = @At("HEAD"))
	private void handleDisconnection(Component reason, CallbackInfo info) {
		Object self = this;
		
		if (self instanceof ClientPacketListener) {
			ClientEvents.DISCONNECT.invoker().handle(Minecraft.getInstance());
		}
	}

}
