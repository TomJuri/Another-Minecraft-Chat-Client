package net.defekt.mc.chatclient.ui.swing;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.JLabel;

public class JLinkLabel extends JLabel {

    private final String link;

    public JLinkLabel(String link) {
        this.link = link;
        setText("<html><a href=\"" + link + "\">" + link + "</a></html>");

        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URL(link).toURI());
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

        });
    }

    public String getLink() {
        return link;
    }

}
