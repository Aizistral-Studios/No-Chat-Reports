package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.brigadier.ParseResults;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.commands.SharedSuggestionProvider;

@Mixin(ClientPacketListener.class)
public interface AccessorClientPacketListener {

	@Invoker("parseCommand")
	public ParseResults<SharedSuggestionProvider> invokeParseCommand(String string);

}
