package net.defekt.mc.chatclient.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class FontAwesome {

    public static final Font FONT;
    public static final Font BRANDS_FONT;

    public static final String X = "\u2716";
    public static final String CLEAR = "\uf51a";
    public static final String GEAR = "\u2699";
    public static final String PLUG = "\uf1e6";
    public static final String PALETTE = "\uf53f";
    public static final String DOWNLOAD = "\uf019";
    public static final String BOX = "\uf49e";
    public static final String CLOSED_BOX = "\uf466";
    public static final String USER = "\uf007";
    public static final String USERS = "\uf0c0";
    public static final String USER_C = "\uf2bd";
    public static final String WRENCH = "\uf0ad";
    public static final String NETWORK = "\uf6ff";
    public static final String LOCK = "\uf023";
    public static final String KEY = "\uf084";
    public static final String FOLDER = "\uf07b";
    public static final String CLOCK = "\uf017";
    public static final String GLOBE = "\uf0ac";
    public static final String STOPWATCH = "\uf2f2";
    public static final String VIAL = "\uf492";

    // Brands

    public static final String DISCORD = "\uf392";

    public static Icon createIcon(String character, Component parent, Font overrideFont) {
        return createIcon(character, parent, FONT.getSize2D(), overrideFont);
    }

    public static Icon createIcon(String character, Component parent, float size) {
        return createIcon(character, parent, size, FONT);
    }

    public static Icon createIcon(String character, Component parent) {
        return createIcon(character, parent, FONT);
    }

    public static Icon createIcon(String character, Component parent, float fontSize, Font font) {
        Font overrideFont = font.deriveFont(fontSize);
        Rectangle2D bounds = overrideFont.getStringBounds(character,
                new FontRenderContext(overrideFont.getTransform(), true, true));
        BufferedImage img = new BufferedImage((int) bounds.getWidth() + 4, (int) bounds.getHeight() + 4,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(overrideFont);
        g.setColor(parent == null ? Color.black : parent.getForeground());
        g.drawString(character, 2, (int) bounds.getHeight());
        return new ImageIcon(img);
    }

    static {
        Font f;
        try (InputStream is = FontAwesome.class.getResourceAsStream("/resources/fa-solid-900.ttf")) {
            f = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(16f);
        } catch (Exception e) {
            f = new JLabel().getFont();
            e.printStackTrace();
        }
        FONT = f;
        try (InputStream is = FontAwesome.class.getResourceAsStream("/resources/fa-brands-400.ttf")) {
            f = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(16f);
        } catch (Exception e) {
            f = new JLabel().getFont();
            e.printStackTrace();
        }
        BRANDS_FONT = f;
    }
}
