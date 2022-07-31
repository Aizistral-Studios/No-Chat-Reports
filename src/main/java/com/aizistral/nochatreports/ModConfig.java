package com.aizistral.nochatreports;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

@Config(name = "NoChatReports")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip()
    public boolean demandOnServer = true;
    @ConfigEntry.Gui.Tooltip()
    public boolean enableDebugLog = false;
    @ConfigEntry.Gui.Tooltip()
    public boolean showServerSafety = true;
    @ConfigEntry.Gui.Tooltip()
    public boolean hideRedChatIndicators = true;
    @ConfigEntry.Gui.Tooltip()
    public boolean hideYellowChatIndicators = true;
    @ConfigEntry.Gui.Tooltip()
    public boolean hideGrayChatIndicators = true;
    @ConfigEntry.Gui.Tooltip()
    public boolean hideWarningToast = true;
    @ConfigEntry.Gui.Tooltip()
    public boolean alwaysHideReportButton = false;
    @ConfigEntry.Gui.Tooltip()
    public boolean versionEasterEgg = true;
    @ConfigEntry.Gui.Tooltip()
    public boolean disableTelemetry = true;
    @ConfigEntry.Gui.Tooltip()
    public boolean showReloadButton = true;
}