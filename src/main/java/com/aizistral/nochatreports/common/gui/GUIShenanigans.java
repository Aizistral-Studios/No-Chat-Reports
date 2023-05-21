package com.aizistral.nochatreports.common.gui;

import net.minecraft.client.gui.ComponentPath;

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

}
