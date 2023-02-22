package net.defekt.mc.chatclient.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.defekt.mc.chatclient.api.AMCPlugin;
import net.defekt.mc.chatclient.api.PluginDescription;
import net.defekt.mc.chatclient.api.ui.GUIComponents;
import net.defekt.mc.chatclient.plugins.Plugins;
import net.defekt.mc.chatclient.protocol.data.Messages;
import net.defekt.mc.chatclient.protocol.data.UserPreferences;
import net.defekt.mc.chatclient.protocol.io.IOUtils;
import net.defekt.mc.chatclient.ui.swing.JLinkLabel;
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

    public PluginDisplayPanel(GUIComponents cpts, PluginDescription plugin, boolean remote,
            Consumer<PluginDescription> downloader, Window parent) {
        this(cpts, plugin, remote, downloader, parent, 0, false);
    }

    public PluginDisplayPanel(GUIComponents cpts, PluginDescription plugin, boolean remote,
            Consumer<PluginDescription> downloader, Window parent, int stars, boolean starred) {
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
        JButton del = new JButton(Messages.getString("Main.delete"));

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
                        String.format(Messages.getString("PluginManager.deleteWarning"), plugin.getName()),
                        Messages.getString("PluginManager.deleteWarningTitle"), JOptionPane.CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        new String[] { Messages.getString("Main.yes"), Messages.getString("Main.no") }, 0);
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
                                Messages.getString("PluginManager.loadIncompatibleWarning"),
                                Messages.getString("Main.inventoryHandlingHelpTitle"), JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE, null,
                                new String[] { Messages.getString("Main.yes"), Messages.getString("Main.no") }, 0);
                        if (resp == 1) return;
                    }

                    boolean trusted = prefs.getTrustedAuthors().contains(plugin.getAuthor());
                    int flag = Plugins.getPluginFlag(plugin);
                    boolean verified = flag == Plugins.PLUGIN_VERIFIED;
                    boolean malicious = flag == Plugins.PLUGIN_MALICIOUS;

                    if (malicious) {
                        SwingUtils.playExclamation();
                        JOptionPane.showOptionDialog(parent, Messages.getString("PluginManager.loadMaliciousError"),
                                Messages.getString("PluginManager.maliciousTitle"), JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.ERROR_MESSAGE, null,
                                new Object[] { new JButton(Messages.getString("PluginManager.unverifiedYes")) {
                                    {
                                        setEnabled(false);
                                    }
                                }, Messages.getString("PluginManager.unverifiedNo") }, 0);
                        return;

                    } else if (!verified && !trusted) {
                        SwingUtils.playExclamation();
                        JCheckBox trustBox = new JCheckBox(Messages.getString("PluginManager.trustAuthorCheck"));
                        int resp = JOptionPane.showOptionDialog(parent,
                                new Object[] { Messages.getString("PluginManager.loadUnverifiedWarning"), trustBox },
                                Messages.getString("Main.inventoryHandlingHelpTitle"), JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE, null,
                                new String[] { Messages.getString("PluginManager.unverifiedYes"),
                                        Messages.getString("PluginManager.unverifiedNo") },
                                0);
                        if (trustBox.isSelected()) prefs.getTrustedAuthors().add(plugin.getAuthor());
                        if (resp == 1) return;
                    }

                    try {
                        AMCPlugin pl = Plugins.loadPlugin(plugin);
                        pl.onGUIInitialized(cpts);
                        cpts.revalidateAll();
                        enabled.add(id);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        SwingUtils.showErrorDialog(parent, Messages.getString("ServerDetailsDialog.error"), e2,
                                Messages.getString("PluginManager.errorLoading"));
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
            JButton download = new JButton(Messages.getString("PluginManager.btnDownload"));
            JButton starBtn = new JButton(Integer.toString(stars), star);
            starBtn.setEnabled(!starred);

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
                download.setText(Messages.getString("PluginManager.btnUpdate"));
            }

            boolean localHasUpdates = hasUpdates;
            try {
                String b = URLEncoder.encode(plugin.getName(), "utf-8");
                starBtn.addActionListener(e -> {
                    starBtn.setEnabled(false);

                    new Thread(() -> {
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                                new URL(Plugins.pluginStarsURL + "?id=" + Main.up.getUserID() + "&p=" + b)
                                        .openStream()))) {

                            String result = br.readLine();
                            br.close();
                            if (!result.equalsIgnoreCase("ok")) throw new IOException("Invalid response");
                            int i = Integer.parseInt(starBtn.getText());
                            starBtn.setText(Integer.toString(i + 1));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            starBtn.setEnabled(true);
                        }
                    }).start();
                });
            } catch (UnsupportedEncodingException e1) {
                starBtn.setEnabled(false);
            }

            download.addActionListener(e -> {

                if (localHasUpdates) {
                    SwingUtils.playAsterisk();
                    JOptionPane.showOptionDialog(parent, Messages.getString("PluginManager.updateWarning"),
                            Messages.getString("Main.cancel"), JOptionPane.CANCEL_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, new String[] { "Cancel" }, 0);
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
        int flag = remote ? Plugins.PLUGIN_VERIFIED : Plugins.getPluginFlag(plugin);
        boolean verified = flag == Plugins.PLUGIN_VERIFIED;
        boolean malicious = flag == Plugins.PLUGIN_MALICIOUS;
        boolean trusted = !remote && Main.up.getTrustedAuthors().contains(plugin.getAuthor());

        String website = plugin.getWebsiteString();
        if (website != null) {
            JLabel linkLabel = new JLinkLabel(website);

            add(linkLabel);
        }

        if (malicious) {
            verified = false;
            trusted = false;
            JLabel verificationLabel = new JLabel(Messages.getString("PluginManager.malicious"));
            verificationLabel.setIcon(exc);

            add(verificationLabel);
        } else if (verified) {
            JLabel verificationLabel = new JLabel(Messages.getString("PluginManager.verified"));
            verificationLabel.setIcon(check);

            add(verificationLabel);
        } else if (trusted) {
            JLabel verificationLabel = new JLabel(Messages.getString("PluginManager.trusted"));
//            verificationLabel.setIcon(check);

            add(verificationLabel);
        }

        if (!plugin.getApi().equals(Main.VERSION)) {
            JLabel deprecationLabel = new JLabel(Messages.getString("PluginManager.incompatible") + plugin.getApi());
            deprecationLabel.setForeground(new Color(100, 0, 0));
            deprecationLabel.setIcon(exc);

            add(deprecationLabel);
        }

        add(new JLabel(" "));
        if (plugin.getDescription() != null) for (String desc : plugin.getDescription())
            add(new JLabel(desc));

        ctls.setAlignmentX(LEFT_ALIGNMENT);
        add(ctls);
        add(new JLabel(" "));
        add(new JSeparator(JSeparator.HORIZONTAL));
    }

    private static void initBtnStates(JButton load, JButton delete, boolean enabled, boolean halted, boolean deleted) {
        boolean restart = halted || deleted;
        load.setText(restart ? Messages.getString("PluginManager.restart")
                : enabled ? Messages.getString("PluginManager.disable") : Messages.getString("PluginManager.enable"));
        load.setEnabled(!restart);
        delete.setEnabled(!deleted);
    }
}
