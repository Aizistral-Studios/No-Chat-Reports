package com.aizistral.nochatreports.common.gui;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class SwitchableButtonIcon {
    private final List<ButtonIconData> icons;
    private int index = 0;

    public SwitchableButtonIcon(ButtonIconData defaultIcon, ButtonIconData... otherIcons) {
        ImmutableList.Builder<ButtonIconData> builder = ImmutableList.builder();
        builder.add(defaultIcon);
        builder.add(otherIcons);
        this.icons = builder.build();
    }

    public SwitchableButtonIcon setIndex(int index) {
        this.index = Math.clamp(index, 0, icons.size() - 1);
        return this;
    }

    public int getIndex() {
        return index;
    }

    public ButtonIconData get(int index) {
        return icons.get(index);
    }

    public ButtonIconData getDefault() {
        return get(0);
    }

    public ButtonIconData getCurrent() {
        return get(index);
    }
}
