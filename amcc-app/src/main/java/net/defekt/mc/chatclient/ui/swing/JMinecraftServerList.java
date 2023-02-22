package net.defekt.mc.chatclient.ui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.defekt.mc.chatclient.protocol.data.Messages;
import net.defekt.mc.chatclient.protocol.data.ServerEntry;
import net.defekt.mc.chatclient.ui.Main;

/**
 * Minecraft-like server list<br>
 * It shows informations about all servers it contains (their names, motd,
 * version, players and even an icon).<br>
 * by default it uses custom cell renderer -
 * {@link MinecraftServerListRenderer}.
 * 
 * @see ServerEntry
 * @see MinecraftServerListRenderer
 * @author Defective4
 *
 */
public class JMinecraftServerList extends JMemList<ServerEntry> {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     * 
     * @param main      instance of main application class
     * @param popupMenu if pop-up menu with options should be accessible for this
     *                  list
     */
    public JMinecraftServerList(final Main main, final boolean popupMenu) {
        final Random rand = new Random();
        for (int x = 0; x < bytemap.length; x++) {
            for (int y = 0; y < bytemap[x].length; y++) {
                bytemap[x][y] = (byte) rand.nextInt(3);
            }
        }

        setOpaque(true);
        setBackground(new Color(0, 0, 0, 0));
        setCellRenderer(new MinecraftServerListRenderer());
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (popupMenu) {
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        setSelectedIndex(locationToIndex(e.getPoint()));
                        final ServerEntry et = getSelectedValue();
                        final int selIndex = getSelectedIndex();

                        if (et != null) {
                            final JPopupMenu pm = new JPopupMenu();

                            final JMenuItem conItem = new JMenuItem(Messages.getString("Main.connectServerOption")) {
                                {
                                    addActionListener(main.getConnectionACL());
                                    setFont(getFont().deriveFont(Font.BOLD));
                                }
                            };

                            final JMenuItem mupItem = new JMenuItem(
                                    Messages.getString("JMinecraftServerList.serverUpLabel")) {
                                {
                                    addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(final ActionEvent e) {
                                            main.moveServer(selIndex, 0);
                                        }
                                    });
                                }
                            };

                            final JMenuItem mdownItem = new JMenuItem(
                                    Messages.getString("JMinecraftServerList.serverDownLabel")) {
                                {
                                    addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(final ActionEvent e) {
                                            main.moveServer(selIndex, 1);
                                        }
                                    });
                                }
                            };

                            final JMenuItem queryItem = new JMenuItem(
                                    Messages.getString("JMinecraftServerList.detailsLabel")) {
                                {
                                    addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(final ActionEvent e) {
                                            final ServerDetailsDialog dialog = new ServerDetailsDialog(
                                                    main.getMainWindow(), et);
                                            dialog.setVisible(true);
                                        }
                                    });
                                }
                            };

                            final JMenuItem saveIconItem = new JMenuItem(
                                    Messages.getString("JMinecraftServerList.saveIconLabel")) {
                                {
                                    addActionListener(new ActionListener() {

                                        @Override
                                        public void actionPerformed(final ActionEvent e) {
                                            if (et.getIcon() != null) {
                                                try {
                                                    final byte[] image = Base64.getDecoder()
                                                            .decode(et.getIcon().getBytes());

                                                    final JFileChooser fc = new JFileChooser();
                                                    fc.setDialogTitle(Messages.getString("Main.save"));
                                                    fc.setApproveButtonText(Messages.getString("Main.save"));
                                                    fc.setAcceptAllFileFilterUsed(false);
                                                    fc.setFileFilter(new FileNameExtensionFilter("PNG File", ".png"));
                                                    fc.setSelectedFile(new File("icon.png"));

                                                    final int op = fc.showSaveDialog(main.getMainWindow());
                                                    if (op == JFileChooser.APPROVE_OPTION) {
                                                        final File out = fc.getSelectedFile();
                                                        try (OutputStream os = new FileOutputStream(out)) {
                                                            os.write(image);
                                                            os.close();
                                                            SwingUtils.playAsterisk();
                                                            JOptionPane.showOptionDialog(main.getMainWindow(),
                                                                    Messages.getString(
                                                                            "JMinecraftServerList.exportIconSuccess"),
                                                                    "...", JOptionPane.CANCEL_OPTION,
                                                                    JOptionPane.INFORMATION_MESSAGE, null,
                                                                    new String[] { Messages.getString("Main.ok") }, 0);
                                                        } catch (final Exception ex) {
                                                            ex.printStackTrace();
                                                            SwingUtils.showErrorDialog(main.getMainWindow(),
                                                                    Messages.getString(
                                                                            "JMinecraftPlayerList.exportErrorDialogTitle"),
                                                                    ex, Messages.getString(
                                                                            "JMinecraftServerList.saveIconErrorMessage"));
                                                        }
                                                    }

                                                } catch (final Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                                }
                            };

                            if (et.getIcon() == null) {
                                saveIconItem.setEnabled(false);
                            }

                            mupItem.setEnabled(selIndex > 0);
                            mdownItem.setEnabled(selIndex < getListData().length - 1);

                            pm.add(conItem);
                            pm.add(mupItem);
                            pm.add(mdownItem);
                            pm.add(queryItem);
                            pm.add(saveIconItem);
                            pm.show(JMinecraftServerList.this, e.getX(), e.getY());
                        }
                    }
                }
            });
        }
    }

    private static final Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final byte[][] bytemap = new byte[(int) (sSize.getWidth() / 16)][(int) (sSize.getHeight() / 16)];

    @Override
    public void paintComponent(final Graphics g) {
//		for (int x = 0; x <= getWidth() / 64; x++)
//			for (int y = 0; y <= getHeight() / 64; y++)
//				g.drawImage(Main.bgImage, x * 64, y * 64, 64, 64, null);
        g.setColor(new Color(60, 47, 74));
        g.fillRect(0, 0, getWidth(), getHeight());
        for (int x = 0; x < getWidth() / 16; x++) {
            for (int y = 0; y < getHeight() / 16; y++) {
                final int mod = bytemap[x % bytemap.length][y % bytemap[x].length] * 10;
                g.setColor(new Color(60 - mod, 47 - mod, 74 - mod));
                g.fillRect(x * 16, y * 16, 16, 16);
            }
        }

        super.paintComponent(g);
    }
}
