package net.defekt.mc.chatclient.ui.swing;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.data.ChatMessages;
import net.defekt.mc.chatclient.protocol.data.Messages;
import net.defekt.mc.chatclient.protocol.data.PlayerInfo;
import net.defekt.mc.chatclient.protocol.data.PlayerSkinCache;
import net.defekt.mc.chatclient.protocol.io.IOUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Custom list component used to display players list.<br>
 * By default it uses custom cell renderer - {@link MinecraftPlayerListRenderer}
 *
 * @author Defective4
 * @see MinecraftPlayerListRenderer
 * @see PlayerInfo
 * @see MinecraftClient
 */
public class JMinecraftPlayerList extends JMemList<PlayerInfo> {
    private static final long serialVersionUID = 1L;
    private MinecraftClient mcl = null;

    /**
     * Default constructor
     *
     * @param filterField text field used as player filter
     * @param win         parent window containing this list
     * @param addr        associated server's address
     */
    public JMinecraftPlayerList(final JTextField filterField, final JFrame win, final String addr) {
        setCellRenderer(new MinecraftPlayerListRenderer(filterField, this));
        setBackground(new Color(35, 35, 35));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    setSelectedIndex(locationToIndex(e.getPoint()));
                    final PlayerInfo inf = getSelectedValue();

                    final JPopupMenu jp = new JPopupMenu();
                    JMenuItem placeholder;
                    try {
                        placeholder = new JMenuItem(inf.getName(),
                                                    new ImageIcon(IOUtils.scaleImage(PlayerSkinCache.getHead(inf.getUUID()),
                                                                                     2)));
                    } catch (final Exception e2) {
                        placeholder = new JMenuItem(inf.getName());
                    }
                    placeholder.setEnabled(false);

                    final JMenuItem playerInfo = new JMenuItem(Messages.getString(
                            "JMinecraftPlayerList.playerListOptionPlayerInfo"));
                    playerInfo.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(final ActionEvent ev) {
                            showUserInfo(inf);
                        }
                    });

                    final JMenuItem resetSkin = new JMenuItem(Messages.getString(
                            "JMinecraftPlayerList.playerListOptionResetSkin"));
                    resetSkin.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(final ActionEvent ev) {
                            if (PlayerSkinCache.getSkincache().containsKey(inf.getUUID())) {
                                PlayerSkinCache.getSkincache().remove(inf.getUUID());
                            }
                        }
                    });

                    final JMenuItem resetAllSkins = new JMenuItem(Messages.getString(
                            "JMinecraftPlayerList.playerListOptionClearSkinCache"));
                    resetAllSkins.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(final ActionEvent ev) {

                            PlayerSkinCache.getSkincache().clear();
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    repaint();
                                }
                            });
                        }
                    });

                    final JMenuItem exportList = new JMenuItem(Messages.getString(
                            "JMinecraftPlayerList.playerListOptionExportPlayerList"));
                    exportList.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(final ActionEvent e) {

                            if (mcl == null) return;
                            final String fname = addr.replace(".", "_") + " player list";
                            final JFileChooser jfc = new JFileChooser();
                            jfc.setDialogTitle(Messages.getString("JMinecraftPlayerList.exportDialogTitle"));
                            jfc.setAcceptAllFileFilterUsed(false);
                            jfc.addChoosableFileFilter(new FileNameExtensionFilter(Messages.getString(
                                    "JMinecraftPlayerList.exportDialogFileTypeCSV"), "csv"));
                            jfc.setSelectedFile(new File(fname));
                            final int ret = jfc.showSaveDialog(win);
                            if (ret == JFileChooser.APPROVE_OPTION) {
                                File sel = jfc.getSelectedFile();
                                final String ext = "csv";
                                if (!sel.getName().contains(".") || !sel.getName()
                                                                        .substring(sel.getName().lastIndexOf("."))
                                                                        .equals("." + ext)) {
                                    sel = new File(sel.getPath() + "." + ext);
                                }

                                try {
                                    switch (ext) {
                                        default: {
                                            final PrintWriter pw = new PrintWriter(new FileOutputStream(sel));
                                            pw.println(Messages.getString("JMinecraftPlayerList.exportFileColumns"));
                                            for (final UUID uid : mcl.getPlayersTabList().keySet()) {
                                                final PlayerInfo pinf = mcl.getPlayersTabList().get(uid);
                                                final String name = pinf.getName();
                                                final String dname = pinf.getDisplayName() == null ?
                                                        "N/A" :
                                                        ChatMessages.removeColors(ChatMessages.parse(pinf.getDisplayName()));
                                                final String uuid = pinf.getUUID().toString();
                                                final String ping = pinf.getPing() > 0 ?
                                                        Integer.toString(pinf.getPing()) :
                                                        "?";
                                                final String skurl = PlayerSkinCache.getSkincache()
                                                                                    .containsKey(pinf.getUUID()) ?
                                                        PlayerSkinCache.getSkincache().get(pinf.getUUID()).getUrl() :
                                                        "N/A";

                                                pw.println(name + "; " + dname + "; " + uuid + "; " + ping + "; " + skurl);
                                            }
                                            pw.close();
                                            break;
                                        }
                                    }
                                } catch (final Exception e2) {
                                    SwingUtils.showErrorDialog(win,
                                                               Messages.getString(
                                                                       "JMinecraftPlayerList.exportErrorDialogTitle"),
                                                               e2,
                                                               Messages.getString(
                                                                       "JMinecraftPlayerList.exportErrorDialogMessage") + e2.toString());
                                }
                            }
                        }
                    });

                    jp.add(placeholder);
                    jp.add(playerInfo);
                    jp.add(resetSkin);
                    jp.add(new JSeparator());
                    jp.add(resetAllSkins);
                    jp.add(exportList);

                    jp.show(JMinecraftPlayerList.this, e.getX(), e.getY());
                }
            }
        });

    }

    private void showUserInfo(final PlayerInfo info) {
        final JFrame diag = new JFrame(Messages.getString("JMinecraftPlayerList.userInfoDialogTitle") + info.getName());
        diag.setAlwaysOnTop(true);
        diag.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        final Box box = Box.createVerticalBox();

        final String name = info.getName();
        final String dname = info.getDisplayName() == null ? "N/A" : ChatMessages.parse(info.getDisplayName());
        final int ping = info.getPing();
        final String pingI = Integer.toString(ping);
        final String uuid = info.getUUID().toString();

        box.add(new JLabel(Messages.getString("JMinecraftPlayerList.userInfoDialogName") + name));
        box.add(new JPanel() {
            private static final long serialVersionUID = 1L;

            {
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                add(new JLabel(Messages.getString("JMinecraftPlayerList.userInfoDialogDisplayName")));
                final JTextPane jtp = new JTextPane();
                jtp.setMaximumSize(new Dimension(SwingUtils.sSize.width, 20));
                jtp.setEditable(false);
                jtp.setOpaque(false);
                SwingUtils.appendColoredText(dname.replace("\u00a77", "\u00a78")
                                                  .replace("\u00a7f", "\u00a70")
                                                  .replace("\u00a7r", "\u00a70"), jtp);
                add(jtp);
            }
        });
        box.add(new JPanel() {
            private static final long serialVersionUID = 1L;

            {
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                add(new JLabel(Messages.getString("JMinecraftPlayerList.userInfoDialogPing")));
                BufferedImage bb = MinecraftPlayerListRenderer.bar5;
                Color c = Color.green;

                if (ping >= 1000) {
                    bb = MinecraftPlayerListRenderer.bar1;
                    c = new Color(255, 150, 0);
                } else if (ping >= 600) {
                    bb = MinecraftPlayerListRenderer.bar2;
                    c = new Color(200, 150, 0);
                } else if (ping >= 300) {
                    bb = MinecraftPlayerListRenderer.bar3;
                    c = new Color(150, 150, 0);
                } else if (ping >= 150) {
                    bb = MinecraftPlayerListRenderer.bar4;
                    c = new Color(0, 150, 0);
                } else if (ping > 0) {
                    bb = MinecraftPlayerListRenderer.bar5;
                    c = Color.green;
                }

                final Color fc = c;
                final BufferedImage bbf = bb;
                add(new JPanel() {
                    private static final long serialVersionUID = 1L;

                    {
                        setMaximumSize(new Dimension(16, 16));
                    }

                    @Override
                    public void paintComponent(final Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(bbf, 0, 0, 16, 16, null);
                    }
                });
                add(new JLabel(" " + pingI + "ms") {
                    private static final long serialVersionUID = 1L;

                    {
                        setForeground(fc);
                    }
                });

            }
        });
        box.add(new JPanel() {
            private static final long serialVersionUID = 1L;

            {
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                add(new JLabel(Messages.getString("JMinecraftPlayerList.userInfoDialogUUID")));
                final JTextPane jtp = new JTextPane();
                jtp.setMaximumSize(new Dimension(SwingUtils.sSize.width, 20));
                jtp.setEditable(false);
                jtp.setOpaque(false);
                jtp.setText(uuid);
                add(jtp);
            }
        });

        box.add(new JButton(Messages.getString("JMinecraftPlayerList.userInfoDialogViewSkin")) {
            private static final long serialVersionUID = 1L;

            {
                addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        diag.dispose();
                        final BufferedImage skin = PlayerSkinCache.getSkincache().get(info.getUUID()).getImg();
                        final BufferedImage p1 = IOUtils.renderPlayerSkin(skin, 0);
                        final BufferedImage p2 = IOUtils.renderPlayerSkin(skin, 1);

                        final BufferedImage[] igs = new BufferedImage[]{p1, p2, skin};

                        final int[] inRef = new int[]{0};

                        final JFrame win = new JFrame(Messages.getString("JMinecraftPlayerList.userSkinDialogTitle") + info.getName());
                        win.setAlwaysOnTop(true);
                        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                        final Box box = Box.createVerticalBox();
                        final JPanel jp = new JPanel() {
                            private static final long serialVersionUID = 1L;

                            {
                                setPreferredSize(new Dimension((SwingUtils.sSize.height / 2) * (skin.getWidth() / skin.getHeight()),
                                                               SwingUtils.sSize.height / 2));
                            }

                            @Override
                            public void paintComponent(final Graphics g) {
                                super.paintComponent(g);
                                g.drawImage(IOUtils.resizeImageProp(igs[inRef[0]], getHeight()), 0, 0, null);
                            }
                        };
                        box.add(jp);

                        box.add(new JPanel() {
                            private static final long serialVersionUID = 1L;

                            {
                                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                                add(new JButton("<") {
                                    {
                                        addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(final ActionEvent ev) {
                                                inRef[0]--;
                                                if (inRef[0] < 0) {
                                                    inRef[0] = igs.length - 1;
                                                }
                                                SwingUtilities.invokeLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        jp.repaint();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                                add(new JLabel(Messages.getString("JMinecraftPlayerList.userSkinDialogChangeView")));
                                add(new JButton(">") {
                                    {
                                        addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(final ActionEvent ev) {
                                                inRef[0]++;
                                                if (inRef[0] >= igs.length) {
                                                    inRef[0] = 0;
                                                }
                                                SwingUtilities.invokeLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        jp.repaint();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });

                        win.setContentPane(box);
                        win.pack();
                        SwingUtils.centerWindow(win);
                        win.setVisible(true);
                    }
                });
            }
        });

        for (final Component ct : box.getComponents())
            if (ct instanceof JComponent) {
                ((JComponent) ct).setAlignmentX(Component.LEFT_ALIGNMENT);
            }

        diag.setContentPane(box);
        diag.pack();
        SwingUtils.centerWindow(diag);
        diag.setResizable(false);
        diag.setVisible(true);
    }

    /**
     * Get Minecraft client instance
     *
     * @return Minecraft client instance
     */
    public MinecraftClient getMcl() {
        return mcl;
    }

    /**
     * Set current Minecraft client instance
     *
     * @param mcl Minecraft client instance
     */
    public void setMcl(final MinecraftClient mcl) {
        this.mcl = mcl;
    }
}
