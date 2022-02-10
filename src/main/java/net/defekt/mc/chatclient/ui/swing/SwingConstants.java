package net.defekt.mc.chatclient.ui.swing;

public class SwingConstants {
	protected static final char[] minecraftiaChars = "\r\n!@#$%^&*()_+{}:\"|<>?`~,./;'\\[]-= abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZęóąśłżńĘÓĄŚŁŻŃ0123456789» ↙↘↗↖℅↓↑←→▲△■□●○§♀♂≥≤≠≈∫√÷×±”€"
			.toCharArray();

	protected static boolean checkMCSupported(String s) {
		for (char c : s.toCharArray())
			if (!containsChar(c))
				return false;
		return true;
	}

	private static boolean containsChar(char c) {
		for (int x = 0; x < minecraftiaChars.length; x++)
			if (minecraftiaChars[x] == c)
				return true;
		return false;
	}
}
