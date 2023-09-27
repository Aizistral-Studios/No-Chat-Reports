package com.aizistral.nochatreports.common.gui;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.gui.components.WidgetSprites;

public class SwitchableSprites {
	private final List<WidgetSprites> sprites;
	private int index = 0;

	private SwitchableSprites(WidgetSprites def, WidgetSprites... sprites) {
		ImmutableList.Builder<WidgetSprites> builder = ImmutableList.builder();

		builder.add(def);
		builder.add(sprites);

		this.sprites = builder.build();
	}

	public SwitchableSprites setIndex(int index) {
		if (index >= this.sprites.size()) {
			index = this.sprites.size() - 1;
		} else if (index < 0) {
			index = 0;
		}

		this.index = index;
		return this;
	}

	public int getIndex() {
		return this.index;
	}

	public WidgetSprites get(int index) {
		return this.sprites.get(index);
	}

	public WidgetSprites getDefault() {
		return this.get(0);
	}

	public WidgetSprites getCurrent() {
		return this.get(this.index);
	}

	public static SwitchableSprites of(WidgetSprites def, WidgetSprites... sprites) {
		return new SwitchableSprites(def, sprites);
	}

}
