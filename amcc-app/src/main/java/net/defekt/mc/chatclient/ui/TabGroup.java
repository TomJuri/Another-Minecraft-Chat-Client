package net.defekt.mc.chatclient.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TabGroup extends JPanel {
    public TabGroup(final String icon, final String text) {
        this(icon, text, FontAwesome.FONT);
    }

    private final String text;

    public TabGroup(final String icon, final String text, final Font font) {
        this(icon, text, font, false);
    }

    public TabGroup(final String icon, final String text, final boolean displayText) {
        this(icon, text, FontAwesome.FONT, displayText);
    }

    public TabGroup(final String icon, final String text, final Font font, final boolean displayText) {
        this.text = text;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        final JLabel iconL = new JLabel(icon);
        iconL.setFont(font);

        add(iconL);
        if (displayText) add(new JLabel(" " + text));
        setBackground(new Color(0, 0, 0, 0));
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }

    public String getText() {
        return text;
    }
}
