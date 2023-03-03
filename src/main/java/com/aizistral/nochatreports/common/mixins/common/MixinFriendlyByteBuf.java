package com.aizistral.nochatreports.common.mixins.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerDataExtension;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.ServerMetadata;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Util;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Mixin(PacketByteBuf.class)
public abstract class MixinFriendlyByteBuf {
	@Shadow @Final
	private static Gson GSON;

	@Inject(method = "readJsonWithCodec", at = @At("HEAD"), cancellable = true)
	private void onReadJsonWithCodec(Codec codec, CallbackInfoReturnable info) throws Exception {
		if (codec == ServerMetadata.CODEC) {
			info.cancel();

			JsonElement jsonElement = JsonHelper.deserialize(GSON, this.readUtf(), JsonElement.class);
			DataResult dataResult = codec.parse(JsonOps.INSTANCE, jsonElement);
			Object result = Util.getResult(dataResult, string -> new DecoderException("Failed to decode json: " + string));

			if (jsonElement.getAsJsonObject().has("preventsChatReports")) {
				((ServerDataExtension) result).setPreventsChatReports(jsonElement.getAsJsonObject()
						.get("preventsChatReports").getAsBoolean());
			}

			info.setReturnValue(result);
		}
	}

	@Inject(method = "writeJsonWithCodec", at = @At("HEAD"), cancellable = true)
	private void onWriteJsonWithCodec(Codec codec, Object object, CallbackInfo info) throws Exception {
		if (!NCRConfig.getCommon().addQueryData())
			return;

		if (codec == ServerMetadata.CODEC) {
			info.cancel();

			DataResult<JsonElement> dataResult = codec.encodeStart(JsonOps.INSTANCE, object);
			JsonElement element = Util.getResult(dataResult, string -> new EncoderException("Failed to encode: " + string + " " + object));

			element.getAsJsonObject().addProperty("preventsChatReports", true);

			this.writeUtf(GSON.toJson(element));
		}
	}

	@Shadow
	public abstract String readUtf();

	@Shadow
	public abstract PacketByteBuf writeUtf(String string);
}
