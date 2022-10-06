package com.aizistral.nochatreports.config;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.encryption.AESCFB8Encryptor;
import com.aizistral.nochatreports.encryption.Encryptor;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;
import com.aizistral.nochatreports.mixins.client.MixinChatListener;
import com.aizistral.nochatreports.mixins.client.MixinToastComponent;

import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.util.StringUtil;

public final class NCRConfigClient extends JSONConfig {
	protected static final String FILE_NAME = "NoChatReports/NCR-Client.json";

	protected boolean demandOnServer = false, showServerSafety = true, hideRedChatIndicators = true,
			hideYellowChatIndicators = true, hideGrayChatIndicators = true, hideWarningToast = true,
			alwaysHideReportButton = false, disableTelemetry = true, showReloadButton = true,
			whitelistAllServers = false, verifiedIconEnabled = true, showNCRButton = true,
			enableMod = true, skipRealmsWarning = false;
	protected int verifiedIconOffsetX = 0, verifiedIconOffsetY = 0, reconnectAwaitSeconds = 4,
			postDisconnectAwaitSeconds = 10;

	protected NCRConfigClient() {
		super(FILE_NAME);
	}

	@Override
	public NCRConfigClient getDefault() {
		return new NCRConfigClient();
	}

	public void toggleMod() {
		this.enableMod = !this.enableMod;
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

	/**
	 * @return True if button to disable/enable entire mod should be displayed in
	 * multiplayer menu.
	 */

	public boolean showNCRButton() {
		return this.showNCRButton;
	}

	/**
	 * @return Whether or not No Chat Reports should provide networking-related functionality.
	 */

	public boolean enableMod() {
		return this.enableMod;
	}

	/**
	 * @return How many seconds No Chat Reports should wait before attemting automatic reconnects.
	 * @see <a href="https://github.com/Aizistral-Studios/No-Chat-Reports/issues/190">Issue #190</a>
	 */

	public int getReconnectAwaitSeconds() {
		return this.reconnectAwaitSeconds;
	}

	/**
	 * @return Same as <code>reconnectAwaitSeconds</code>, but countdown starts when user disconnects
	 * from server. If this delay is greater than <code>reconnectAwaitSeconds</code> then it will apply.
	 */

	public int getPostDisconnectAwaitSeconds() {
		return this.postDisconnectAwaitSeconds;
	}

	/**
	 * @return Whether warning on entering Realms screen should not be displayed.
	 */

	public boolean skipRealmsWarning() {
		return this.skipRealmsWarning;
	}

	public void setSkipRealmsWarning(boolean skipRealmsWarning) {
		this.skipRealmsWarning = skipRealmsWarning;
	}

}
