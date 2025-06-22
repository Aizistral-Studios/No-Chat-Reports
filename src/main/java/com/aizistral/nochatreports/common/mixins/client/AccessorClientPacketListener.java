package com.aizistral.nochatreports.common.mixins.client;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.world.entity.player.ProfileKeyPair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientPacketListener.class)
public interface AccessorClientPacketListener {

	@Accessor("commands")
	public CommandDispatcher<ClientSuggestionProvider> getCommands();

	@Accessor("suggestionsProvider")
	public ClientSuggestionProvider getClientSuggestionProvider();

	@Invoker("setKeyPair")
	public void invokeSetKeyPair(ProfileKeyPair profileKeyPair);
}
