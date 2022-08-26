package net.defekt.mc.chatclient.ui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import net.defekt.mc.chatclient.protocol.data.ChatColor;
import net.defekt.mc.chatclient.protocol.data.StatusInfo;
import net.defekt.mc.chatclient.ui.Main;
import net.defekt.mc.chatclient.ui.Messages;
import net.defekt.mc.chatclient.ui.ServerEntry;

/**
 * Custom cell renderer used in {@link JMinecraftServerList}.<br>
 * It is used to render contained servers and show information about them,
 * including motd, server name, player count, etc.
 * 
 * @see JMinecraftServerList
 * @see ServerEntry
 * 
 * @author Defective4
 *
 */
public class MinecraftServerListRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(final JList<? extends Object> list, final Object value,
            final int index, final boolean isSelected, final boolean cellHasFocus) {

        final ServerEntry entry = (ServerEntry) value;
        final Box serverBox = Box.createVerticalBox();
        serverBox.setMinimumSize(new Dimension(list.getMinimumSize().width, 100));

        final JLabel name = new JLabel(" " + entry.getName());
        final JTextPane version = new JTextPane();
        version.setText("???");
        final JLabel players = new JLabel(" " + Messages.getString("MinecraftServerListRenderer.serverListPlayersLabel")
                + entry.getVersion() + ")");
        final JTextPane description = new JTextPane();
        description.setText(" " + Messages.getString("MinecraftServerListRenderer.serverListStatusPinging"));

        serverBox.add(name);
        serverBox.add(players);
        serverBox.add(version);
        serverBox.add(description);
        serverBox.add(new JLabel(" "));

        for (final Component ct : serverBox.getComponents()) {
            ct.setFont(Main.mcFont);
            ct.setForeground(Color.white);
            if (ct instanceof JTextPane) {
                final JTextPane jtp = (JTextPane) ct;
                jtp.setForeground(ChatColor.translateColorCode("7"));
                jtp.setOpaque(false);
                jtp.setEditable(false);
                jtp.setAlignmentX(Component.LEFT_ALIGNMENT);
            }
        }

        version.setForeground(ChatColor.translateColorCode("7"));
        players.setForeground(ChatColor.translateColorCode("7"));
        serverBox.setOpaque(isSelected);
        serverBox.setBackground(isSelected ? new Color(0, 0, 0, 155) : new Color(0, 0, 0, 0));

        BufferedImage icon = null;

        if (entry.getInfo() != null) {
            final StatusInfo inf = entry.getInfo();
            if (inf.getOnlinePlayers() != -1) {
                players.setText(
                        " " + Integer.toString(inf.getOnlinePlayers()) + "/" + Integer.toString(inf.getMaxPlayers())
                                + " " + Messages.getString("MinecraftServerListRenderer.serverListPlayersLabel2")
                                + entry.getVersion() + ")"
                                + (inf.getModType() != null
                                        ? " (" + Messages.getString("MinecraftServerListRenderer.modded") + ")"
                                        : ""));
            }
            description.setText("");
            SwingUtils.appendColoredText(" " + inf.getDescription().replace("\n", "\n "), description);
            version.setText(inf.getVersionName().isEmpty() ? " ???" : "");
            SwingUtils.appendColoredText(" " + inf.getVersionName(), version);

        }
        if (entry.getIcon() != null) {
            final String ibase = entry.getIcon();
            try {
                icon = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(ibase.getBytes())));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                list.repaint();
            }
        });

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                serverBox.repaint();
                serverBox.revalidate();
            }
        });

        if (list instanceof JMinecraftServerList) {
            final JMinecraftServerList jmc = (JMinecraftServerList) list;
            if (jmc.getSelectedIndex() == -1) {
                jmc.setListData(jmc.getListData());
            }
        }

        final BufferedImage icon2 = icon;

        final Box bBox = Box.createHorizontalBox();
        bBox.add(new JPanel() {
            {
                setPreferredSize(new Dimension(72, 68));
            }

            @Override
            public void paintComponent(final Graphics g) {
                if (icon2 != null) {
                    g.drawImage(icon2, 4, 4, 64, 64, null);
                }
            }
        });
        bBox.add(serverBox);
        return bBox;
    }

}
