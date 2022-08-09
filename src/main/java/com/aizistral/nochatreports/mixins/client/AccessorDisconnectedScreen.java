package com.aizistral.nochatreports.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.aizistral.nochatreports.NoChatReportsClient;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;

import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.network.chat.Component;

@Mixin(DisconnectedScreen.class)
public interface AccessorDisconnectedScreen {

	/**
	 * @return Disconnect reason of this screen. This is used in {@link NoChatReportsClient} to detect
	 * when player have been kicked out of server because client refused to send account's public key,
	 * and subsequently swap the screen for {@link UnsafeServerScreen} where player can choose to
	 * reconnect and expose their key.
	 */

	@Accessor
	public Component getReason();

}
