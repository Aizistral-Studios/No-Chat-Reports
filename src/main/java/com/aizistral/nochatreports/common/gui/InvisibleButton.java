package com.aizistral.nochatreports.common.gui;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class InvisibleButton extends ButtonWidget {

	public InvisibleButton() {
		super(0, 0, 20, 20, Text.empty(), btn -> {}, Supplier::get);
	}

	@Override
	public void render(MatrixStack poseStack, int i, int j, float f) {
		// NO-OP
	}

}
