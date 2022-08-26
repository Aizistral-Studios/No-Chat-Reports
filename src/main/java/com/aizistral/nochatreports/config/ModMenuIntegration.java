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
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.Component;

/**
 * Implementation of ModMenu and ClothConfig support for the mod.
 *
 * @author MODKILLER1001
 */

@Environment(EnvType.CLIENT)
public final class ModMenuIntegration implements ModMenuApi {
	private static final List<String> DEFAULT_WHITELISTED_SERVERS = new ArrayList<>();

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
			visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showServerSafety"), NCRConfig.getInstance().showServerSafety)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.showServerSafety.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getInstance().showServerSafety = newValue)
					.build());

			// Add link for documentation for showServerSafety
			visual.addEntry(entryBuilder.startTextDescription(Component.translatable("option.NoChatReports.showServerSafety.moreInfo")
					.withStyle(s -> s
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/Configuration-Files/#option-showserversafety"))).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/Configuration-Files/#option-showserversafety"))))
					.build());

			// Set an option for hideRedChatIndicators
			visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideRedChatIndicators"), NCRConfig.getInstance().hideRedChatIndicators)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.hideRedChatIndicators.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getInstance().hideRedChatIndicators = newValue)
					.build());

			// Set an option for hideYellowChatIndicators
			visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideYellowChatIndicators"), NCRConfig.getInstance().hideYellowChatIndicators)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.hideYellowChatIndicators.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getInstance().hideYellowChatIndicators = newValue)
					.build());

			// Set an option for hideGrayChatIndicators
			visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideGrayChatIndicators"), NCRConfig.getInstance().hideGrayChatIndicators)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.hideGrayChatIndicators.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getInstance().hideGrayChatIndicators = newValue)
					.build());

			// Set an option for hideWarningToast
			visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideWarningToast"), NCRConfig.getInstance().hideWarningToast)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.hideWarningToast.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getInstance().hideWarningToast = newValue)
					.build());

			// Set an option for alwaysHideReportButton
			visual.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.alwaysHideReportButton"), NCRConfig.getInstance().alwaysHideReportButton)
					.setDefaultValue(false)
					.setTooltip(this.makeTooltip("option.NoChatReports.alwaysHideReportButton.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getInstance().alwaysHideReportButton = newValue)
					.build());

			// Set an option for demandOnServer
			technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.demandOnServer"), NCRConfig.getInstance().demandOnServer)
					.setDefaultValue(false)
					.setTooltip(this.makeTooltip("option.NoChatReports.demandOnServer.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getInstance().demandOnServer = newValue)
					.build());

			// Set an option for enableDebugLog
			technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.enableDebugLog"), NCRConfig.getInstance().enableDebugLog)
					.setDefaultValue(false)
					.setTooltip(this.makeTooltip("option.NoChatReports.enableDebugLog.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getInstance().enableDebugLog = newValue)
					.build());

			// Set an option for convertToGameMessage
			technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.convertToGameMessage"), NCRConfig.getInstance().convertToGameMessage)
					.setDefaultValue(false)
					.setTooltip(this.makeTooltip("option.NoChatReports.convertToGameMessage.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getInstance().convertToGameMessage = newValue)
					.build());

			// Set an option for disableTelemetry
			technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.disableTelemetry"), NCRConfig.getInstance().disableTelemetry)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.disableTelemetry.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getInstance().disableTelemetry = newValue)
					.build());

			// Set an option for showReloadButton
			technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showReloadButton"), NCRConfig.getInstance().showReloadButton)
					.setDefaultValue(true)
					.setTooltip(this.makeTooltip("option.NoChatReports.showReloadButton.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getInstance().showReloadButton = newValue)
					.build());

			// Set an option for whitelistAllServers
			technical.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.whitelistAllServers"), NCRConfig.getInstance().whitelistAllServers)
					.setDefaultValue(false)
					.setTooltip(this.makeTooltip("option.NoChatReports.whitelistAllServers.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getInstance().whitelistAllServers = newValue)
					.build());

			// Instructions for adding servers
			whitelistedServers.addEntry(entryBuilder.startTextDescription(Component.translatable("option.NoChatReports.whitelistedServers.instructions"))
					.build());

			// Add or remove whitelisted servers
			whitelistedServers.addEntry(entryBuilder.startStrList(Component.translatable("option.NoChatReports.whitelistedServers"), NCRConfig.getInstance().whitelistedServers)
					.setExpanded(true)
					.setInsertInFront(true)
					.setDefaultValue(DEFAULT_WHITELISTED_SERVERS)
					.setTooltip(this.makeTooltip("option.NoChatReports.whitelistedServers.tooltip"))
					.setAddButtonTooltip(Component.translatable("option.NoChatReports.whitelistedServers.addButtonTooltip"))
					.setRemoveButtonTooltip(Component.translatable("option.NoChatReports.whitelistedServers.removeButtonTooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getInstance().whitelistedServers = newValue)
					.setCreateNewInstance(baseListEntry -> new StringListListEntry.StringListCell(Language.getInstance().getOrDefault(("option.NoChatReports.whitelistedServers.serverAddress")), baseListEntry))
					.build());

			// Save config
			builder.setSavingRunnable(() -> {
				NCRConfig.saveConfig();
				NCRConfig.loadConfig();
			});
			return builder.build();
		};
	}

}
