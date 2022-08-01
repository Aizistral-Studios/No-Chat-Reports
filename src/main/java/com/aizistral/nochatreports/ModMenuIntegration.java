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

            // Set an option for demandOnClient
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.demandOnClient"), NoReportsConfig.demandOnClient)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.demandOnClient.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.demandOnClient = newValue)
                    .build());

            // Set an option for enableDebugLog
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.enableDebugLog"), NoReportsConfig.demandOnClient)
                    .setDefaultValue(false)
                    .setTooltip(Component.translatable("option.NoChatReports.enableDebugLog.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.demandOnClient = newValue)
                    .build());

            // Set an option for convertToGameMessage
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.convertToGameMessage"), NoReportsConfig.demandOnClient)
                    .setDefaultValue(false)
                    .setTooltip(Component.translatable("option.NoChatReports.convertToGameMessage.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.demandOnClient = newValue)
                    .build());

            // Set an option for showServerSafety
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showServerSafety"), NoReportsConfig.demandOnClient)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.showServerSafety.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.demandOnClient = newValue)
                    .build());

            // Set an option for hideRedChatIndicators
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideRedChatIndicators"), NoReportsConfig.demandOnClient)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.hideRedChatIndicators.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.demandOnClient = newValue)
                    .build());

            // Set an option for hideYellowChatIndicators
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideYellowChatIndicators"), NoReportsConfig.demandOnClient)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.hideYellowChatIndicators.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.demandOnClient = newValue)
                    .build());

            // Set an option for alwaysHideReportButton
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.alwaysHideReportButton"), NoReportsConfig.demandOnClient)
                    .setDefaultValue(false)
                    .setTooltip(Component.translatable("option.NoChatReports.alwaysHideReportButton.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.demandOnClient = newValue)
                    .build());

            // Set an option for versionEasterEgg
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.versionEasterEgg"), NoReportsConfig.demandOnClient)
                    //.setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.versionEasterEgg.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.demandOnClient = newValue)
                    .build());

            // Set an option for disableTelemetry
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.disableTelemetry"), NoReportsConfig.demandOnClient)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.disableTelemetry.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.demandOnClient = newValue)
                    .build());

            // Set an option for showReloadButton
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showReloadButton"), NoReportsConfig.demandOnClient)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("option.NoChatReports.showReloadButton.tooltip"))
                    .setSaveConsumer(newValue -> NoReportsConfig.demandOnClient = newValue)
                    .build());

            // Save config
            builder.setSavingRunnable(() -> {
                // Create a Property
                Properties configProperties = new Properties();
                // Set the property
                configProperties.setProperty("demandOnClient", String.valueOf(NoReportsConfig.demandOnClient));
                configProperties.setProperty("enableDebugLog", String.valueOf(NoReportsConfig.enableDebugLog));
                configProperties.setProperty("convertToGameMessage", String.valueOf(NoReportsConfig.convertToGameMessage));
                configProperties.setProperty("showServerSafety", String.valueOf(NoReportsConfig.showServerSafety));
                configProperties.setProperty("hideRedChatIndicators", String.valueOf(NoReportsConfig.hideRedChatIndicators));
                configProperties.setProperty("hideYellowChatIndicators", String.valueOf(NoReportsConfig.hideYellowChatIndicators));
                configProperties.setProperty("hideGrayChatIndicators", String.valueOf(NoReportsConfig.hideGrayChatIndicators));
                configProperties.setProperty("hideWarningToast", String.valueOf(NoReportsConfig.hideWarningToast));
                configProperties.setProperty("alwaysHideReportButton", String.valueOf(NoReportsConfig.alwaysHideReportButton));
                configProperties.setProperty("versionEasterEgg", String.valueOf(NoReportsConfig.versionEasterEgg));
                configProperties.setProperty("disableTelemetry", String.valueOf(NoReportsConfig.disableTelemetry));
                configProperties.setProperty("showReloadButton", String.valueOf(NoReportsConfig.showReloadButton));
                // Save the properties
                NoReportsConfig.saveConfig();
                NoChatReports.LOGGER.info("line 121 logger info config");
                NoReportsConfig.loadConfig();
            });


            return builder.build();
        };
    }


}