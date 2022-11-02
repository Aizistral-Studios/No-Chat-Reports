package com.aizistral.nochatreports.config;

import java.util.ArrayList;
import java.util.List;

import com.aizistral.nochatreports.gui.UnsafeServerScreen;

import net.minecraft.client.multiplayer.resolver.ServerAddress;

public class NCRServerWhitelist extends JSONConfig {
	protected static final String FILE_NAME = "NoChatReports/NCR-ServerWhitelist.json";
	protected static final List<String> DEFAULT_WHITELISTED_SERVERS = new ArrayList<>();
	protected List<String> whitelistedServers = DEFAULT_WHITELISTED_SERVERS;

	protected NCRServerWhitelist() {
		super(FILE_NAME);
	}

	@Override
	public NCRServerWhitelist getDefault() {
		return new NCRServerWhitelist();
	}

	/**
	 * @return Current list of whitelisted unsafe servers. Used on client to remember which servers
	 * user disabled unsafety warnings for.<br><br>
	 *
	 * This is empty by default.
	 *
	 * @see UnsafeServerScreen
	 */

	public List<String> getList() {
		return this.whitelistedServers;
	}

	/**
	 * @param address Address of the server to be check.
	 * @return Whether this server is in the whitelist of unsafe servers, as returned by
	 * {@link #getList()}.
	 * @see UnsafeServerScreen
	 */

	public boolean isWhitelisted(ServerAddress address) {
		return NCRConfig.getClient().whitelistAllServers() || (address != null &&
				this.getList().contains(address.getHost() + ":" + address.getPort()));
	}

	public boolean add(ServerAddress address) {
		return this.getList().add(address.getHost() + ":" + address.getPort());
	}

	public boolean remove(ServerAddress address) {
		return this.getList().remove(address.getHost() + ":" + address.getPort());
	}

}
