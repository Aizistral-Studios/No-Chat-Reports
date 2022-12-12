package com.aizistral.nochatreports.config;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class ServerAddressAdapter extends TypeAdapter<ServerAddress> {
	public static final ServerAddressAdapter INSTANCE = new ServerAddressAdapter();

	private ServerAddressAdapter() {
		// NO-OP
	}

	@Override
	public ServerAddress read(JsonReader reader) throws IOException {
		String string = reader.nextString();

		if (!ServerAddress.isValidAddress(string))
			throw new IOException("Incorrect server address format: " + string);

		return ServerAddress.parseString(string);
	}

	@Override
	public void write(JsonWriter writer, ServerAddress address) throws IOException {
		writer.value(address.toString());
	}

}
