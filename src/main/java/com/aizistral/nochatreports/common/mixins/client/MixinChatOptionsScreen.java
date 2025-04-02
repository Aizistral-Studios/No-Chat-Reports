package com.aizistral.nochatreports.common.mixins.client;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;

import com.aizistral.nochatreports.common.config.NCRConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.ChatOptionsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

/**
 * Mixin responsible for graying out "Only Show Secure Chat" option.
 *
 * @author Kevinthegreat (original implementation)
 * @author Aizistral (current version)
 */

@Mixin(ChatOptionsScreen.class)
public abstract class MixinChatOptionsScreen extends OptionsSubScreen {

	public MixinChatOptionsScreen() {
		super(null, null, null);
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
