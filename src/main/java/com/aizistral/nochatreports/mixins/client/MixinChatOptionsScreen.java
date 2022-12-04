package com.aizistral.nochatreports.mixins.client;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import com.aizistral.nochatreports.config.NCRConfig;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.ChatOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

/**
 * Mixin responsible for graying out "Only Show Secure Chat" option.
 *
 * @author Kevinthegreat (original implementation)
 * @author Aizistral (current version)
 */

@Mixin(ChatOptionsScreen.class)
public class MixinChatOptionsScreen extends SimpleOptionsSubScreen {

	public MixinChatOptionsScreen(Screen screen, Options options, Component component, OptionInstance<?>... optionInstances) {
		super(screen, options, component, optionInstances);
		throw new IllegalStateException("Can't touch this");
	}

	@Override
	protected void init() {
		super.init();

		if (NCRConfig.getClient().enableMod()) {
			Optional.ofNullable(this.list.findOption(Minecraft.getInstance().options.onlyShowSecureChat()))
			.ifPresent(option -> option.active = false);
		}
	}

}
