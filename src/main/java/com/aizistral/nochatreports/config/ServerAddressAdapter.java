package com.aizistral.nochatreports.config;

import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

@Environment(EnvType.CLIENT)
public class ServerAddressAdapter extends TypeAdapter<ServerAddress> {
	public static final ServerAddressAdapter INSTANCE = new ServerAddressAdapter();

	private ServerAddressAdapter() {
		// NO-OP
	}

	@Override
	public ServerAddress read(JsonReader reader) throws IOException {
		String string = reader.nextString();
		String address[] = string.split(":");

		if (address.length != 2)
			throw new IOException("Incorrect server address format: " + string);

		return new ServerAddress(address[0], Integer.valueOf(address[1]));
	}

	@Override
	public void write(JsonWriter writer, ServerAddress address) throws IOException {
		writer.value(address.getHost() + ":" + address.getPort());
	}

}
