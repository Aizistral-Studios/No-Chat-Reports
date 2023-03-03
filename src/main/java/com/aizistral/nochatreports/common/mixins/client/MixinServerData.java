package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.aizistral.nochatreports.common.core.ServerDataExtension;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.nbt.NbtCompound;

/**
 * Adds "preventsChatReports" property to {@link ServerInfo} and handles it during serialization to
 * and deserialization from NBT-tag.
 *
 * @author fxmorin (original implementation)
 * @author Aizistral (current version)
 */

@Mixin(ServerInfo.class)
public class MixinServerData implements ServerDataExtension {
	private boolean preventsChatReports;

	@Override
	public boolean preventsChatReports() {
		return this.preventsChatReports;
	}

	@Override
	public void setPreventsChatReports(boolean prevents) {
		this.preventsChatReports = prevents;
	}

	@Inject(method = "write", at = @At("RETURN"))
	private void onWrite(CallbackInfoReturnable<NbtCompound> info) {
		info.getReturnValue().putBoolean("preventsChatReports", this.preventsChatReports);
	}

	@Inject(method = "read", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At("RETURN"))
	private static void onRead(NbtCompound tag, CallbackInfoReturnable<ServerInfo> info, ServerInfo data) {
		((ServerDataExtension)data).setPreventsChatReports(tag.getBoolean("preventsChatReports"));
	}

}
