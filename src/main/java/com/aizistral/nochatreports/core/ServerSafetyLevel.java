package com.aizistral.nochatreports.core;

import com.aizistral.nochatreports.NoChatReports;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Represents the level of "safety" of the given server, in the eyes of the client.
 * Current level is updated dynamically and can be obtained using {@link ServerSafetyState#getCurrent()}.
 * @author Aizistral
 */

public enum ServerSafetyLevel {

	/**
	 * For servers that have No Chat Reports or something of similar purpose installed, and make
	 * verifiable effort to remove cryptographic signature from every player message they relay.
	 */
	SECURE,


	/**
	 * For servers that do not make any effort to remove signatures from chat messages themselves,
	 * but do not prohibit players from sending unsigned messages. To protect the player falls fully
	 * on the client of respective player in such case, and vanilla client will not do that.
	 */
	UNINTRUSIVE,

	/**
	 * For servers that have <code>enforce-secure-profile</code> enabled, or enforce signed messages
	 * through some other means. Users of this mod can choose to play on those servers regardless
	 * after being informed of danger, but there is very little mod will be able to do to protect
	 * them in such case.
	 */

	INSECURE,

	/**
	 * Transient status for when evaluation of safety level is not yet complete for current server.
	 */

	UNKNOWN;

	@OnlyIn(Dist.CLIENT)
	public Component getTooltip() {
		return Component.translatable("gui.nochatreports.status_" + this.name().toLowerCase());
	}
}
