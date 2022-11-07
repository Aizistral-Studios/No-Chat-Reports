package com.aizistral.nochatreports.mixins.client;

import java.util.List;

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

@Mixin(ChatOptionsScreen.class)
public class MixinChatOptionsScreen extends SimpleOptionsSubScreen {
	private AbstractWidget onlyShowSecureChat;

	/**
	 * @implNote Using {@link net.minecraft.client.gui.Font#split} because
	 * {@link net.minecraft.client.OptionInstance#splitTooltip(Minecraft, Component)} is protected.
	 * Field {@link net.minecraft.client.gui.screens.Screen#minecraft} is not used because it can be null.
	 * @author Kevinthegreat
	 */
	@SuppressWarnings({"JavaDoc", "JavadocReference"})
	private List<FormattedCharSequence> secureChatTooltip;

	public MixinChatOptionsScreen(Screen screen, Options options, Component component, OptionInstance<?>[] optionInstances) {
		super(screen, options, component, optionInstances);
		throw new IllegalStateException("Can't touch this");
	}

	/**
	 * Gray out the only show secure chat option by deactivating the button when the screen is initialized.
	 * @author Kevinthegreat
	 */

	@Override
	protected void init() {
		super.init();

		if (!NCRConfig.getClient().enableMod())
			return;

		this.secureChatTooltip = Minecraft.getInstance().font.split(Component.translatable("gui.nochatreports.secure_chat"), 200);
		this.onlyShowSecureChat = this.list.findOption(Minecraft.getInstance().options.onlyShowSecureChat());

		if (this.onlyShowSecureChat != null) {
			this.onlyShowSecureChat.active = false;
		}
	}

	/**
	 * Render the tooltip on mouseover for only show secure chat option.
	 * Minecraft only render tooltips when the widget is active.
	 * @author Kevinthegreat
	 */

	@Override
	public void render(@NotNull PoseStack poseStack, int x, int y, float f) {
		super.render(poseStack, x, y, f);

		if (!NCRConfig.getClient().enableMod())
			return;

		if (this.onlyShowSecureChat != null && this.onlyShowSecureChat.visible && x >= (double) this.onlyShowSecureChat.getX() && y >= (double) this.onlyShowSecureChat.getY() && x < (double) (this.onlyShowSecureChat.getX() + this.onlyShowSecureChat.getWidth()) && y < (double) (this.onlyShowSecureChat.getY() + this.onlyShowSecureChat.getHeight())) {
			this.renderTooltip(poseStack, this.secureChatTooltip, x, y);
		}
	}
}