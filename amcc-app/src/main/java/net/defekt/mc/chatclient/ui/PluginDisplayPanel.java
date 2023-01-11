package net.defekt.mc.chatclient.ui;

import java.awt.Color;
import java.awt.Font;
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
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.defekt.mc.chatclient.api.PluginDescription;
import net.defekt.mc.chatclient.plugins.Plugins;
import net.defekt.mc.chatclient.protocol.data.UserPreferences;
import net.defekt.mc.chatclient.protocol.io.IOUtils;

public class PluginDisplayPanel extends JPanel {

    private static final Icon check;

    static {
        BufferedImage checkI = null;
        try (InputStream in = PluginDisplayPanel.class.getResourceAsStream("/resources/icons/check.png")) {
            checkI = ImageIO.read(in);
            checkI = IOUtils.resizeImageProp(checkI, 16);
        } catch (Exception e) {

        }
        check = new ImageIcon(checkI == null ? new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB) : checkI);
    }

    public PluginDisplayPanel(PluginDescription plugin, boolean remote, Consumer<PluginDescription> downloader) {
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
                deleted.add(id);
                enabled.remove(id);
                initBtnStates(load, del, false, true, true);
            });

            load.addActionListener(e -> {
                boolean enable = !enabled.contains(id);
                boolean halt = false;
                if (enable) {
                    try {
                        Plugins.loadPlugin(plugin);
                        enabled.add(id);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        // TODO handle error
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

            download.addActionListener(e -> {
                if (downloader != null) {
                    download.setEnabled(false);
                    new Thread(() -> {
                        downloader.accept(plugin);
                    }).start();
                }
            });

            ctls.add(download);
        }

        add(title);
        boolean verified = remote || Plugins.isVerified(plugin);

        if (verified) {
            JLabel verificationLabel = new JLabel("Verified!");
            verificationLabel.setForeground(new Color(0, 100, 0));
            verificationLabel.setIcon(check);

            add(verificationLabel);
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
