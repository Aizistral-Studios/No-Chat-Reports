package com.aizistral.nochatreports.mixins.both;

import com.aizistral.nochatreports.core.NoReportsConfig;
import com.aizistral.nochatreports.core.NoChatReportingOption;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.util.GsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.reflect.Type;

@Mixin(ServerStatus.Serializer.class)
public class MixinSerializer {

	@Inject(method = "serialize(Lnet/minecraft/network/protocol/status/ServerStatus;" +
			"Ljava/lang/reflect/Type;Lcom/google/gson/JsonSerializationContext;)Lcom/google/gson/JsonElement;",
			at = @At("RETURN"))
	private void onSerialize(ServerStatus serverStatus, Type type, JsonSerializationContext context,
			CallbackInfoReturnable<JsonElement> info) {
		if (!NoReportsConfig.includeQueryData()) return;
		((JsonObject)info.getReturnValue()).addProperty("noChatReporting", true);
	}

	@Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;" +
			"Lcom/google/gson/JsonDeserializationContext;)" +
			"Lnet/minecraft/network/protocol/status/ServerStatus;",
			locals = LocalCapture.CAPTURE_FAILSOFT, at = @At("RETURN"))
	private void onDeserialize(JsonElement jsonElement, Type arg1, JsonDeserializationContext context,
			CallbackInfoReturnable<ServerStatus> info, JsonObject jsonObj, ServerStatus status) {
		if (!jsonObj.has("noChatReporting")) return;
		((NoChatReportingOption)status).setNoChatReporting(GsonHelper.getAsBoolean(jsonObj, "noChatReporting"));
	}

}
