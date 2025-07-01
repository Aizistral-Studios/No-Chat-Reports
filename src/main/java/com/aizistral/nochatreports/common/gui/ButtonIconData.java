package com.aizistral.nochatreports.common.gui;

public record ButtonIconData(IconData iconEnabled, IconData iconDisabled) {
    public ButtonIconData(IconData icon) {
        this(icon, icon);
    }

    public IconData getIconData(boolean buttonActive) {
        return buttonActive ? iconEnabled : iconDisabled;
    }
}
