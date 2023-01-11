package net.defekt.mc.chatclient.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

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

    public PluginDisplayPanel(PluginDescription plugin) {
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
        JButton load = new JButton();
        JButton del = new JButton("Delete");

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

        add(title);
        boolean verified = Plugins.isVerified(plugin);

        if (verified) {
            JLabel verificationLabel = new JLabel("Verified!");
            verificationLabel.setForeground(new Color(0, 100, 0));
            verificationLabel.setIcon(check);

            add(verificationLabel);
        }
        add(new JLabel(" "));
        for (String desc : plugin.getDescription())
            add(new JLabel(desc));

        Box ctls = Box.createHorizontalBox();
        ctls.add(load);
        ctls.add(del);
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
