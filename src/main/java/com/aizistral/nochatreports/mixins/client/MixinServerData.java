package com.aizistral.nochatreports.mixins.client;

import com.aizistral.nochatreports.core.NoChatReportingOption;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerData.class)
public class MixinServerData implements NoChatReportingOption {

    private boolean hasNoChatReporting;

    @Override
    public boolean hasNoChatReporting() {
        return this.hasNoChatReporting;
    }

    @Override
    public void setNoChatReporting(boolean state) {
        this.hasNoChatReporting = state;
    }

    @Inject(method = "write", at = @At("RETURN"))
    private void onWrite(CallbackInfoReturnable<CompoundTag> cir) {
        if (this.hasNoChatReporting) cir.getReturnValue().putBoolean("hasNoChatReporting", true);
    }

    @Inject(method = "read", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At("RETURN"))
    private static void onRead(CompoundTag compound, CallbackInfoReturnable<ServerData> cir, ServerData serverData) {
        ((NoChatReportingOption)serverData).setNoChatReporting(compound.contains("hasNoChatReporting"));
    }
}
