package com.aizistral.nochatreports.common.mixins.client;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPacketListener.class)
public interface AccessorClientPacketListener {

	@Accessor("commands")
	public CommandDispatcher<ClientSuggestionProvider> getCommands();

	@Accessor("suggestionsProvider")
	public ClientSuggestionProvider getClientSuggestionProvider();
}
