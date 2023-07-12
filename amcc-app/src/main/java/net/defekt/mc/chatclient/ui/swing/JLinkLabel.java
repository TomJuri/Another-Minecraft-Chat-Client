package net.defekt.mc.chatclient.ui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class JLinkLabel extends JLabel {

    private final String link;

    public JLinkLabel(final String link) {
        this.link = link;
        setText("<html><a href=\"" + link + "\">" + link + "</a></html>");

        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URL(link).toURI());
                } catch (final Exception e2) {
                    e2.printStackTrace();
                }
            }

        });
    }

    public String getLink() {
        return link;
    }

}
