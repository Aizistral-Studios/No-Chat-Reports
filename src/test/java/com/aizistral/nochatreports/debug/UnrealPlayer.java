package com.aizistral.nochatreports.debug;

import java.util.UUID;

/**
 * Exists solely to help with testing some of the features of the mod.
 * You can use this to trick client into thinking there is another player
 * on server, and send chat messages as if they are from that player.
 * <br><br>
 * DON'T FORGET TO TURN "convertToGameMessage" OFF!
 *
 * @author Aizistral
 */

public class UnrealPlayer {
	public static final UnrealPlayer DEFAULT = new UnrealPlayer(UUID.fromString("bfa45411-874a-4ee0-b3bd-00c716059d95"), "Aizistral");
	private final UUID id;
	private final String name;

	public UnrealPlayer(UUID id, String name) {
		this.id = id;
		this.name = name;
	}

	//	public void join(MinecraftServer server, ServerGamePacketListenerImpl connection) {
	//		ClientboundPlayerInfoPacket packet = new ClientboundPlayerInfoPacket(Action.ADD_PLAYER,
	//				this.getPlayer(server));
	//		connection.send(packet);
	//	}
	//
	//	public void leave(MinecraftServer server, ServerGamePacketListenerImpl connection) {
	//		ClientboundPlayerInfoPacket packet = new ClientboundPlayerInfoPacket(Action.REMOVE_PLAYER,
	//				this.getPlayer(server));
	//		connection.send(packet);
	//	}
	//
	//	public ServerPlayer getPlayer(MinecraftServer server) {
	//		return new ServerPlayer(server, server.overworld(), new GameProfile(this.id, this.name), null);
	//	}
	//
	//	public void sendSystem(MinecraftServer server, ServerGamePacketListenerImpl connection, Component message) {
	//		ServerPlayer player = this.getPlayer(server);
	//		ClientboundSystemChatPacket packet = new ClientboundSystemChatPacket(message, false);
	//		connection.send(packet);
	//	}
	//
	//	public void sendMessage(MinecraftServer server, ServerGamePacketListenerImpl connection, String message, @Nullable String unsigned) {
	//		ServerPlayer player = this.getPlayer(server);
	//		ClientboundPlayerChatPacket packet = new ClientboundPlayerChatPacket(new PlayerChatMessage(
	//				new SignedMessageHeader(MessageSignature.EMPTY, this.id), MessageSignature.EMPTY,
	//				new SignedMessageBody(new ChatMessageContent(message, Component.literal(message)), Instant.now(), 0,
	//						LastSeenMessages.EMPTY), unsigned != null ? Optional.of(Component.literal(unsigned))
	//								: Optional.empty(), FilterMask.PASS_THROUGH),
	//				ChatType.bind(ChatType.CHAT, player).toNetwork(server.registryAccess()));
	//
	//
	//		//				new ClientboundPlayerChatPacket(Component.literal(message),
	//		//				unsigned != null ? Optional.of(Component.literal(unsigned)) : Optional.empty(), 0,
	//		//						new ChatSender(this.id, Component.literal(this.name)), Instant.now(),
	//		//						Crypt.SaltSignaturePair.EMPTY);
	//		connection.send(packet);
	//	}

}
