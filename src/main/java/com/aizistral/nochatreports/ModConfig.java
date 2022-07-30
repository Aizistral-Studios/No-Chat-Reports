package com.aizistral.nochatreports;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "NoChatReports")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean demandOnClient = true;
    @ConfigEntry.Gui.Tooltip
    public boolean demandOnServer = false;
    @ConfigEntry.Gui.Tooltip
    public boolean enableDebugLog = false;
    @ConfigEntry.Gui.Tooltip
    public boolean convertToGameMessage = false;
    @ConfigEntry.Gui.Tooltip
    public boolean showServerSafety = true;
    @ConfigEntry.Gui.Tooltip
    public boolean suppressVanillaSecurityNotices = true;
    @ConfigEntry.Gui.Tooltip
    public boolean alwaysHideReportButton = false;
    @ConfigEntry.Gui.Tooltip
    public boolean versionEasterEgg = true;
    @ConfigEntry.Gui.Tooltip
    public boolean disableTelemetry = true;
    @ConfigEntry.Gui.Tooltip
    public boolean showReloadButton = true;
}