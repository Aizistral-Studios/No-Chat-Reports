package com.aizistral.nochatreports.common.mixins.common;

import java.lang.reflect.Type;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerDataExtension;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.util.GsonHelper;

/**
 * Handles "preventsChatReports" property during serialization and deserialization of {@link ServerStatus}.
 *
 * @author fxmorin (original implementation)
 * @author Aizistral (current version)
 */

@Mixin(ServerStatus.Serializer.class)
public class MixinServerStatusSerializer {

	@Inject(method = "serialize(Lnet/minecraft/network/protocol/status/ServerStatus;" +
			"Ljava/lang/reflect/Type;Lcom/google/gson/JsonSerializationContext;)Lcom/google/gson/JsonElement;",
			at = @At("RETURN"))
	private void onSerialize(ServerStatus serverStatus, Type type, JsonSerializationContext context,
			CallbackInfoReturnable<JsonElement> info) {
		if (!NCRConfig.getCommon().addQueryData())
			return;

		((JsonObject) info.getReturnValue()).addProperty("preventsChatReports", true);
	}

	@Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;" +
			"Lcom/google/gson/JsonDeserializationContext;)" +
			"Lnet/minecraft/network/protocol/status/ServerStatus;",
			locals = LocalCapture.CAPTURE_FAILSOFT, at = @At("RETURN"))
	private void onDeserialize(JsonElement element, Type type, JsonDeserializationContext context,
			CallbackInfoReturnable<ServerStatus> info, JsonObject json, ServerStatus status) {
		if (!json.has("preventsChatReports"))
			return;

		((ServerDataExtension) status).setPreventsChatReports(GsonHelper.getAsBoolean(json, "preventsChatReports"));
	}

}
