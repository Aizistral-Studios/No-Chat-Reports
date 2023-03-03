package com.aizistral.nochatreports.common.mixins.client;

import java.util.Optional;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ChatOptionsScreen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import com.aizistral.nochatreports.common.config.NCRConfig;

/**
 * Mixin responsible for graying out "Only Show Secure Chat" option.
 *
 * @author Kevinthegreat (original implementation)
 * @author Aizistral (current version)
 */

@Mixin(ChatOptionsScreen.class)
public class MixinChatOptionsScreen extends SimpleOptionsScreen {

	public MixinChatOptionsScreen(Screen screen, GameOptions options, Text component, SimpleOption<?>... optionInstances) {
		super(screen, options, component, optionInstances);
		throw new IllegalStateException("Can't touch this");
	}

	@Override
	protected void init() {
		super.init();

		if (NCRConfig.getClient().enableMod()) {
			Optional.ofNullable(this.buttonList.getWidgetFor(MinecraftClient.getInstance().options.getOnlyShowSecureChat()))
			.ifPresent(option -> option.active = false);
		}
	}

}
