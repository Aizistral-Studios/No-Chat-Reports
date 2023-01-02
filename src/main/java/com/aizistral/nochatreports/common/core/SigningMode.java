package com.aizistral.nochatreports.common.core;

import org.jetbrains.annotations.Nullable;

import com.aizistral.nochatreports.common.config.NCRConfig;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * Signing modes control how NCR behaves with respect to chat signing.
 * These can be enabled globally or per-server.
 *
 * @author Aizistral
 */

public enum SigningMode {

	/**
	 * Special value that indicates signing behavior should be controlled by overarching
	 * configuration.
	 */
	DEFAULT,

	/**
	 * Never sign any messages, ignore server demands.
	 */
	NEVER,

	/**
	 * Always sign messages, regardless of whether the server asked to.
	 */
	ALWAYS,

	/**
	 * Don't sign by default, if server refuses to accept unsigned message - show GUI
	 * prompt which allows the user to enable signing or ignore server warnings for one
	 * session.
	 */
	PROMPT,

	/**
	 * Don't sign by default, if server refuses to accept unsigned message - immediately
	 * enable signing for one session and re-send that message.
	 */
	ON_DEMAND,

	/**
	 * Same as {@link SigningMode#NEVER}, but additionally indicates that signing cannot be
	 * turned on at all for some reason (i.e. missing keys or offline account).
	 */
	NEVER_FORCED;

	public String getNameKey() {
		return "gui.nochatreports.signing_mode." + this.name().toLowerCase();
	}

	public MutableComponent getName() {
		if (this != DEFAULT)
			return Component.translatable(this.getNameKey());
		else
			return Component.translatable(this.getNameKey(), NCRConfig.getClient().defaultSigningMode().getName());
	}

	public String getTooltipKey() {
		return "gui.nochatreports.signing_mode." + this.name().toLowerCase() + ".tooltip";
	}

	public MutableComponent getTooltip() {
		return Component.translatable(this.getTooltipKey());
	}

	public SigningMode next() {
		SigningMode result = null;

		if (this.ordinal() == values().length - 1) {
			result = values()[0];
		} else {
			result = values()[this.ordinal() + 1];
		}

		return result.isSelectable() ? result : result.next();
	}

	public SigningMode resolve() {
		return switch(this) {
		case DEFAULT -> NCRConfig.getClient().defaultSigningMode();
		case NEVER_FORCED -> NEVER;
		default -> this;
		};
	}

	public boolean isSelectable() {
		return this != NEVER_FORCED;
	}

	public boolean isSelectableGlobally() {
		return this.isSelectable() && this != DEFAULT;
	}

	public static SigningMode nullable(@Nullable SigningMode mode) {
		return mode == null ? DEFAULT : mode;
	}

}
