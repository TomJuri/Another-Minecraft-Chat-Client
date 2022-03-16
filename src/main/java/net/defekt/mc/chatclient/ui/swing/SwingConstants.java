package net.defekt.mc.chatclient.ui.swing;

/**
 * Internal constant UI values
 * 
 * @author Defective4
 *
 */
public class SwingConstants {
	protected static final char[] minecraftiaChars = "\r\n!@#$%^&*()_+{}:\"|<>?`~,./;'\\[]-= abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZęóąśłżńĘÓĄŚŁŻŃ0123456789» ↙↘↗↖℅↓↑←→▲△■□●○§♀♂≥≤≠≈∫√÷×±”€"
			.toCharArray();

	protected static boolean checkMCSupported(final String s) {
		for (final char c : s.toCharArray())
			if (!containsChar(c))
				return false;
		return true;
	}

	private static boolean containsChar(final char c) {
		for (int x = 0; x < minecraftiaChars.length; x++)
			if (minecraftiaChars[x] == c)
				return true;
		return false;
	}
}
