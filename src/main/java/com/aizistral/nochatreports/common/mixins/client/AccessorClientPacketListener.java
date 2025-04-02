package com.aizistral.nochatreports.common.mixins.client;

import com.mojang.brigadier.ParseResults;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.entity.player.ProfileKeyPair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientPacketListener.class)
public interface AccessorClientPacketListener {

	@Invoker("parseCommand")
	public ParseResults<SharedSuggestionProvider> invokeParseCommand(String string);

	@Invoker("setKeyPair")
	public void invokeSetKeyPair(ProfileKeyPair profileKeyPair);
}
