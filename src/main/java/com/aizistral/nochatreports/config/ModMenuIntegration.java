package com.aizistral.nochatreports.config;

import java.util.ArrayList;
import java.util.List;

import com.aizistral.nochatreports.gui.FontHelper;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;

/**
 * Implementation of ModMenu and ClothConfig support for the mod.
 *
 * @author MODKILLER1001
 */

@Environment(EnvType.CLIENT)
public final class ModMenuIntegration implements ModMenuApi {

	private Component[] makeTooltip(String key) {
		String localized = Language.getInstance().getOrDefault(key);
		List<String> list = FontHelper.wrap(Minecraft.getInstance().font, localized, 250);
		Component[] tooltip = new Component[list.size()];

		for (int i = 0; i < list.size(); i++) {
			tooltip[i] = Component.literal(list.get(i));
		}

		return tooltip;
	}

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return screen -> {

			// TODO Sort config options into client and common instead of visual and technical

			// Get the previous screen
			ConfigBuilder builder = ConfigBuilder.create()
					.setParentScreen(Minecraft.getInstance().screen)
					.setTitle(Component.translatable("configuration.NoChatReports.config"));

			// Set category
			ConfigCategory visual = builder.getOrCreateCategory(Component.translatable("configuration.NoChatReports.category.visual"));
			ConfigCategory technical = builder.getOrCreateCategory(Component.translatable("configuration.NoChatReports.category.technical"));
			ConfigCategory whitelistedServers = builder.getOrCreateCategory(Component.translatable("configuration.NoChatReports.category.whitelistedServers"));
			ConfigCategory lanOptions = builder.getOrCreateCategory(Component.translatable("configuration.NoChatReports.category.lanOptions"));

			ConfigEntryBuilder entryBuilder = builder.entryBuilder();

			// Set an option for showServerSafety
			visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showServerSafety"), NCRConfig.getClient().showServerSafety)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.showServerSafety.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().showServerSafety = newValue)
					.build());

			// Add link for documentation for showServerSafety
			visual.addEntry(entryBuilder.startTextDescription(Component.translatable("option.NoChatReports.showServerSafety.moreInfo")
					.withStyle(s -> s
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/Configuration-Files/#option-showserversafety"))).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/Configuration-Files/#option-showserversafety"))))
					.build());

			// Set an option for verifiedIconEnabled
			visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.verifiedIconEnabled"), NCRConfig.getClient().verifiedIconEnabled)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.verifiedIconEnabled.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().verifiedIconEnabled = newValue)
					.build());

			// Set an option for verifiedIconOffsetX
			visual.addEntry(entryBuilder.startIntField(Component.translatable("option.NoChatReports.verifiedIconOffsetX"), NCRConfig.getClient().verifiedIconOffsetX)
					.setDefaultValue(0)
					.setTooltip(this.makeTooltip("option.NoChatReports.verifiedIconOffsetX.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().verifiedIconOffsetX = newValue)
					.build());

			// Set an option for verifiedIconOffsetY
			visual.addEntry(entryBuilder.startIntField(Component.translatable("option.NoChatReports.verifiedIconOffsetY"), NCRConfig.getClient().verifiedIconOffsetY)
					.setDefaultValue(0)
					.setTooltip(this.makeTooltip("option.NoChatReports.verifiedIconOffsetY.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().verifiedIconOffsetY = newValue)
					.build());

			// Set an option for hideRedChatIndicators
			visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideRedChatIndicators"), NCRConfig.getClient().hideRedChatIndicators)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.hideRedChatIndicators.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().hideRedChatIndicators = newValue)
					.build());

			// Set an option for hideYellowChatIndicators
			visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideYellowChatIndicators"), NCRConfig.getClient().hideYellowChatIndicators)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.hideYellowChatIndicators.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().hideYellowChatIndicators = newValue)
					.build());

			// Set an option for hideGrayChatIndicators
			visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideGrayChatIndicators"), NCRConfig.getClient().hideGrayChatIndicators)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.hideGrayChatIndicators.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().hideGrayChatIndicators = newValue)
					.build());

			// Set an option for hideWarningToast
			visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideWarningToast"), NCRConfig.getClient().hideWarningToast)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.hideWarningToast.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().hideWarningToast = newValue)
					.build());

			// Set an option for alwaysHideReportButton
			visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.alwaysHideReportButton"), NCRConfig.getClient().alwaysHideReportButton)
					.setDefaultValue(false)
					.setTooltip(this.makeTooltip("option.NoChatReports.alwaysHideReportButton.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().alwaysHideReportButton = newValue)
					.build());

			// Set an option for demandOnServer
			technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.demandOnServer"), NCRConfig.getClient().demandOnServer)
					.setDefaultValue(false)
					.setTooltip(this.makeTooltip("option.NoChatReports.demandOnServer.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().demandOnServer = newValue)
					.build());

			// Set an option for enableDebugLog
			technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.enableDebugLog"), NCRConfig.getCommon().enableDebugLog)
					.setDefaultValue(false)
					.setTooltip(this.makeTooltip("option.NoChatReports.enableDebugLog.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getCommon().enableDebugLog = newValue)
					.build());

			// Set an option for disableTelemetry
			technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.disableTelemetry"), NCRConfig.getClient().disableTelemetry)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.disableTelemetry.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().disableTelemetry = newValue)
					.build());

			// Set an option for showReloadButton
			technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showReloadButton"), NCRConfig.getClient().showReloadButton)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.showReloadButton.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().showReloadButton = newValue)
					.build());

			// Set an option for whitelistAllServers
			technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.whitelistAllServers"), NCRConfig.getClient().whitelistAllServers)
					.setDefaultValue(false)
					.setTooltip(this.makeTooltip("option.NoChatReports.whitelistAllServers.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().whitelistAllServers = newValue)
					.build());

			// Set an option for showNCRButton
			technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showNCRButton"), NCRConfig.getClient().showNCRButton)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.showNCRButton.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().showNCRButton = newValue)
					.build());

			// Set an option for enableMod
			technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.enableMod"), NCRConfig.getClient().enableMod)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.enableMod.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().enableMod = newValue)
					.build());

			// Instructions for adding servers
			whitelistedServers.addEntry(entryBuilder.startTextDescription(Component.translatable("option.NoChatReports.whitelistedServers.instructions"))
					.build());

			// Add or remove whitelisted servers
			whitelistedServers.addEntry(entryBuilder.startStrList(Component.translatable("option.NoChatReports.whitelistedServers"), NCRConfig.getServerWhitelist().whitelistedServers)
					.setExpanded(true)
					.setInsertInFront(true)
					.setDefaultValue(NCRServerWhitelist.DEFAULT_WHITELISTED_SERVERS)
					.setTooltip(this.makeTooltip("option.NoChatReports.whitelistedServers.tooltip"))
					.setAddButtonTooltip(Component.translatable("option.NoChatReports.whitelistedServers.addButtonTooltip"))
					.setRemoveButtonTooltip(Component.translatable("option.NoChatReports.whitelistedServers.removeButtonTooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getServerWhitelist().whitelistedServers = newValue)
					.setCreateNewInstance(baseListEntry -> new StringListListEntry.StringListCell(Language.getInstance().getOrDefault(("option.NoChatReports.whitelistedServers.serverAddress")), baseListEntry))
					.build());

			// Set an option for demandOnClient
			lanOptions.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.demandOnClient"), NCRConfig.getCommon().demandOnClient)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.demandOnClient.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getCommon().demandOnClient = newValue)
					.build());

			// Set an option for convertToGameMessage
			lanOptions.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.convertToGameMessage"), NCRConfig.getCommon().convertToGameMessage)
					.setDefaultValue(false)
					.setTooltip(this.makeTooltip("option.NoChatReports.convertToGameMessage.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getCommon().convertToGameMessage = newValue)
					.build());

			// Set an option for addQueryData
			lanOptions.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.addQueryData"), NCRConfig.getCommon().addQueryData)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.addQueryData.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getCommon().addQueryData = newValue)
					.build());

			// Save config
			builder.setSavingRunnable(NCRConfig::save);
			return builder.build();
		};
	}

}