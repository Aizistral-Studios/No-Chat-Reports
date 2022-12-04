package com.aizistral.nochatreports.core;

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
	ON_DEMAND;
}
