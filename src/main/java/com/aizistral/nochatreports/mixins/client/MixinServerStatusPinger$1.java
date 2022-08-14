package com.aizistral.nochatreports.mixins.client;

import com.aizistral.nochatreports.core.NoChatReportingOption;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/client/multiplayer/ServerStatusPinger$1")
public class MixinServerStatusPinger$1 { //Anonymous class - Transfer data from ServerStatus to ServerData

    @Final
    @Shadow
    private ServerData val$data;


    @Inject(
            method = "handleStatusResponse(Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/status/ServerStatus;getPlayers()Lnet/minecraft/network/protocol/status/ServerStatus$Players;",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            )
    )
    private void getNoChatReports(ClientboundStatusResponsePacket clientboundStatusResponsePacket, CallbackInfo ci) {
        boolean hasChatReporting = ((NoChatReportingOption)clientboundStatusResponsePacket.getStatus()).hasNoChatReporting();
        ((NoChatReportingOption)val$data).setNoChatReporting(hasChatReporting);
    }
}
