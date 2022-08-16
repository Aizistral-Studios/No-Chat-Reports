package com.aizistral.nochatreports.core;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Component;

/**
 * Implementation of ModMenu and ClothConfig support for the mod.
 *
 * @author MODKILLER1001
 */

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {

        // Get the previous screen
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(Minecraft.getInstance().screen)
                .setTitle(Component.translatable("configuration.NoChatReports.config"));

        // Set category
        ConfigCategory visual = builder.getOrCreateCategory(Component.translatable("configuration.NoChatReports.category.visual"));
        ConfigCategory technical = builder.getOrCreateCategory(Component.translatable("configuration.NoChatReports.category.technical"));
        ConfigCategory whitelistedServers = builder.getOrCreateCategory(Component.translatable("configuration.NoChatReports.category.whitelistedServers"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Set an option for showServerSafety
        visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showServerSafety"), NoReportsConfig.getInstance().showServerSafety)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("option.NoChatReports.showServerSafety.tooltip"))
                .setSaveConsumer(newValue -> NoReportsConfig.getInstance().showServerSafety = newValue)
                .build());

        // Add link for documentation for showServerSafety
        visual.addEntry(entryBuilder.startTextDescription(Component.translatable("option.NoChatReports.showServerSafety.currentSafetyLevels")
                .withStyle(s -> s
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/Configuration-Files/#option-showserversafety"))).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/Configuration-Files/#option-showserversafety"))))
                .build());
  
        // Set an option for hideRedChatIndicators
        visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideRedChatIndicators"), NoReportsConfig.getInstance().hideRedChatIndicators)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("option.NoChatReports.hideRedChatIndicators.tooltip"))
                .setSaveConsumer(newValue -> NoReportsConfig.getInstance().hideRedChatIndicators = newValue)
                .build());

        // Set an option for hideYellowChatIndicators
        visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideYellowChatIndicators"), NoReportsConfig.getInstance().hideYellowChatIndicators)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("option.NoChatReports.hideYellowChatIndicators.tooltip"))
                .setSaveConsumer(newValue -> NoReportsConfig.getInstance().hideYellowChatIndicators = newValue)
                .build());

        // Set an option for hideGrayChatIndicators
        visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideGrayChatIndicators"), NoReportsConfig.getInstance().hideGrayChatIndicators)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("option.NoChatReports.hideGrayChatIndicators.tooltip"))
                .setSaveConsumer(newValue -> NoReportsConfig.getInstance().hideGrayChatIndicators = newValue)
                .build());

        // Set an option for hideWarningToast
        visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideWarningToast"), NoReportsConfig.getInstance().hideWarningToast)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("option.NoChatReports.hideWarningToast.tooltip"))
                .setSaveConsumer(newValue -> NoReportsConfig.getInstance().hideWarningToast = newValue)
                .build());

        // Set an option for alwaysHideReportButton
        visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.alwaysHideReportButton"), NoReportsConfig.getInstance().alwaysHideReportButton)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("option.NoChatReports.alwaysHideReportButton.tooltip"))
                .setSaveConsumer(newValue -> NoReportsConfig.getInstance().alwaysHideReportButton = newValue)
                .build());
        
        // Set an option for demandOnServer
        technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.demandOnServer"), NoReportsConfig.getInstance().demandOnServer)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("option.NoChatReports.demandOnServer.tooltip"))
                .setSaveConsumer(newValue -> NoReportsConfig.getInstance().demandOnServer = newValue)
                .build());

        // Set an option for enableDebugLog
        technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.enableDebugLog"), NoReportsConfig.getInstance().enableDebugLog)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("option.NoChatReports.enableDebugLog.tooltip"))
                .setSaveConsumer(newValue -> NoReportsConfig.getInstance().enableDebugLog = newValue)
                .build());
            
        // Set an option for convertToGameMessage
        technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.convertToGameMessage"), NoReportsConfig.getInstance().convertToGameMessage)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("option.NoChatReports.convertToGameMessage.tooltip"))
                .setSaveConsumer(newValue -> NoReportsConfig.getInstance().convertToGameMessage = newValue)
                .build());

        // Set an option for disableTelemetry
        technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.disableTelemetry"), NoReportsConfig.getInstance().disableTelemetry)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("option.NoChatReports.disableTelemetry.tooltip"))
                .setSaveConsumer(newValue -> NoReportsConfig.getInstance().disableTelemetry = newValue)
                .build());
            
        // Set an option for showReloadButton
        technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showReloadButton"), NoReportsConfig.getInstance().showReloadButton)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("option.NoChatReports.showReloadButton.tooltip"))
                .setSaveConsumer(newValue -> NoReportsConfig.getInstance().showReloadButton = newValue)
                .build());

        // Set an option for whitelistAllServers
        technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.whitelistAllServers"), NoReportsConfig.getInstance().whitelistAllServers)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("option.NoChatReports.whitelistAllServers.tooltip"))
                .setSaveConsumer(newValue -> NoReportsConfig.getInstance().whitelistAllServers = newValue)
                .build());

        // Instructions for adding servers
        whitelistedServers.addEntry(entryBuilder.startTextDescription(Component.translatable("option.NoChatReports.whitelistedServers.instructions"))
                .build());
        
        // Add or remove whitelisted servers
        whitelistedServers.addEntry(entryBuilder.startStrList(Component.translatable("option.NoChatReports.whitelistedServers"), NoReportsConfig.getInstance().whitelistedServers)
                .setExpanded(true)
                .setInsertInFront(true)
                .setTooltip(Component.translatable("option.NoChatReports.whitelistedServers.tooltip"))
                .setAddButtonTooltip(Component.translatable("option.NoChatReports.whitelistedServers.addButtonTooltip"))
                .setRemoveButtonTooltip(Component.translatable("option.NoChatReports.whitelistedServers.removeButtonTooltip"))
                .setSaveConsumer(newValue -> NoReportsConfig.getInstance().whitelistedServers = newValue)
                .build());

        // Save config
        builder.setSavingRunnable(() -> {
            NoReportsConfig.saveConfig();
            NoReportsConfig.loadConfig(); 
        });
        return builder.build();
        };
    }
}