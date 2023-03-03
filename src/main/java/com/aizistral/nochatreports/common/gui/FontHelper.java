package com.aizistral.nochatreports.common.gui;

import java.util.Arrays;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.util.FormattedCharSequence;

/**
 * I couldn't find a way to properly convert {@link FormattedCharSequence} back into
 * normal string. Lol.
 *
 * @author Aizistral
 */

@Environment(EnvType.CLIENT)
public final class FontHelper {

	private FontHelper() {
		throw new IllegalStateException("Can't touch this");
	}

	/**
	 * Breaks a string into a list of pieces that will fit a specified width.
	 */

	public static List<String> wrap(Font font, String str, int wrapWidth) {
		return Arrays.asList(wrapFormattedStringToWidth(font, str, wrapWidth).split("\n"));
	}

	/**
	 * Inserts newline and formatting into a string to wrap it within the specified
	 * width.
	 */

	private static String wrapFormattedStringToWidth(Font font, String str, int wrapWidth) {
		int j = sizeStringToWidth(font, str, wrapWidth);

		if (str.length() <= j)
			return str;
		else {
			String s1 = str.substring(0, j);
			char c0 = str.charAt(j);
			boolean flag = c0 == 32 || c0 == 10;
			String s2 = getFormatFromString(s1) + str.substring(j + (flag ? 1 : 0));
			return s1 + "\n" + wrapFormattedStringToWidth(font, s2, wrapWidth);
		}
	}

	/**
	 * Determines how many characters from the string will fit into the specified
	 * width.
	 */

	private static int sizeStringToWidth(Font font, String str, int wrapWidth) {
		int j = str.length();
		int k = 0;
		int l = 0;
		int i1 = -1;

		for (boolean flag = false; l < j; ++l) {
			char c0 = str.charAt(l);

			switch (c0) {
			case 10:
				--l;
				break;
			case 167:
				if (l < j - 1) {
					++l;
					char c1 = str.charAt(l);

					if (c1 != 108 && c1 != 76) {
						if (c1 == 114 || c1 == 82 || isFormatColor(c1)) {
							flag = false;
						}
					} else {
						flag = true;
					}
				}

				break;
			case 32:
				i1 = l;
			default:
				k += font.width(String.valueOf(c0));

				if (flag) {
					++k;
				}
			}

			if (c0 == 10) {
				++l;
				i1 = l;
				break;
			}

			if (k > wrapWidth) {
				break;
			}
		}

		return l != j && i1 != -1 && i1 < l ? i1 : l;
	}

	/**
	 * Checks if the char code is a hexadecimal character, used to set colour.
	 */

	private static boolean isFormatColor(char colorChar) {
		return colorChar >= 48 && colorChar <= 57 || colorChar >= 97 && colorChar <= 102
				|| colorChar >= 65 && colorChar <= 70;
	}

	/**
	 * Checks if the char code is O-K...lLrRk-o... used to set special formatting.
	 */

	private static boolean isFormatSpecial(char formatChar) {
		return formatChar >= 107 && formatChar <= 111 || formatChar >= 75 && formatChar <= 79 || formatChar == 114
				|| formatChar == 82;
	}

	/**
	 * Digests a string for nonprinting formatting characters then returns a string
	 * containing only that formatting.
	 */

	private static String getFormatFromString(String text) {
		String s1 = "";
		int i = -1;
		int j = text.length();

		while ((i = text.indexOf(167, i + 1)) != -1) {
			if (i < j - 1) {
				char c0 = text.charAt(i + 1);

				if (isFormatColor(c0)) {
					s1 = "\u00a7" + c0;
				} else if (isFormatSpecial(c0)) {
					s1 = s1 + "\u00a7" + c0;
				}
			}
		}

		return s1;
	}

}
