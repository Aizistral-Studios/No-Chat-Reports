package com.aizistral.nochatreports.mixins.client;

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
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

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
    private final List<FormattedCharSequence> secureChatTooltip = Minecraft.getInstance().font.split(Component.translatable("gui.nochatreport.secureChat"), 200);

    public MixinChatOptionsScreen(Screen screen, Options options, Component component, OptionInstance<?>[] optionInstances) {
        super(screen, options, component, optionInstances);
    }

    /**
     * Gray out the only show secure chat option by deactivating the button when the screen is initialized.
     *
     * @author Kevinthegreat
     */

    @Override
    protected void init() {
        super.init();
        onlyShowSecureChat = list.findOption(Minecraft.getInstance().options.onlyShowSecureChat());
        if (onlyShowSecureChat != null) {
            onlyShowSecureChat.active = false;
        }
    }

    /**
     * Render the tooltip on mouseover for only show secure chat option.
     * Minecraft only render tooltips when the widget is active.
     *
     * @author Kevinthegreat
     */

    @Override
    public void render(@NotNull PoseStack poseStack, int x, int y, float f) {
        super.render(poseStack, x, y, f);
        if (onlyShowSecureChat != null && onlyShowSecureChat.visible && x >= (double) onlyShowSecureChat.x && y >= (double) onlyShowSecureChat.y && x < (double) (onlyShowSecureChat.x + onlyShowSecureChat.getWidth()) && y < (double) (onlyShowSecureChat.y + onlyShowSecureChat.getHeight())) {
            renderTooltip(poseStack, secureChatTooltip, x, y);
        }
    }
}
