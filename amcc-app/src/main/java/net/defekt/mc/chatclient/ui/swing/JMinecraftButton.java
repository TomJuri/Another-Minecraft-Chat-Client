package net.defekt.mc.chatclient.ui.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.GlyphVector;

import javax.swing.JButton;

import net.defekt.mc.chatclient.protocol.data.UserPreferences;
import net.defekt.mc.chatclient.protocol.data.UserPreferences.ColorPreferences;
import net.defekt.mc.chatclient.ui.Main;

/**
 * A Minecraft styled button.<br>
 * Other than appearance, there is no difference between this button and a
 * regular {@link JButton}
 * 
 * @author Defective4
 *
 */
public class JMinecraftButton extends JButton {
    private static final long serialVersionUID = 1L;
    private boolean hover = false;
    private boolean unicodeFont = false;
    private final UserPreferences up = Main.up;
    private ColorPreferences cp = up.getColorPreferences();

    /**
     * Default constructor
     * 
     * @param text text that should appear on button
     */
    public JMinecraftButton(final String text) {
        super(text);
        unicodeFont = !SwingConstants.checkMCSupported(text)
                || Main.up.getUnicodeCharactersMode().equals(UserPreferences.Constants.UNICODECHARS_KEY_FORCE_UNICODE);
        Font font;
        if (unicodeFont) {
            font = Font.decode(null).deriveFont(Font.BOLD).deriveFont((float) 16);
        } else {
            font = Main.mcFont.deriveFont((float) 14);
        }
        setFont(font);
        int u, b, l, r;
        final Insets is = getMargin();
        u = is.top + 3;
        b = is.bottom + 3;
        l = is.left + 3;
        r = is.right + 3;
        setMargin(new Insets(u, l, b, r));
        if (!up.isDisableCustomButtons()) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(final MouseEvent e) {
                    hover = false;
                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                    hover = true;
                }
            });
        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        if (!up.isDisableCustomButtons()) {
            final Graphics2D g2 = (Graphics2D) g;
            g2.setFont(getFont());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setColor(Color.black);

            g2.fillRect(0, 0, 2, getHeight());
            g2.fillRect(getWidth() - 2, 0, 2, getHeight());
            g2.fillRect(0, 0, getWidth(), 2);
            g2.fillRect(0, getHeight() - 2, getWidth(), 2);

            final Color baseEnabledColor = new Color(Integer.parseInt(cp.getColorEnabledButton(), 16));
            final Color baseEnabledHoverColor = new Color(Integer.parseInt(cp.getColorEnabledHoverButton(), 16));

            g2.setColor(
                    new Color(
                            Integer.parseInt(
                                    !isEnabled() ? cp.getColorDisabledButton()
                                            : hover ? SwingUtils
                                                    .getHexRGB(SwingUtils.brighten(baseEnabledHoverColor, 66))
                                                    : SwingUtils.getHexRGB(SwingUtils.brighten(baseEnabledColor, 60)),
                                    16)));
            g2.fillRect(2, 2, getWidth() - 4, 2);
            g2.fillRect(2, 2, 2, getHeight() - 4);

            g2.setColor(
                    new Color(
                            Integer.parseInt(
                                    !isEnabled() ? cp.getColorDisabledButton()
                                            : hover ? SwingUtils
                                                    .getHexRGB(SwingUtils.brighten(baseEnabledHoverColor, -29))
                                                    : SwingUtils.getHexRGB(SwingUtils.brighten(baseEnabledColor, -25)),
                                    16)));
            g2.fillRect(2, getHeight() - 4, getWidth() - 4, 2);
            g2.fillRect(getWidth() - 4, 2, 2, getHeight() - 4);

            g2.setColor(new Color(Integer.parseInt(!isEnabled() ? cp.getColorDisabledButton()
                    : hover ? cp.getColorEnabledHoverButton() : cp.getColorEnabledButton(), 16)));
            g2.fillRect(4, 4, getWidth() - 8, getHeight() - 8);

            final GlyphVector glyph = getFont().createGlyphVector(g2.getFontRenderContext(), getText());
            final Shape bText = glyph.getOutline();
            final float y = (float) ((getHeight() + (bText.getBounds2D().getHeight() * (unicodeFont ? 1 : 2))) / 2);
            final float x = (float) ((getWidth() - (bText.getBounds2D().getWidth())) / 2);

            final Color tx = new Color(Integer.parseInt(cp.getColorText(), 16));
            if (isEnabled()) {
                g2.setColor(SwingUtils.brighten(tx, -190));
                g2.fill(glyph.getOutline(x + 2, y + 2));
            }

            g2.setColor(isEnabled() ? tx : new Color(Integer.parseInt(cp.getDisabledColorText(), 16)));
            g2.fill(glyph.getOutline(x, y));
        } else {
            super.paintComponent(g);
        }
    }

    /**
     * Get current color preferences used to draw this button.
     * 
     * @return color preferences
     */
    public ColorPreferences getCp() {
        return cp;
    }

    /**
     * Set color preferences used to draw this button.
     * 
     * @param cp color preferences
     */
    public void setCp(final ColorPreferences cp) {
        this.cp = cp;
    }

}
