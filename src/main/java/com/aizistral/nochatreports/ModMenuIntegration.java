package com.aizistral.nochatreports;

import com.aizistral.nochatreports.core.NoReportsConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.Properties;
import java.util.Optional;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {

            // Get the previous screen
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(Minecraft.getInstance().screen)
                    .setTitle(Component.translatable("configuration.NoChatReports.config"));

            // Set category
            ConfigCategory general = builder.getOrCreateCategory(Component.translatable("configuration.NoChatReports.category.general"));

            // Set an option for demandOnServer
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.demandOnServer"), NoReportsConfig.getInstance().demandOnServer)
                    .setDefaultValue(false)
                    .setTooltip(Component.translatable("option.NoChatReports.demandOnServer.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.getInstance().demandOnServer = newValue)
                    .build());

            // Set an option for enableDebugLog
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.enableDebugLog"), NoReportsConfig.getInstance().enableDebugLog)
                    .setDefaultValue(false)
                    .setTooltip(Component.translatable("option.NoChatReports.enableDebugLog.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.getInstance().enableDebugLog = newValue)
                    .build());
                
            // Set an option for convertToGameMessage
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.convertToGameMessage"), NoReportsConfig.getInstance().convertToGameMessage)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.convertToGameMessage.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.getInstance().convertToGameMessage = newValue)
                    .build());        

            // Set an option for showServerSafety
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showServerSafety"), NoReportsConfig.getInstance().showServerSafety)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.showServerSafety.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.getInstance().showServerSafety = newValue)
                    .build());

            // Set an option for hideRedChatIndicators
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideRedChatIndicators"), NoReportsConfig.getInstance().hideRedChatIndicators)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.hideRedChatIndicators.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.getInstance().hideRedChatIndicators = newValue)
                    .build());

            // Set an option for hideYellowChatIndicators
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideYellowChatIndicators"), NoReportsConfig.getInstance().hideYellowChatIndicators)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.hideYellowChatIndicators.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.getInstance().hideYellowChatIndicators = newValue)
                    .build());

            // Set an option for hideGrayChatIndicators
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideGrayChatIndicators"), NoReportsConfig.getInstance().hideGrayChatIndicators)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.hideGrayChatIndicators.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.getInstance().hideGrayChatIndicators = newValue)
                    .build());

            // Set an option for hideWarningToast
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideWarningToast"), NoReportsConfig.getInstance().hideWarningToast)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.hideWarningToast.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.getInstance().hideWarningToast = newValue)
                    .build());

            // Set an option for alwaysHideReportButton
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.alwaysHideReportButton"), NoReportsConfig.getInstance().alwaysHideReportButton)
                    .setDefaultValue(false)
                    .setTooltip(Component.translatable("option.NoChatReports.alwaysHideReportButton.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.getInstance().alwaysHideReportButton = newValue)
                    .build());

            // Set an option for versionEasterEgg
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.versionEasterEgg"), NoReportsConfig.getInstance().versionEasterEgg)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.versionEasterEgg.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.getInstance().versionEasterEgg = newValue)
                    .build());

            // Set an option for disableTelemetry
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.disableTelemetry"), NoReportsConfig.getInstance().disableTelemetry)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.disableTelemetry.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.getInstance().disableTelemetry = newValue)
                    .build());

            // Set an option for showReloadButton
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showReloadButton"), NoReportsConfig.getInstance().showReloadButton)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.showReloadButton.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.getInstance().showReloadButton = newValue)
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