package com.aizistral.nochatreports.config;

import java.util.ArrayList;
import java.util.List;

import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;
import com.aizistral.nochatreports.mixins.client.MixinChatListener;
import com.aizistral.nochatreports.mixins.client.MixinToastComponent;

import net.minecraft.client.multiplayer.resolver.ServerAddress;

public final class NCRClientConfig extends JSONConfig {
	protected static final String FILE_NAME = "NoChatReports/NCR-Client.json";
	protected static final List<String> DEFAULT_WHITELISTED_SERVERS = new ArrayList<>();

	protected boolean demandOnServer = false, showServerSafety = true, hideRedChatIndicators = true,
			hideYellowChatIndicators = true, hideGrayChatIndicators = true, hideWarningToast = true,
			alwaysHideReportButton = false, disableTelemetry = true, showReloadButton = true,
			whitelistAllServers = false, verifiedIconEnabled = true;
	protected int verifiedIconOffsetX = 0, verifiedIconOffsetY = 0;
	protected List<String> whitelistedServers = DEFAULT_WHITELISTED_SERVERS;

	protected NCRClientConfig() {
		super(FILE_NAME);
	}

	/**
	 * @return True if client with No Chat Reports installed should demand that mod is present on
	 * every server that it tries to join. In that case client will not be able to join servers that
	 * don't have the mod installed. Servers without the mod can still provide varying degrees of
	 * safety, which {@link ServerSafetyLevel} and respective icon in bottom-right corner of the chat
	 * screen is designed to indicate.<br><br>
	 *
	 * This is false by default.
	 */

	public boolean demandOnServer() {
		return this.demandOnServer;
	}

	/**
	 * @return True if current {@link ServerSafetyLevel} should be indicated on client by an icon in
	 * the bottom-right corner of chat screen.<br><br>
	 *
	 * This is true by default.
	 */

	public boolean showServerSafety() {
		return this.showServerSafety;
	}

	/**
	 * @return Current list of whitelisted unsafe servers. Used on client to remember which servers
	 * user disabled unsafety warnings for.<br><br>
	 *
	 * This is empty by default.
	 *
	 * @see UnsafeServerScreen
	 */

	public List<String> getWhitelistedServers() {
		return this.whitelistedServers;
	}

	/**
	 * @param address Address of the server to be check.
	 * @return Whether this server is in the whitelist of unsafe servers, as returned by
	 * {@link #getWhitelistedServers()}.
	 * @see UnsafeServerScreen
	 */

	public boolean isWhitelistedServer(ServerAddress address) {
		return this.getWhitelistedServers().contains(address.getHost() + ":" + address.getPort());
	}

	/**
	 * @return True if vanilla's unsigned messages trust indicator should be removed.<br><br>
	 *
	 * This is true by default.
	 *
	 * @see MixinChatListener#onEvaluateTrustLevel
	 */

	public boolean hideRedChatIndicators() {
		return this.hideRedChatIndicators;
	}

	/**
	 * @return True if vanilla's modified/hidden messages trust indicator should be removed.<br><br>
	 *
	 * This is true by default.
	 *
	 * @see MixinChatListener#onEvaluateTrustLevel
	 */

	public boolean hideYellowChatIndicators() {
		return this.hideYellowChatIndicators;
	}

	/**
	 * @return True if vanilla's system messages trust indicator should be removed.<br><br>
	 *
	 * This is true by default.
	 *
	 * @see com.aizistral.nochatreports.mixins.client.MixinGuiMessageTag#onSystem
	 */

	public boolean hideGrayChatIndicators() {
		return this.hideGrayChatIndicators;
	}

	/**
	 * @return True if "Chat messages can't be verified" toast should be removed.<br><br>
	 *
	 * This is true by default.
	 *
	 * @see MixinToastComponent#onAddToast
	 */

	public boolean hideWarningToast() {
		return this.hideWarningToast;
	}

	/**
	 * @return True if reporting buttons should always be hidden from social interactions screen,
	 * instead of simply being disabled.<br><br>
	 *
	 * This is false by default.
	 */

	public boolean alwaysHideReportButton() {
		return this.alwaysHideReportButton;
	}

	/**
	 * @return True if sending telemetry to Mojang should be disabled.<br><br>
	 *
	 * This is true by default.
	 */

	public boolean disableTelemetry() {
		return this.disableTelemetry;
	}

	/**
	 * @return True if the configuration reload button should be displayed in multiplayer screen.
	 * <br><br>
	 *
	 * This is true by default.
	 */

	public boolean showReloadButton() {
		return this.showReloadButton;
	}

	/**
	 * @return True if the full-screen warning should be skipped for servers that require chat signing,
	 * letting users simply join the server. Because of that, servers will no longer be added to the
	 * whitelist.<br><br>
	 *
	 * This is false by default.
	 */

	public boolean whitelistAllServers() {
		return this.whitelistAllServers;
	}

	/**
	 * @return True if "Safe Server" icon should be displayed in multiplayer menu for servers that
	 * declare themselves as preventing chat reports.<br><br>
	 *
	 * This is true by default.
	 */

	public boolean verifiedIconEnabled() {
		return this.verifiedIconEnabled;
	}


	/**
	 * @return X offset for "Safe Server" icon in multiplayer menu.
	 */

	public int getVerifiedIconOffsetX() {
		return 16 + this.verifiedIconOffsetX;
	}

	/**
	 * @return Y offset for "Safe Server" icon in multiplayer menu.
	 */

	public int getVerifiedIconOffsetY() {
		return 12 + this.verifiedIconOffsetY;
	}

}
