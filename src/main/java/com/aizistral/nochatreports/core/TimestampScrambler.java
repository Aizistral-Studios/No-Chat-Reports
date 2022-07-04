package com.aizistral.nochatreports.core;

import java.time.Instant;
import java.util.Random;

import com.aizistral.nochatreports.NoChatReports;

import net.minecraft.network.chat.MessageSigner;

/**
 * My implementation is independent, but I credit
 * {@link <a href = "https://modrinth.com/mod/delayedreports">Delayed Reports<a/>}
 * for the idea.
 *
 * @author Aizistral
 */

public class TimestampScrambler {
	private static final Random THEY_SEE_ME_ROLLIN = new Random();
	private static final int MIN_OFFSET = -270, MAX_OFFSET = 90;
	private static Instant lastTimestamp = Instant.now().plusSeconds(MIN_OFFSET);

	private TimestampScrambler() {
		throw new IllegalStateException("Can't touch this");
	}

	public static MessageSigner randomizeTimestamp(MessageSigner original) {
		return new MessageSigner(original.sender(), randomizeTimestamp(original.timeStamp()), original.salt());
	}

	public static Instant randomizeTimestamp(Instant original) {
		long diff = -(original.getEpochSecond() - lastTimestamp.getEpochSecond());
		long offset = between(diff < MIN_OFFSET ? MIN_OFFSET : diff, MAX_OFFSET);

		NoChatReports.LOGGER.info("Offset: " + offset);

		return lastTimestamp = original.plusSeconds(offset);
	}

	private static long between(long one, long two) {
		return one + THEY_SEE_ME_ROLLIN.nextLong(two - one);
	}

}
