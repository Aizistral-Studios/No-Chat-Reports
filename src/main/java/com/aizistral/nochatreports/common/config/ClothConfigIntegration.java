package com.aizistral.nochatreports.common.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.aizistral.nochatreports.common.core.SigningMode;
import com.aizistral.nochatreports.common.gui.FontHelper;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;

public final class ClothConfigIntegration {
	public static final boolean ACTIVE;

	static {
		boolean ccp;

		try {
			Class.forName("me.shedaniel.clothconfig2.api.AbstractConfigListEntry");
			ccp = true;
		} catch (Throwable ex) {
			ccp = false;
		}

		ACTIVE = ccp;
	}

	private ClothConfigIntegration() {
		throw new IllegalStateException("Can't touch this");
	}

	private static String translateKey(String key) {
		return Language.getInstance().getOrDefault(key);
	}

	private static Component[] wrapTooltip(String text) {
		List<String> list = FontHelper.wrap(Minecraft.getInstance().font, text, 250);
		Component[] tooltip = new Component[list.size()];

		for (int i = 0; i < list.size(); i++) {
			tooltip[i] = Component.literal(list.get(i));
		}

		return tooltip;
	}

	private static Component[] makeTooltip(String key) {
		return wrapTooltip(translateKey(key));
	}

	public static Screen getConfigScreen(Screen parent) {
		return Internal.getConfigScreen(parent);
	}

	private static class Internal {

		// This has to be isolated in a subclass due to classloading issues
		protected static Screen getConfigScreen(Screen parent) {
			// Get the previous screen
			ConfigBuilder builder = ConfigBuilder.create()
					.setParentScreen(Minecraft.getInstance().screen)
					.setTitle(Component.translatable("configuration.NoChatReports.config"));

			// Set category
			ConfigCategory client = builder.getOrCreateCategory(Component.translatable("configuration.NoChatReports.category.client"));
			ConfigCategory lan = builder.getOrCreateCategory(Component.translatable("configuration.NoChatReports.category.lan"));

			ConfigEntryBuilder entryBuilder = builder.entryBuilder();

			// Set an option for enableMod
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.enableMod"), NCRConfig.getClient().enableMod)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.enableMod.tooltip"))
					.setSaveConsumer(newValue -> ServerSafetyState.scheduleResetAction(() -> {
						NCRConfig.getClient().enableMod = newValue;
						NCRConfig.getClient().saveFile();
					}))
					.build());

			// Set an option for showNCRButton
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showNCRButton"), NCRConfig.getClient().showNCRButton)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.showNCRButton.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().showNCRButton = newValue)
					.build());


			String signingTooltip = translateKey("option.NoChatReports.defaultSigningMode.tooltip")
					+ "\n ";

			for (SigningMode mode : SigningMode.values()) {
				if (mode.isSelectableGlobally()) {
					signingTooltip += "\n\n";
					signingTooltip += ChatFormatting.AQUA;
					signingTooltip += ChatFormatting.BOLD;
					signingTooltip += ChatFormatting.UNDERLINE;
					signingTooltip += translateKey(mode.getNameKey());
					signingTooltip += ChatFormatting.RESET;
					signingTooltip += "\n";
					signingTooltip += translateKey(mode.getTooltipKey());
				}
			}

			// Dropdown for defaultSigningMode
			client.addEntry(
					entryBuilder.startStringDropdownMenu(Component.translatable("option.NoChatReports.defaultSigningMode"),
							NCRConfig.getClient().defaultSigningMode.getName().getString())
					.setTooltip(wrapTooltip(signingTooltip))
					.setDefaultValue(SigningMode.PROMPT.getName().getString())
					.setSelections(Stream.of(SigningMode.values()).filter(SigningMode::isSelectableGlobally)
							.map(mode -> mode.getName().getString()).toList())
					.setSaveConsumer(newValue -> {
						NCRConfig.getClient().defaultSigningMode = Arrays.stream(SigningMode.values())
								.filter(mode -> mode.getName().getString().equals(newValue))
								.findFirst().get();
					})
					.setSuggestionMode(false)
					.build()
					);

			// Set an option for showReloadButton
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showReloadButton"), NCRConfig.getClient().showReloadButton)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.showReloadButton.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().showReloadButton = newValue)
					.build());

			// Set an option for verifiedIconEnabled
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.verifiedIconEnabled"), NCRConfig.getClient().verifiedIconEnabled)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.verifiedIconEnabled.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().verifiedIconEnabled = newValue)
					.build());

			// Set an option for verifiedIconOffsetX
			client.addEntry(entryBuilder.startIntField(Component.translatable("option.NoChatReports.verifiedIconOffsetX"), NCRConfig.getClient().verifiedIconOffsetX)
					.setDefaultValue(0)
					.setTooltip(makeTooltip("option.NoChatReports.verifiedIconOffsetX.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().verifiedIconOffsetX = newValue)
					.build());

			// Set an option for verifiedIconOffsetY
			client.addEntry(entryBuilder.startIntField(Component.translatable("option.NoChatReports.verifiedIconOffsetY"), NCRConfig.getClient().verifiedIconOffsetY)
					.setDefaultValue(0)
					.setTooltip(makeTooltip("option.NoChatReports.verifiedIconOffsetY.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().verifiedIconOffsetY = newValue)
					.build());

			// Warning for showEncryptionButton
			client.addEntry(entryBuilder.startTextDescription(Component.translatable("gui.NoChatReports.showEncryptionButtonWarning"))
					.build());

			// Set an option for showEncryptionButton
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showEncryptionButton"), NCRConfig.getEncryption().showEncryptionButton)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.showEncryptionButton.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getEncryption().showEncryptionButton = newValue)
					.build());

			// Set an option for showEncryptionIndicators
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showEncryptionIndicators"), NCRConfig.getEncryption().showEncryptionIndicators)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.showEncryptionIndicators.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getEncryption().showEncryptionIndicators = newValue)
					.build());

			// Set an option for showServerSafety
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.showServerSafety"), NCRConfig.getClient().showServerSafety)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.showServerSafety.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().showServerSafety = newValue)
					.build());

			// Add link for documentation for showServerSafety
			client.addEntry(entryBuilder.startTextDescription(Component.translatable("option.NoChatReports.showServerSafety.moreInfo")
					.withStyle(s -> s
							.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/Configuration-Files/#option-showserversafety"))).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/Configuration-Files/#option-showserversafety"))))
					.build());

			// Set an option for hideInsecureMessageIndicators
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideInsecureMessageIndicators"), NCRConfig.getClient().hideInsecureMessageIndicators)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.hideInsecureMessageIndicators.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().hideInsecureMessageIndicators = newValue)
					.build());

			// Set an option for hideModifiedMessageIndicators
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideModifiedMessageIndicators"), NCRConfig.getClient().hideModifiedMessageIndicators)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.hideModifiedMessageIndicators.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().hideModifiedMessageIndicators = newValue)
					.build());

			// Set an option for hideSystemMessageIndicators
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideSystemMessageIndicators"), NCRConfig.getClient().hideSystemMessageIndicators)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.hideSystemMessageIndicators.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().hideSystemMessageIndicators = newValue)
					.build());

			// Set an option for hideWarningToast
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideWarningToast"), NCRConfig.getClient().hideWarningToast)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.hideWarningToast.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().hideWarningToast = newValue)
					.build());

			// Set an option for hideSigningRequestMessage
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.hideSigningRequestMessage"), NCRConfig.getClient().hideSigningRequestMessage)
					.setDefaultValue(false)
					.setTooltip(makeTooltip("option.NoChatReports.hideSigningRequestMessage.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().hideSigningRequestMessage = newValue)
					.build());

			// Set an option for alwaysHideReportButton
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.alwaysHideReportButton"), NCRConfig.getClient().alwaysHideReportButton)
					.setDefaultValue(false)
					.setTooltip(makeTooltip("option.NoChatReports.alwaysHideReportButton.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().alwaysHideReportButton = newValue)
					.build());

			// Set an option for demandOnServer
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.demandOnServer"), NCRConfig.getClient().demandOnServer)
					.setDefaultValue(false)
					.setTooltip(makeTooltip("option.NoChatReports.demandOnServer.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().demandOnServer = newValue)
					.build());

			// Set an option for enableDebugLog
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.enableDebugLog"), NCRConfig.getCommon().enableDebugLog)
					.setDefaultValue(false)
					.setTooltip(makeTooltip("option.NoChatReports.enableDebugLog.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getCommon().enableDebugLog = newValue)
					.build());

			// Set an option for disableTelemetry
			client.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.disableTelemetry"), NCRConfig.getClient().disableTelemetry)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.disableTelemetry.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getClient().disableTelemetry = newValue)
					.build());

			// Set an option for demandOnClient
			lan.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.demandOnClient"), NCRConfig.getCommon().demandOnClient)
					.setDefaultValue(false)
					.setTooltip(makeTooltip("option.NoChatReports.demandOnClient.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getCommon().demandOnClient = newValue)
					.build());

			// Set an option for demandOnClientMessage
			lan.addEntry(entryBuilder.startStrField(Component.translatable("option.NoChatReports.demandOnClientMessage"), NCRConfig.getCommon().demandOnClientMessage)
					.setDefaultValue(NCRConfig.getCommon().demandOnClientMessage)
					.setTooltip(makeTooltip("option.NoChatReports.demandOnClientMessage.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getCommon().demandOnClientMessage = newValue)
					.build());

			// Set an option for convertToGameMessage
			lan.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.convertToGameMessage"), NCRConfig.getCommon().convertToGameMessage)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.convertToGameMessage.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getCommon().convertToGameMessage = newValue)
					.build());

			// Set an option for addQueryData
			lan.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.NoChatReports.addQueryData"), NCRConfig.getCommon().addQueryData)
					.setDefaultValue(true)
					.setTooltip(makeTooltip("option.NoChatReports.addQueryData.tooltip"))
					.setSaveConsumer(newValue -> NCRConfig.getCommon().addQueryData = newValue)
					.build());

			// Save config
			builder.setSavingRunnable(NCRConfig::save);
			return builder.build();
		}
	}

}
