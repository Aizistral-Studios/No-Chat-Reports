package com.aizistral.nochatreports.core;

import org.jetbrains.annotations.Nullable;

import com.aizistral.nochatreports.config.NCRConfig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

	public MutableComponent getName() {
		String key = "gui.nochatreports.signing_mode." + this.name().toLowerCase();

		if (this != DEFAULT)
			return Component.translatable(key);
		else
			return Component.translatable(key, NCRConfig.getClient().defaultSigningMode().getName());
	}

	public MutableComponent getTooltip() {
		return Component.translatable("gui.nochatreports.signing_mode." + this.name().toLowerCase() +
				".tooltip");
	}

	public SigningMode next() {
		SigningMode result = null;

		if (this.ordinal() == values().length - 1) {
			result = values()[0];
		} else {
			result = values()[this.ordinal() + 1];
		}

		return result == NEVER_FORCED ? result.next() : result;
	}

	public SigningMode resolve() {
		return switch(this) {
		case DEFAULT -> NCRConfig.getClient().defaultSigningMode();
		case NEVER_FORCED -> NEVER;
		default -> this;
		};
	}

	public static SigningMode nullable(@Nullable SigningMode mode) {
		return mode == null ? DEFAULT : mode;
	}

}
