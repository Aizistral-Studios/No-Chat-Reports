package com.aizistral.nochatreports.core;

import net.minecraft.network.protocol.status.ServerStatus;

public class NoReportServerStatus extends ServerStatus {

    private boolean chatReporting;

    public final boolean hasChatReporting() {
        return this.chatReporting;
    }

    public final void setChatReporting(boolean state) {
        this.chatReporting = state;
    }
}
