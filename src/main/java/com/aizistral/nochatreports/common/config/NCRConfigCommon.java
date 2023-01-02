package com.aizistral.nochatreports.common.config;

public final class NCRConfigCommon extends JSONConfig {
	protected static final String FILE_NAME = "NoChatReports/NCR-Common.json";

	protected String demandOnClientMessage = "You do not have No Chat Reports, and this server is"
			+ " configured to require it on client!";
	protected boolean demandOnClient = false, convertToGameMessage = true,
			addQueryData = true, enableDebugLog = false;

	protected NCRConfigCommon() {
		super(FILE_NAME);
	}

	@Override
	public NCRConfigCommon getDefault() {
		return new NCRConfigCommon();
	}

	/**
	 * @return True if server with No Chat Reports installed should demand that mod is present on
	 * every client that tries to join. Mod's installation on client is optional otherwise.<br><br>
	 *
	 * This is false by default.
	 */

	public boolean demandOnClient() {
		return this.demandOnClient;
	}

	/**
	 * @return Disconnect message that should be shown to client if <code>demandOnClient</code> is
	 * true and it tries to join without No Chat Reports installed.
	 */

	public String demandOnClientMessage() {
		return this.demandOnClientMessage;
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
