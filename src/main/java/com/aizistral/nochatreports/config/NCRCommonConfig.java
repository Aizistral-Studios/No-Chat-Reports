package com.aizistral.nochatreports.config;

import java.nio.file.Path;
import java.util.List;

import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;
import com.aizistral.nochatreports.mixins.client.MixinChatListener;
import com.aizistral.nochatreports.mixins.client.MixinToastComponent;

import net.minecraft.client.multiplayer.resolver.ServerAddress;

public final class NCRCommonConfig extends AbstractJSONConfig {
	protected static final String FILE_NAME = "NoChatReports/NCR-Common.json";
	protected boolean demandOnClient = true, enableDebugLog = false, convertToGameMessage = false,
			addQueryData = true;

	protected NCRCommonConfig() {
		super(FILE_NAME);
	}

	/**
	 * @return True if server with No Chat Reports installed should demand that mod is present on
	 * every client that tries to join. Mod's installation on client is optional otherwise.<br><br>
	 *
	 * This is false by default.
	 */

	public boolean demandsOnClient() {
		return this.demandOnClient;
	}

	/**
	 * @return True if server should convert all player messages to system messages. System messages
	 * do not feature signatures and look completely identical in the chat, with exception of gray line
	 * next to them added in pre-release 3. They are also unselectable for chat reporting.<br><br>
	 *
	 * This is false by default.
	 */

	public boolean convertToGameMessage() {
		return this.convertToGameMessage;
	}

	/**
	 * @return True if mod should log some additional information about its activity and message
	 * signatures. Generally only useful for debugging.<br><br>
	 *
	 * This is false by default.
	 */

	public boolean enableDebugLog() {
		return this.enableDebugLog;
	}

	/**
	 * @return True if server should include extra query data to help clients know that your server
	 * is secure.<br><br>
	 *
	 * This is true by default.
	 */

	public boolean addQueryData() {
		return this.addQueryData;
	}

}
