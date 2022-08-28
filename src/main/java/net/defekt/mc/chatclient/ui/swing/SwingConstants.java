package net.defekt.mc.chatclient.ui.swing;

/**
 * Internal constant UI values
 * 
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public class SwingConstants {
    protected static final char[] minecraftiaChars = "\r\n!@#$%^&*()_+{}:\"|<>?`~,./;'\\[]-= abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZĹşĹąÄ™ĂłÄ…Ĺ›Ĺ‚ĹĽĹ„Ä�Ă“Ä„ĹšĹ�Ĺ»Ĺ�0123456789Â» â†™â†�â†—â†–â„…â†“â†‘â†�â†’â–˛â–łâ– â–ˇâ—Źâ—‹Â§â™€â™‚â‰Ąâ‰¤â‰ â‰�â�«â�šĂ·Ă—Â±â€ťâ‚¬"
            .toCharArray();

    protected static boolean checkMCSupported(final String s) {
        for (final char c : s.toCharArray())
            if (!containsChar(c)) return false;
        return true;
    }

    private static boolean containsChar(final char c) {
        for (int x = 0; x < minecraftiaChars.length; x++)
            if (minecraftiaChars[x] == c) return true;
        return false;
    }
}
