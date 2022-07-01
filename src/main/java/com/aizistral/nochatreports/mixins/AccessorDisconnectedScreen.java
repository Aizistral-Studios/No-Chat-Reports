package com.aizistral.nochatreports.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.network.chat.Component;

@Mixin(DisconnectedScreen.class)
public interface AccessorDisconnectedScreen {

	@Accessor
	public Component getReason();

}
