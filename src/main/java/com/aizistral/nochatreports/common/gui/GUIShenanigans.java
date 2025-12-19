package com.aizistral.nochatreports.common.gui;

import com.aizistral.nochatreports.common.NCRCore;

import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.Identifier;

public final class GUIShenanigans {

	private GUIShenanigans() {
		throw new IllegalStateException("Can't touch this");
	}

	public static ComponentPath getLeaf(ComponentPath path) {
		while (path instanceof ComponentPath.Path cpath) {
			if (path != cpath.childPath()) {
				path = cpath.childPath();
			} else {
				break;
			}
		}

		return path;
	}

	public static WidgetSprites getSprites(String path) {
		return getSprites(path, true, true);
	}

	public static WidgetSprites getSprites(String path, boolean hasHovered) {
		return getSprites(path, hasHovered, true);
	}

	public static WidgetSprites getSprites(String path, boolean hasHovered, boolean hasDisabled) {
		var normal = Identifier.fromNamespaceAndPath("nochatreports", path);
		var hovered = hasHovered ? Identifier.fromNamespaceAndPath("nochatreports", path + "_hovered") : normal;
		var disabled = hasDisabled ? Identifier.fromNamespaceAndPath("nochatreports", path + "_hovered") : hovered;
		return new WidgetSprites(normal, hovered, disabled);
	}

}
