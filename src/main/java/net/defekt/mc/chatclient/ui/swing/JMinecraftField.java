package net.defekt.mc.chatclient.ui.swing;

import java.awt.Color;
import java.awt.Font;

import net.defekt.mc.chatclient.ui.Main;
import net.defekt.mc.chatclient.ui.UserPreferences;

/**
 * A Minecraft styled text field.<br>
 * Other than appearance there is no difference between this text field and a
 * regular {@link JPlaceholderField}
 * 
 * @author Defective4
 *
 */
public class JMinecraftField extends JPlaceholderField {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 * 
	 * @param placeholder placeholder to display on this field
	 */
	public JMinecraftField(final String placeholder) {
		super(placeholder);
		final boolean unicodeFont = !Main.up.isLangUnicodeSupported()
				|| Main.up.getUnicodeCharactersMode().equals(UserPreferences.Constants.UNICODECHARS_KEY_FORCE_UNICODE);
		Font font;
		if (unicodeFont) {
			font = Font.decode(null).deriveFont(Font.BOLD).deriveFont((float) 16);
		} else {
			font = Main.mcFont.deriveFont((float) 14);
		}
		setBackground(Color.black);
		setForeground(Color.white);
		setCaretColor(Color.white);
		setFont(font);
		setText("");
	}

}
