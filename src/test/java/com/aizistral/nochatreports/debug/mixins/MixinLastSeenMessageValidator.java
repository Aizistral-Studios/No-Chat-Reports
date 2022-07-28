package com.aizistral.nochatreports.debug.mixins;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.chat.LastSeenMessagesValidator;
import net.minecraft.network.chat.LastSeenMessagesValidator.ErrorCondition;

@Mixin(LastSeenMessagesValidator.class)
public class MixinLastSeenMessageValidator {
	@Shadow @Final
	private static int NOT_FOUND;

	@Shadow
	private LastSeenMessages lastSeenMessages = LastSeenMessages.EMPTY;
	@Shadow @Final
	private ObjectList<LastSeenMessages.Entry> pendingEntries;

	private int calculateIndices(List<LastSeenMessages.Entry> lastSeenEntries, int[] indexArray, @Nullable LastSeenMessages.Entry unseenEntry) {
		int value = 0;
		int counter = 0;
		Arrays.fill(indexArray, NOT_FOUND);

		List<LastSeenMessages.Entry> previousSeenEntries = this.lastSeenMessages.entries();
		int previousSeenEntiresSize = previousSeenEntries.size();

		for (counter = previousSeenEntiresSize - 1; counter >= 0; --counter) {
			value = lastSeenEntries.indexOf(previousSeenEntries.get(counter));
			if (value == -1) {
				continue;
			}
			indexArray[value] = -counter - 1;
		}

		counter = NOT_FOUND;
		value = this.pendingEntries.size();

		for (int counter2 = 0; counter2 < value; ++counter2) {
			LastSeenMessages.Entry pendingEntry = this.pendingEntries.get(counter2);
			int pendingIndexInLastSeen = lastSeenEntries.indexOf(pendingEntry);

			if (pendingIndexInLastSeen != -1) {
				indexArray[pendingIndexInLastSeen] = counter2;
			}

			if (!pendingEntry.equals(unseenEntry)) {
				continue;
			}

			counter = counter2;
		}

		return counter;
	}

	public Set<ErrorCondition> validateAndUpdate(LastSeenMessages.Update update) {
		EnumSet<ErrorCondition> errors = EnumSet.noneOf(ErrorCondition.class);
		LastSeenMessages lastSeenMessages = update.lastSeen();
		LastSeenMessages.Entry unseenEntry = update.lastReceived().orElse(null);
		List<LastSeenMessages.Entry> lastSeenEntries = lastSeenMessages.entries();

		int previousSeenCount = this.lastSeenMessages.entries().size();
		int value = NOT_FOUND;
		int lastSeenCount = lastSeenEntries.size();

		if (lastSeenCount < previousSeenCount) {
			errors.add(ErrorCondition.REMOVED_MESSAGES);
		}

		int[] indexArray = new int[lastSeenCount];
		int indicesResult = this.calculateIndices(lastSeenEntries, indexArray, unseenEntry);

		for (int counter = lastSeenCount - 1; counter >= 0; --counter) {
			int index = indexArray[counter];

			if (index != NOT_FOUND) {
				if (index < value) {
					errors.add(ErrorCondition.OUT_OF_ORDER);
					continue;
				}
				value = index;
				continue;
			}

			errors.add(ErrorCondition.UNKNOWN_MESSAGES);
		}

		if (unseenEntry != null) {
			if (indicesResult == NOT_FOUND || indicesResult < value) {
				errors.add(ErrorCondition.UNKNOWN_MESSAGES);
			} else {
				value = indicesResult;
			}
		}

		if (value >= 0) {
			this.pendingEntries.removeElements(0, value + 1);
		}

		if (this.hasDuplicateProfiles(lastSeenMessages)) {
			errors.add(ErrorCondition.DUPLICATED_PROFILES);
		}

		this.lastSeenMessages = lastSeenMessages;
		return errors;
	}

	private boolean hasDuplicateProfiles(LastSeenMessages lastSeenMessages) {
		HashSet<UUID> set = new HashSet<UUID>(lastSeenMessages.entries().size());

		for (LastSeenMessages.Entry entry : lastSeenMessages.entries()) {
			if (set.add(entry.profileId())) {
				continue;
			}

			return true;
		}

		return false;
	}

}
