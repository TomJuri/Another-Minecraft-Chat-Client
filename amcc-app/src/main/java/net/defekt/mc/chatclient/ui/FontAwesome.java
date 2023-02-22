package net.defekt.mc.chatclient.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class FontAwesome {

    private static final Map<String, Icon> cache = new HashMap<>();

    public static Font FONT;

    public static final String X = "\u2716";
    public static final String CLEAR = "\uf51a";
    public static final String GEAR = "\u2699";
    public static final String PLUG = "\uf1e6";

    public static Icon createIcon(String character) {
        synchronized (cache) {
            if (!cache.containsKey(character)) {
                Rectangle2D bounds = FONT.getStringBounds(character,
                        new FontRenderContext(FONT.getTransform(), true, true));
                BufferedImage img = new BufferedImage((int) bounds.getWidth(), (int) bounds.getHeight(),
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = img.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setFont(FONT);
                g.setColor(Color.black);
                g.drawString(character, 0, (int) bounds.getHeight() - 2);
                cache.put(character, new ImageIcon(img));
            }
            return cache.get(character);
        }
    }

    static {
        try (InputStream is = FontAwesome.class.getResourceAsStream("/resources/fa-solid-900.ttf")) {
            FONT = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(16f);
        } catch (Exception e) {
            new JLabel().getFont();
            e.printStackTrace();
        }
    }
}
