package net.defekt.mc.chatclient.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.defekt.mc.chatclient.api.PluginDescription;
import net.defekt.mc.chatclient.plugins.Plugins;
import net.defekt.mc.chatclient.protocol.data.UserPreferences;
import net.defekt.mc.chatclient.protocol.io.IOUtils;
import net.defekt.mc.chatclient.ui.swing.SwingUtils;

public class PluginDisplayPanel extends JPanel {

    private static final Icon check;
    private static final Icon exc;
    private static final Icon star;

    static {
        BufferedImage checkI = null;
        BufferedImage excI = null;
        BufferedImage starI = null;
        try (InputStream in = PluginDisplayPanel.class.getResourceAsStream("/resources/icons/check.png")) {
            checkI = ImageIO.read(in);
            checkI = IOUtils.resizeImageProp(checkI, 16);
        } catch (Exception e) {
        }
        try (InputStream in = PluginDisplayPanel.class.getResourceAsStream("/resources/icons/x.png")) {
            excI = ImageIO.read(in);
            excI = IOUtils.resizeImageProp(excI, 16);
        } catch (Exception e) {
        }
        try (InputStream in = PluginDisplayPanel.class.getResourceAsStream("/resources/icons/star.png")) {
            starI = ImageIO.read(in);
            starI = IOUtils.resizeImageProp(starI, 14);
        } catch (Exception e) {
        }
        check = new ImageIcon(checkI == null ? new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB) : checkI);
        exc = new ImageIcon(excI == null ? new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB) : excI);
        star = new ImageIcon(starI == null ? new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB) : starI);
    }

    public PluginDisplayPanel(PluginDescription plugin, boolean remote, Consumer<PluginDescription> downloader,
            Window parent) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Box title = Box.createHorizontalBox();
        JLabel nameLabel = new JLabel(plugin.getName() + " (v" + plugin.getVersion() + ")");
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD).deriveFont(13f));

        JLabel authorLabel = new JLabel("by " + plugin.getAuthor());
        nameLabel.setFont(nameLabel.getFont().deriveFont(12f));

        title.add(nameLabel);
        title.add(new JLabel(" "));
        title.add(authorLabel);
        title.setAlignmentX(LEFT_ALIGNMENT);

        Box ctls = Box.createHorizontalBox();
        JButton load = new JButton();
        JButton del = new JButton("Delete");

        if (!remote) {
            UserPreferences prefs = Main.up;
            List<String> enabled = prefs.getEnabledPlugins();
            List<String> halted = prefs.getHaltedPlugins();
            List<String> deleted = prefs.getDeletedPlugins();
            String id = plugin.getUID();

            initBtnStates(load, del, enabled.contains(id), halted.contains(id), deleted.contains(id));

            del.addActionListener(e -> {
                SwingUtils.playAsterisk();
                int resp = JOptionPane.showOptionDialog(parent,
                        "Are you sure you want to delete plugin " + plugin.getName() + "?\n"
                                + "If you press \"Yes\" the plugin will be deleted on the next startup.\n"
                                + "This action is irreversible.",
                        "title", JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        new String[] { "Yes", "No" }, 0);
                if (resp == 1) return;
                deleted.add(id);
                enabled.remove(id);
                initBtnStates(load, del, false, true, true);
            });

            load.addActionListener(e -> {
                boolean enable = !enabled.contains(id);
                boolean halt = false;
                if (enable) {

                    if (!plugin.getApi().equals(Main.VERSION)) {
                        SwingUtils.playAsterisk();
                        int resp = JOptionPane.showOptionDialog(parent,
                                "You are trying to load a plugin written\n" + "for an older version of AMCC.\n"
                                        + "Unexpected behavior may happen.\n\n" + "Do you want to continue?",
                                "title", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
                                new String[] { "Yes", "No" }, 0);
                        if (resp == 1) return;
                    }

                    if (!Plugins.isVerified(plugin)) {
                        SwingUtils.playExclamation();
                        int resp = JOptionPane.showOptionDialog(parent, "You are trying to load an unverified plugin.\n"
                                + "Plugins can access all data sent between client and sever,\n"
                                + "as well as any information outside of AMCC (such as private files and documents)\n"
                                + "Please make sure to download plugins only from trusted sources.\n\n"
                                + "Do you wish to continue?", "title", JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE, null,
                                new String[] { "Yes, I understand the risk", "Take me back" }, 0);
                        if (resp == 1) return;
                    }

                    try {
                        Plugins.loadPlugin(plugin);
                        enabled.add(id);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        SwingUtils.showErrorDialog(parent, "Error", e2, "There was an error loading the plugin!");
                        return;
                    }
                } else {
                    enabled.remove(id);
                    halted.add(id);
                    halt = true;
                }
                initBtnStates(load, del, enable, halt, false);
            });
            ctls.add(load);
            ctls.add(del);
        } else {
            JButton download = new JButton("Download");
            JButton starBtn = new JButton("0", star);
            boolean isInstalled = false;
            boolean hasUpdates = false;

            for (PluginDescription localPlugin : Plugins.listPlugins()) {
                if ((localPlugin.getMain().equals(plugin.getMain())) || (localPlugin.getName().equals(plugin.getName())
                        && localPlugin.getAuthor().equals(plugin.getName()))) {
                    isInstalled = true;
                }

                if (isInstalled) {
                    if (!localPlugin.getVersion().equals(plugin.getVersion())
                            || !localPlugin.getApi().equals(plugin.getApi()))
                        hasUpdates = true;
                    break;
                }
            }

            download.setEnabled(!(isInstalled ^ hasUpdates));

            if (hasUpdates) {
                download.setText("Update");
            }

            boolean localHasUpdates = hasUpdates;

            starBtn.addActionListener(e -> {
                starBtn.setEnabled(false);

                try {
                    int i = Integer.parseInt(starBtn.getText());
                    starBtn.setText(Integer.toString(i + 1));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            });

            download.addActionListener(e -> {

                if (localHasUpdates) {
                    SwingUtils.playAsterisk();
                    JOptionPane.showOptionDialog(parent,
                            "Sorry, but automatic updates are not yet supported.\n"
                                    + "Please delete the plugin manually and then try to download.",
                            "Sorry", JOptionPane.CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                            new String[] { "Cancel" }, 0);
                } else if (downloader != null) {
                    download.setEnabled(false);
                    new Thread(() -> {
                        downloader.accept(plugin);
                    }).start();
                }
            });

            ctls.add(download);
            ctls.add(starBtn);
        }

        add(title);
        boolean verified = remote || Plugins.isVerified(plugin);

        if (verified) {
            JLabel verificationLabel = new JLabel("Verified!");
            verificationLabel.setForeground(new Color(0, 100, 0));
            verificationLabel.setIcon(check);

            add(verificationLabel);
        }

        if (!plugin.getApi().equals(Main.VERSION)) {
            JLabel deprecationLabel = new JLabel(
                    "Incompatible version! This plugin is made for AMCC v" + plugin.getApi());
            deprecationLabel.setForeground(new Color(100, 0, 0));
            deprecationLabel.setIcon(exc);

            add(deprecationLabel);
        }

        add(new JLabel(" "));
        for (String desc : plugin.getDescription())
            add(new JLabel(desc));

        ctls.setAlignmentX(LEFT_ALIGNMENT);
        add(ctls);
        add(new JLabel(" "));
        add(new JSeparator(JSeparator.HORIZONTAL));
    }

    private static void initBtnStates(JButton load, JButton delete, boolean enabled, boolean halted, boolean deleted) {
        boolean restart = halted || deleted;
        load.setText(restart ? "Restart required..." : enabled ? "Disable" : "Enable");
        load.setEnabled(!restart);
        delete.setEnabled(!deleted);
    }
}
