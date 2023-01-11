package net.defekt.mc.chatclient.ui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuComponent;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.DefaultTableModel;

import net.defekt.mc.chatclient.api.PluginDescription;
import net.defekt.mc.chatclient.integrations.discord.DiscordPresence;
import net.defekt.mc.chatclient.plugins.Plugins;
import net.defekt.mc.chatclient.protocol.AuthType;
import net.defekt.mc.chatclient.protocol.InternalPacketListener;
import net.defekt.mc.chatclient.protocol.LANListener;
import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.MinecraftStat;
import net.defekt.mc.chatclient.protocol.ProtocolNumber;
import net.defekt.mc.chatclient.protocol.data.AutoResponseRule;
import net.defekt.mc.chatclient.protocol.data.AutoResponseRule.EffectType;
import net.defekt.mc.chatclient.protocol.data.AutoResponseRule.TriggerType;
import net.defekt.mc.chatclient.protocol.data.ChatMessages;
import net.defekt.mc.chatclient.protocol.data.ForgeMode;
import net.defekt.mc.chatclient.protocol.data.ItemsWindow;
import net.defekt.mc.chatclient.protocol.data.Messages;
import net.defekt.mc.chatclient.protocol.data.PlayerInfo;
import net.defekt.mc.chatclient.protocol.data.PlayerSkinCache;
import net.defekt.mc.chatclient.protocol.data.ProxySetting;
import net.defekt.mc.chatclient.protocol.data.ServerEntry;
import net.defekt.mc.chatclient.protocol.data.TranslationUtils;
import net.defekt.mc.chatclient.protocol.data.UserPreferences;
import net.defekt.mc.chatclient.protocol.data.UserPreferences.ColorPreferences;
import net.defekt.mc.chatclient.protocol.data.UserPreferences.Constants;
import net.defekt.mc.chatclient.protocol.data.UserPreferences.Language;
import net.defekt.mc.chatclient.protocol.data.UserPreferences.SkinRule;
import net.defekt.mc.chatclient.protocol.entity.Entity;
import net.defekt.mc.chatclient.protocol.entity.Player;
import net.defekt.mc.chatclient.protocol.event.ClientListener;
import net.defekt.mc.chatclient.protocol.io.IOUtils;
import net.defekt.mc.chatclient.protocol.io.ListenerHashMap.MapChangeListener;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketFactory;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.UnknownPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket.Position;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientResourcePackStatusPacket.Status;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientUseEntityPacket.UseType;
import net.defekt.mc.chatclient.ui.swing.JAutoResponseList;
import net.defekt.mc.chatclient.ui.swing.JColorChooserButton;
import net.defekt.mc.chatclient.ui.swing.JColorChooserButton.ColorChangeListener;
import net.defekt.mc.chatclient.ui.swing.JMemList;
import net.defekt.mc.chatclient.ui.swing.JMinecraftButton;
import net.defekt.mc.chatclient.ui.swing.JMinecraftField;
import net.defekt.mc.chatclient.ui.swing.JMinecraftPlayerList;
import net.defekt.mc.chatclient.ui.swing.JMinecraftServerList;
import net.defekt.mc.chatclient.ui.swing.JPlaceholderField;
import net.defekt.mc.chatclient.ui.swing.JStringMemList;
import net.defekt.mc.chatclient.ui.swing.JVBoxPanel;
import net.defekt.mc.chatclient.ui.swing.SwingUtils;
import net.defekt.mc.chatclient.ui.swing.TablePacketButton;

@SuppressWarnings({ "javadoc" })
public class Main {

    private Main() {
    }

    private static BufferedImage logoImage = null;

    public static final String VERSION = "1.8.6";
    private static final String CHANGELOG_URL = "https://raw.githubusercontent.com/Defective4/Another-Minecraft-Chat-Client/master/Changes";

    public static Font mcFont = Font.decode(null);

    private static void checkForUpdates() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL(CHANGELOG_URL).openStream()))) {
            final List<String> cgLines = new ArrayList<String>();
            String line;
            while ((line = br.readLine()) != null) {
                cgLines.add(line);
            }

            if (cgLines.size() > 1 && cgLines.get(0).equals("AMCC Change Log")) {
                final String newVersionString = IOUtils.padString(cgLines.get(1).substring(1).replace(".", ""), 3, "0",
                        0);
                final String thisVersionString = IOUtils.padString(VERSION.replace(".", ""), 3, "0", 0);

                final int newVersion = Integer.parseInt(newVersionString);
                final int thisVersion = Integer.parseInt(thisVersionString);

                if (newVersion > thisVersion) {
                    String newVersionSm = cgLines.get(1).substring(1);
                    String oldVersionSm = VERSION;

                    if (newVersionSm.length() - newVersionSm.replace(".", "").length() < 2) {
                        newVersionSm += ".0";
                    }
                    if (oldVersionSm.length() - oldVersionSm.replace(".", "").length() < 2) {
                        oldVersionSm += ".0";
                    }

                    final int nMajor = Integer.parseInt(newVersionSm.substring(0, newVersionSm.indexOf(".")));
                    final int nMinor = Integer.parseInt(
                            newVersionSm.substring(newVersionSm.indexOf(".") + 1, newVersionSm.lastIndexOf(".")));
                    final int nFix = Integer.parseInt(newVersionSm.substring(newVersionSm.lastIndexOf(".") + 1));

                    final int oMajor = Integer.parseInt(oldVersionSm.substring(0, oldVersionSm.indexOf(".")));
                    final int oMinor = Integer.parseInt(
                            oldVersionSm.substring(oldVersionSm.indexOf(".") + 1, oldVersionSm.lastIndexOf(".")));
                    final int oFix = Integer.parseInt(oldVersionSm.substring(oldVersionSm.lastIndexOf(".") + 1));

                    int diff = 0;
                    String vtype = "";

                    if (oFix != nFix) {
                        diff = nFix - oFix;
                        vtype = "minor";
                    }
                    if (oMinor != nMinor) {
                        diff = nMinor - oMinor;
                        vtype = "major";
                    }
                    if (oMajor != nMajor) {
                        diff = nMajor - oMajor;
                        vtype = "major";
                    }

                    cgLines.remove(0);
                    cgLines.remove(0);

                    SwingUtils.showVersionDialog("v" + VERSION, "v" + newVersionSm, diff, vtype, cgLines);
                }
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        PluginDescription[] descs = Plugins.listPlugins(true);
        List<String> deleted = up.getDeletedPlugins();
        List<String> en = up.getEnabledPlugins();
        for (PluginDescription desc : descs) {
            String id = desc.getUID();
            if (deleted.contains(id)) {
                try {
                    desc.getOrigin().delete();
                    en.remove(id);
                } catch (Exception e) {
                    e.printStackTrace(); // TODO error handling
                }
            }
        }
        deleted.clear();
        descs = Plugins.listPlugins();
        for (PluginDescription desc : descs)
            if (up.getEnabledPlugins().contains(desc.getUID())) try {
                Plugins.loadPlugin(desc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        Main.main();
    }

    public static void main() {
        SwingUtils.setNativeLook(up);

        if (!up.isWasLangSet()) {

            final JComboBox<Language> languages = new JComboBox<>(Language.values());
            languages.setSelectedItem(up.getAppLanguage() == null ? Language.English : up.getAppLanguage());

            final JFrame win = new JFrame("Choose your language");
            win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            final JButton ct = new JButton("Ok");
            ct.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    synchronized (win) {
                        win.notify();
                    }
                }
            });

            final JOptionPane cp = new JOptionPane(new Object[] { "Choose your language", languages },
                    JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_OPTION, null, new Object[] { ct });

            win.setContentPane(cp);
            win.pack();
            win.setResizable(false);
            SwingUtils.centerWindow(win);
            win.setVisible(true);
            win.setAlwaysOnTop(true);
            synchronized (win) {
                try {
                    win.wait();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }

            win.dispose();
            up.setAppLanguage((Language) languages.getSelectedItem());
        }

        checkForUpdates();
        try {
            if (!up.getUnicodeCharactersMode()
                    .equalsIgnoreCase(UserPreferences.Constants.UNICODECHARS_KEY_FORCE_UNICODE)) {
                mcFont = Font.createFont(Font.TRUETYPE_FONT,
                        Main.class.getResourceAsStream("/resources/Minecraftia-Regular.ttf"));
            }
            mcFont = mcFont.deriveFont((float) 14);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        try {
            logoImage = ImageIO.read(Main.class.getResourceAsStream("/resources/logo.png"));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        if (up.isEnableInventoryHandling() && up.isLoadInventoryTextures()) {
            SwingItemsWindow.initTextures(new Main(), true);
        }
        new Main().init();
    }

    public static final UserPreferences up = UserPreferences.load();
    private List<ServerEntry> servers = Collections.synchronizedList(new ArrayList<ServerEntry>());
    private final JMinecraftServerList serverListComponent = new JMinecraftServerList(this, true);
    private final JMinecraftServerList lanListComponent = new JMinecraftServerList(this, false);
    private final JTabbedPane tabPane = new JTabbedPane();
    private final Map<JSplitPane, MinecraftClient> clients = new HashMap<JSplitPane, MinecraftClient>();
    private final JFrame win = new JFrame();
    private TrayIcon trayIcon = null;
    private final DiscordPresence discordIntegr = new DiscordPresence("1015565248364826694", this, tabPane, clients);

    private MinecraftClient trayLastMessageSender = null;
    private int trayLastMessageType = 0;

    private ServerEntry selectedServer = null;

    private ActionListener alis;

    public void moveServer(final int index, final int direction) {
        int targetIndex = -1;
        if (index > 0 && direction == 0) {
            targetIndex = index - 1;
        } else if (index < servers.size() - 1 && direction == 1) {
            targetIndex = index + 1;
        }

        if (targetIndex == -1) return;

        final ServerEntry s1 = servers.get(index);
        final ServerEntry s2 = servers.get(targetIndex);

        synchronized (servers) {
            servers.set(targetIndex, s1);
            servers.set(index, s2);
        }

        final ServerEntry[] entries = servers.toArray(new ServerEntry[servers.size()]);

        serverListComponent.setListData(entries);

        final int tgIndex = targetIndex;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                serverListComponent.setSelectedIndex(tgIndex);
            }
        });
    }

    private void addToList(final String host, final int port, final String name, final String version,
            final ForgeMode forgeMode) {
        final ServerEntry entry = new ServerEntry(host, port, name, version, forgeMode);
        for (final ServerEntry se : servers)
            if (se.equals(entry)) return;
        synchronized (servers) {
            servers.add(entry);
        }
        entry.ping();

        final ServerEntry[] entries = servers.toArray(new ServerEntry[servers.size()]);

        serverListComponent.setListData(entries);

    }

    private void removeFromList(final ServerEntry entry) {
        synchronized (servers) {
            servers.remove(entry);
        }
        final ServerEntry[] entries = servers.toArray(new ServerEntry[servers.size()]);
        serverListComponent.setListData(entries);
    }

    private final Runnable upSaveRunnable = new Runnable() {

        @Override
        public void run() {
            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(UserPreferences.serverFile))) {
                os.writeObject(up);
                os.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    };

    private static boolean qmdShowing = false;

    private static void showQuickMessageDialog(final MinecraftClient cl) {
        if (qmdShowing) return;
        final JTextField mField = new JPlaceholderField(Messages.getString("Main.quickMessageDialog"));

        final String label = cl.getHost() + ":" + cl.getPort();
        qmdShowing = true;
        final int resp = JOptionPane.showOptionDialog(null,
                new Object[] { Messages.getString("Main.quickMessageRecipient") + label, mField },
                Messages.getString("Main.quickMesage"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[] { Messages.getString("Main.ok"), Messages.getString("Main.cancel") }, 0);
        qmdShowing = false;
        if (resp == 0) {
            final String msg = mField.getText();
            if (msg.replace(" ", "").isEmpty()) return;
            try {
                cl.sendChatMessage(msg);
            } catch (final IOException e) {
                for (final ClientListener ls : cl.getClientListeners(true)) {
                    ls.disconnected(e.toString(), cl);
                }
            }
        }
    }

    private void init() {
        discordIntegr.start();

        synchronized (up.getServers()) {
            for (final ServerEntry ent : up.getServers()) {
                addToList(ent.getHost(), ent.getPort(), ent.getName(), ent.getVersion(), ent.getForgeMode());
                ent.ping();
            }
        }
        servers = up.getServers();
        final ServerEntry[] entries = servers.toArray(new ServerEntry[servers.size()]);

        MinecraftStat.listenOnLAN(new LANListener() {

            @Override
            public void serverDiscovered(final InetAddress sender, final String motd, final int port) {
                final ServerEntry[] ets = lanListComponent.getListData() == null ? new ServerEntry[0]
                        : lanListComponent.getListData();
                final ServerEntry ent = new ServerEntry(sender.getHostAddress(), port,
                        sender.getHostAddress() + ":" + Integer.toString(port), Messages.getString("Main.Auto"),
                        ForgeMode.AUTO);
                for (final ServerEntry et : ets)
                    if (et.equals(ent)) return;
                final ServerEntry[] ets2 = new ServerEntry[ets.length + 1];
                for (int x = 0; x < ets.length; x++) {
                    ets2[x] = ets[x];
                }
                ets2[ets2.length - 1] = ent;
                lanListComponent.setListData(ets2);

                ent.ping();
            }
        });

        serverListComponent.setListData(entries);
        Runtime.getRuntime().addShutdownHook(new Thread(upSaveRunnable));

        win.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        win.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                if (clients.size() > 0) {
                    final JDialog diag = new JDialog(win);
                    diag.setModal(true);
                    diag.setTitle(Messages.getString("Main.exitDialogTitle"));

                    final JButton ok = new JButton(Messages.getString("Main.ok"));
                    final JButton toTray = new JButton(Messages.getString("Main.exitMinimizeOption"));
                    final JButton cancel = new JButton(Messages.getString("Main.exitCancelOption"));
                    toTray.setEnabled(SystemTray.isSupported());
                    final JCheckBox rememberOp = new JCheckBox(Messages.getString("Main.exitRememberChoice"));

                    ok.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(final ActionEvent ev) {
                            if (rememberOp.isSelected()) {
                                up.setCloseMode(Constants.WINDOW_CLOSE_EXIT);
                            }
                            System.exit(0);
                        }
                    });

                    cancel.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(final ActionEvent ev) {
                            diag.dispose();
                        }
                    });

                    toTray.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(final ActionEvent ev) {
                            if (rememberOp.isSelected()) {
                                up.setCloseMode(Constants.WINDOW_CLOSE_TO_TRAY);
                            }
                            if (trayIcon != null) return;
                            diag.dispose();
                            final SystemTray tray = SystemTray.getSystemTray();
                            trayIcon = new TrayIcon(IOUtils.scaleImage(logoImage, 0.5),
                                    "Another Minecraft Chat Client");
                            try {
                                final MouseListener ml = new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(final MouseEvent e) {
                                        if (e.getButton() != MouseEvent.BUTTON1) return;
                                        tray.remove(trayIcon);
                                        trayIcon = null;
                                        win.setVisible(true);
                                    }
                                };
                                trayIcon.addMouseListener(ml);

                                trayIcon.addActionListener(new ActionListener() {

                                    @Override
                                    public void actionPerformed(final ActionEvent e) {
                                        switch (trayLastMessageType) {
                                            case 0: {
                                                showQuickMessageDialog(trayLastMessageSender);
                                                break;
                                            }
                                            case 1: {
                                                ml.mouseClicked(new MouseEvent(win, 0, System.currentTimeMillis(), 0, 0,
                                                        0, 0, 0, 1, false, MouseEvent.BUTTON1));
                                                break;
                                            }
                                            default: {
                                                break;
                                            }
                                        }
                                    }
                                });

                                final PopupMenu menu = new PopupMenu();

                                final MenuItem quit = new MenuItem(Messages.getString("Main.trayQuitItem"));
                                quit.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(final ActionEvent ev2) {
                                        System.exit(0);
                                    }
                                });

                                final MenuItem open = new MenuItem(Messages.getString("Main.trayOpenGUIItem"));
                                open.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(final ActionEvent ev2) {
                                        ml.mouseClicked(new MouseEvent(win, 0, System.currentTimeMillis(), 0, 0, 0, 0,
                                                0, 1, false, MouseEvent.BUTTON1));
                                    }
                                });
                                open.setFont(win.getFont().deriveFont(Font.BOLD));

                                final Map<String, List<MinecraftClient>> labels = new HashMap<>();

                                for (final MinecraftClient cl : clients.values()) {
                                    final String srvLabel = cl.getHost() + ":" + cl.getPort();
                                    if (!labels.containsKey(srvLabel)) {
                                        labels.put(srvLabel, new ArrayList<>());
                                    }
                                    labels.get(srvLabel).add(cl);
                                }

                                final MenuItem options = new MenuItem(Messages.getString("Main.optionsMenu"));
                                options.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(final ActionEvent ev2) {
                                        showOptionsDialog();
                                    }
                                });

                                menu.add(open);
                                menu.addSeparator();
                                for (final String label : labels.keySet()) {
                                    final Menu srvMenu = new Menu(label);
                                    for (final MinecraftClient cl : labels.get(label)) {
                                        final Menu pMenu = new Menu(cl.getUsername()) {
                                            {
                                                final MinecraftClient client = cl;
                                                final MenuItem dcItem = new MenuItem(
                                                        Messages.getString("Main.trayDisconnectItem"));
                                                final MenuItem qmItem = new MenuItem(
                                                        Messages.getString("Main.trayQuickMessageItem"));
                                                final MenuItem invItem = new MenuItem(
                                                        Messages.getString("Main.showInventoryButton"));
                                                final Menu ins = this;

                                                dcItem.addActionListener(new ActionListener() {

                                                    @Override
                                                    public void actionPerformed(final ActionEvent e) {
                                                        client.close();
                                                        srvMenu.remove(ins);
                                                        if (srvMenu.getItemCount() == 0) {
                                                            menu.remove(srvMenu);
                                                        }
                                                        for (final ClientListener ls : client
                                                                .getClientListeners(true)) {
                                                            ls.disconnected(Messages.getString("Main.trayClosedReason"),
                                                                    client);
                                                        }
                                                    }
                                                });

                                                qmItem.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(final ActionEvent ev) {
                                                        showQuickMessageDialog(client);
                                                    }
                                                });

                                                invItem.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(final ActionEvent ev) {
                                                        if (!up.isEnableInventoryHandling()) return;
                                                        ItemsWindow ww = client.getInventory();
                                                        if (ww instanceof SwingItemsWindow) {
                                                            SwingItemsWindow swi = (SwingItemsWindow) ww;
                                                            swi.openWindow(win, up.isSendWindowClosePackets());
                                                        }
                                                    }
                                                });

                                                add(qmItem);
                                                add(dcItem);
                                                add(invItem);
                                            }
                                        };

                                        srvMenu.add(pMenu);
                                    }
                                    menu.add(srvMenu);
                                }

                                menu.addSeparator();
                                menu.add(options);
                                menu.add(quit);
                                trayIcon.setPopupMenu(menu);

                                tray.add(trayIcon);
                                win.setVisible(false);
                            } catch (final AWTException e1) {
                                e1.printStackTrace();
                            }
                        }

                    });

                    switch (up.getCloseMode()) {
                        default: {
                            break;
                        }
                        case 1: {
                            if (toTray.isEnabled()) {
                                toTray.doClick();
                                return;
                            }
                            break;
                        }
                        case 2: {
                            ok.doClick();
                            return;
                        }
                    }

                    final JOptionPane op = new JOptionPane(
                            new Object[] {
                                    Messages.getString("Main.trayExitQuestion") + Integer.toString(clients.size())
                                            + Messages.getString("Main.trayExitQuestionLine2Append"),
                                    rememberOp },
                            JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null,
                            new JButton[] { ok, toTray, cancel });

                    diag.setContentPane(op);
                    diag.pack();
                    SwingUtils.centerWindow(diag);
                    diag.setVisible(true);
                } else {
                    System.exit(0);
                }
            }
        });

        win.setTitle("Another Minecraft Chat Client v" + VERSION);
        if (logoImage != null) {
            win.setIconImage(logoImage);
        }

        final JTabbedPane sTypesPane = new JTabbedPane();

        final JPanel serverListBox = new JPanel();
        serverListBox.setLayout(new BoxLayout(serverListBox, BoxLayout.Y_AXIS));
        serverListBox.setPreferredSize(
                new Dimension((int) (SwingUtils.sSize.width / 1.5), (int) (SwingUtils.sSize.height / 1.5)));

        final JScrollPane serverListPane = new JScrollPane(serverListComponent);
        serverListPane.setOpaque(false);
        serverListBox.setBackground(new Color(60, 47, 74));

        serverListPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        serverListBox.add(serverListPane);
        serverListComponent.setMinimumSize(serverListBox.getPreferredSize());

        final Box controlsBox = Box.createHorizontalBox();

        final JButton addServer = new JMinecraftButton(Messages.getString("Main.addServerOption"));
        addServer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JTextField nameField = new JPlaceholderField(Messages.getString("Main.serveNameField"));
                nameField.setText(Messages.getString("Main.defaultServerName"));
                final JTextField hostField = new JPlaceholderField(Messages.getString("Main.serverAddressField"));

                final JComboBox<String> versionField = new JComboBox<>();
                versionField.addItem("Auto");
                versionField.addItem("Always Ask");
                for (final ProtocolNumber num : ProtocolNumber.values()) {
                    versionField.addItem(num.name);
                }
                final JComboBox<ForgeMode> forgeField = new JComboBox<>();
                for (final ForgeMode mode : ForgeMode.values()) {
                    forgeField.addItem(mode);
                }

                final Box contents = Box.createVerticalBox();

                final JLabel errorLabel = new JLabel("");
                errorLabel.setForeground(Color.red);

                contents.add(errorLabel);
                contents.add(new JLabel(Messages.getString("Main.basicServerInfoLabel")));
                contents.add(new JLabel(" "));
                contents.add(nameField);
                contents.add(hostField);
                contents.add(new JLabel(" "));
                contents.add(new JLabel(Messages.getString("Main.version") + ":"));
                contents.add(versionField);
                contents.add(new JLabel(" "));
                contents.add(new JLabel("Forge:"));
                contents.add(forgeField);

                for (final Component c : contents.getComponents())
                    if (c instanceof JComponent) {
                        ((JComponent) c).setAlignmentX(Component.LEFT_ALIGNMENT);
                    }

                do {
                    try {
                        final int response = JOptionPane.showOptionDialog(win, contents,
                                Messages.getString("Main.addServerDialogTitle"), JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, null, null);

                        if (response == JOptionPane.OK_OPTION) {
                            final String server = hostField.getText();
                            final String name = nameField.getText();

                            if (server.isEmpty() || name.isEmpty()) {
                                errorLabel.setText(Messages.getString("Main.addServerDialogEmptyFieldsWarning"));
                                continue;
                            }

                            String host = server;
                            int port = 25565;
                            if (server.contains(":") && server.split(":").length > 1) {
                                final String[] ag = server.split(":");
                                host = ag[0];
                                port = Integer.parseInt(ag[1]);
                            }

                            addToList(host, port, name, (String) versionField.getSelectedItem(),
                                    (ForgeMode) forgeField.getSelectedItem());

                            break;
                        } else {
                            break;
                        }

                    } catch (final Exception e1) {
                        e1.printStackTrace();
                        errorLabel.setText(e1.toString());
                    }
                } while (true);
            }
        });

        final JButton refresh = new JMinecraftButton(Messages.getString("Main.refreshOption"));
        refresh.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (serverListComponent.getListData() != null) {
                    serverListComponent.setListData(serverListComponent.getListData());
                }
                if (servers != null) {
                    for (final ServerEntry entry : servers) {
                        try {
                            if (!entry.refreshing) {
                                entry.ping();
                            }
                        } catch (final Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        });

        final JButton removeServer = new JMinecraftButton(Messages.getString("Main.removeServerOption"));
        removeServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev) {
                if (serverListComponent.getSelectedValue() != null) {
                    removeFromList(serverListComponent.getSelectedValue());
                    refresh.doClick();
                }
            }
        });
        removeServer.setEnabled(false);

        final JButton connectServer = new JMinecraftButton(Messages.getString("Main.connectServerOption"));
        alis = new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final ServerEntry et = sTypesPane.getSelectedIndex() == 0 ? serverListComponent.getSelectedValue()
                        : lanListComponent.getSelectedValue();
                if (et == null) return;

                if (et.isRefreshing() || et.isError()) {
                    final String haltReason = et.isRefreshing() ? Messages.getString("Main.haltReasonRefreshing")
                            : Messages.getString("Main.haltReasonError");

                    final int haltResponse = JOptionPane.showOptionDialog(win,
                            new String[] { haltReason, Messages.getString("Main.haltQuestion") },
                            Messages.getString("Main.haltTitle"), JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE, null,
                            new String[] { Messages.getString("Main.haltResponseCancel"),
                                    Messages.getString("Main.haltResponseJoin") },
                            -1);

                    switch (haltResponse) {
                        case 1: {
                            break;
                        }

                        case 0: {
                            return;
                        }

                        default: {
                            return;
                        }
                    }

                }

                final JTabbedPane userTabs = new JTabbedPane();
                final JVBoxPanel box = new JVBoxPanel();

                final JComboBox<AuthType> authType = new JComboBox<AuthType>(AuthType.values());

                box.add(new JLabel(Messages.getString("Main.selectAuthType") + ":"));
                box.add(authType);
                box.add(new JLabel(" "));
                box.add(new JLabel(Messages.getString("Main.enterUsernameLabel")));

                final Box uCtl = Box.createHorizontalBox();

                BufferedImage x = null;
                try {
                    x = ImageIO.read(getClass().getResourceAsStream("/resources/x.png"));
                    x = IOUtils.resizeImageProp(x, 12);
                } catch (final Exception e2) {
                    e2.printStackTrace();
                }
                final JButton unameClear = x == null ? new JButton("C") : new JButton(new ImageIcon(x));
                unameClear.setToolTipText(Messages.getString("Main.clearUnames"));

                final JComboBox<String> unameField = new JComboBox<>();
                unameClear.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        up.clearLastUserNames();
                        unameField.removeAllItems();
                    }
                });
                unameField.setEditable(true);
                unameField.setAlignmentX(Component.LEFT_ALIGNMENT);
                for (final String uname : up.getLastUserNames()) {
                    unameField.addItem(uname);
                }

                final JPasswordField upassField = new JPasswordField();
                upassField.setEnabled(((AuthType) authType.getSelectedItem()) == AuthType.Mojang);

                authType.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        upassField.setEnabled(((AuthType) authType.getSelectedItem()) == AuthType.Mojang);
                    }
                });

                uCtl.add(unameField);
                uCtl.add(unameClear);

                box.add(uCtl);
                box.add(new JLabel(" "));
                box.add(new JLabel(Messages.getString("Main.enterPasswordLabel") + ":"));
                box.add(upassField);
                box.alignAll();

                final JVBoxPanel proxyBox = new JVBoxPanel();

                final JComboBox<ProxySetting> savedProxies = new JComboBox<>(
                        new ProxySetting[] { new ProxySetting("None", "", -1) });
                for (final ProxySetting sett : up.proxies) {
                    savedProxies.addItem(sett);
                }

                final JButton pxLoad = new JButton(Messages.getString("Main.load"));
                pxLoad.setEnabled(false);
                final JButton pxSave = new JButton(Messages.getString("Main.save"));
                pxSave.setEnabled(false);
                final JButton pxDelete = new JButton(Messages.getString("Main.delete"));
                pxDelete.setEnabled(false);
                final JTextField pxField = new JTextField();

                final Box ctlBox = Box.createHorizontalBox();
                ctlBox.add(pxLoad);
                ctlBox.add(pxDelete);

                proxyBox.add(savedProxies);
                proxyBox.add(ctlBox);
                proxyBox.add(new JLabel(" "));
                proxyBox.add(new JLabel(Messages.getString("Main.enterProxyLabel") + ":"));
                proxyBox.add(pxField);
                proxyBox.add(pxSave);
                proxyBox.add(new JLabel(" "));
                proxyBox.alignAll();

                pxField.addKeyListener(new KeyListener() {

                    @Override
                    public void keyTyped(final KeyEvent e) {
                    }

                    @Override
                    public void keyReleased(final KeyEvent e) {
                        pxSave.setEnabled(!pxField.getText().replace(" ", "").isEmpty());
                    }

                    @Override
                    public void keyPressed(final KeyEvent e) {
                    }
                });

                savedProxies.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        try {
                            if (savedProxies != null && savedProxies.getSelectedItem() != null) {
                                final boolean sel = !((ProxySetting) savedProxies.getSelectedItem()).getName()
                                        .equalsIgnoreCase("None");
                                pxDelete.setEnabled(sel);
                                pxLoad.setEnabled(sel);
                            }
                        } catch (final Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                pxLoad.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        try {
                            final ProxySetting set = (ProxySetting) savedProxies.getSelectedItem();
                            if (set != null && !set.getName().equalsIgnoreCase("None")) {
                                pxField.setText(set.getHost() + ":" + set.getPort());
                                pxSave.setEnabled(false);
                            }
                        } catch (final Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                pxDelete.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        try {
                            final ProxySetting set = (ProxySetting) savedProxies.getSelectedItem();
                            if (set != null && !set.getName().equalsIgnoreCase("None")) {
                                up.proxies.remove(set);
                                savedProxies.removeAllItems();
                                savedProxies.addItem(new ProxySetting("None", "", -1));
                                for (final ProxySetting sett : up.proxies) {
                                    savedProxies.addItem(sett);
                                }
                            }
                        } catch (final Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                pxSave.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        try {
                            final String addr = pxField.getText().replace(" ", "");
                            if (!addr.isEmpty() && addr.contains(":") && addr.split(":").length == 2) {
                                final String[] addrSplit = addr.split(":");
                                final String host = addrSplit[0];
                                final int port = Integer.parseInt(addrSplit[1]);
                                final String name = host + ":" + port;
                                final ProxySetting set = new ProxySetting(name, host, port);
                                if (!up.proxies.contains(set)) {
                                    up.proxies.add(set);
                                }
                                pxField.setText("");
                                pxSave.setEnabled(false);
                                savedProxies.removeAllItems();
                                savedProxies.addItem(new ProxySetting("None", "", -1));
                                for (final ProxySetting sett : up.proxies) {
                                    savedProxies.addItem(sett);
                                }
                            }
                        } catch (final Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                userTabs.addTab(Messages.getString("Main.logInTab"), box);
                userTabs.addTab(Messages.getString("Main.proxyTab"), proxyBox);
                Proxy proxyObj = null;

                do {
                    final int response = JOptionPane.showOptionDialog(win, userTabs,
                            Messages.getString("Main.enterUsernameTitle"), JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (response != JOptionPane.OK_OPTION) return;

                    final String uname = (String) unameField.getSelectedItem();
                    if (uname == null || (((AuthType) authType.getSelectedItem()) == AuthType.Mojang
                            && upassField.getPassword().length == 0)) {
                        continue;
                    }
                    final String proxy = pxField.getText().replace(" ", "");
                    if (!proxy.isEmpty() && proxy.contains(":") && proxy.split(":").length == 2) {
                        try {
                            final String[] pxAddr = proxy.split(":");
                            final String phost = pxAddr[0];
                            final int pport = Integer.parseInt(pxAddr[1]);
                            proxyObj = new Proxy(Type.SOCKS, new InetSocketAddress(phost, pport));
                        } catch (final Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (!up.isUsernameAlertSeen() && !uname.replaceAll("[^a-zA-Z0-9]", "").equals(uname)
                            && ((AuthType) authType.getSelectedItem()) != AuthType.Mojang
                            && ((AuthType) authType.getSelectedItem()) != AuthType.TheAltening) {
                        final int alResp = JOptionPane.showOptionDialog(win,
                                Messages.getString("Main.nickIllegalCharsWarning1") + uname
                                        + Messages.getString("Main.nickIllegalCharsWarning2")
                                        + Messages.getString("Main.nickIllegalCharsWarningQuestion"),
                                Messages.getString("Main.nickIllegalCharsWarningTitle"), JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE, null,
                                new Object[] { Messages.getString("Main.nickIllegalCharsWarningOptionYes"),
                                        Messages.getString("Main.nickIllegalCharsWarningOptionNo") },
                                0);
                        if (alResp == 0) {
                            up.setUsernameAlertSeen(true);
                            break;
                        } else {
                            continue;
                        }
                    }
                    if (!uname.isEmpty()) {
                        break;
                    }
                } while (true);

                final String uname = (String) unameField.getSelectedItem();
                final JSplitPane b = createServerPane(et, uname, new String(upassField.getPassword()),
                        ((AuthType) authType.getSelectedItem()), proxyObj);

                tabPane.addTab("", b);
                tabPane.setSelectedComponent(b);

                final Box b2 = Box.createHorizontalBox();
                b2.setName(et.getHost() + "_" + et.getName() + "_" + uname);
                final int pxh = et.getIcon() == null ? 0 : 16;

                BufferedImage bicon = null;
                if (et.getIcon() != null) {
                    try {
                        bicon = ImageIO
                                .read(new ByteArrayInputStream(Base64.getDecoder().decode(et.getIcon().getBytes())));
                    } catch (final IOException e1) {
                        e1.printStackTrace();
                    }
                }

                final BufferedImage bicon2 = bicon;

                b2.add(new JPanel() {
                    {
                        setPreferredSize(new Dimension(pxh, 16));
                    }

                    final BufferedImage bico = bicon2;

                    @Override
                    public void paintComponent(final Graphics g) {
                        g.drawImage(bico, 0, 0, 16, 16, null);
                    }
                });

                b2.add(new JLabel(" " + et.getName() + " (" + (String) unameField.getSelectedItem() + ")"));

                final JButton close = new JButton("x");
                close.setMargin(new Insets(0, 5, 0, 5));
                close.addActionListener(new ActionListener() {
                    private final JSplitPane box = b;

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        for (int x = 0; x < tabPane.getTabCount(); x++)
                            if (tabPane.getComponentAt(x).equals(box)) {
                                if (clients.containsKey(box)) {
                                    clients.get(box).close();
                                }
                                tabPane.removeTabAt(x);
                                clients.remove(b);
                                discordIntegr.update();
                                break;
                            }
                    }
                });

                b2.add(close);
                up.putUserName(uname);
                tabPane.setTabComponentAt(tabPane.getSelectedIndex(), b2);
            }
        };
        connectServer.addActionListener(alis);
        connectServer.setEnabled(false);

        serverListComponent.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    selectedServer = serverListComponent.getSelectedValue();
                    if (selectedServer != null) {
                        removeServer.setEnabled(true);
                        connectServer.setEnabled(true);
                    } else {
                        removeServer.setEnabled(false);
                        connectServer.setEnabled(false);
                    }

                }
            }
        });

        final MouseListener doubleClickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() >= 2 && connectServer.isEnabled()) {
                    connectServer.doClick();
                }
            }
        };

        serverListComponent.addMouseListener(doubleClickListener);
        controlsBox.add(connectServer);
        controlsBox.add(addServer);
        controlsBox.add(removeServer);
        controlsBox.add(refresh);

        serverListBox.add(controlsBox);

        final JPanel lanListBox = new JPanel();
        lanListBox.setBackground(serverListBox.getBackground());
        lanListBox.setLayout(new BoxLayout(lanListBox, BoxLayout.Y_AXIS));
        lanListBox.setPreferredSize(
                new Dimension((int) (SwingUtils.sSize.width / 1.5), (int) (SwingUtils.sSize.height / 1.5)));

        final JScrollPane lanListPane = new JScrollPane(lanListComponent);
        lanListPane.setOpaque(false);

        lanListPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        final Box lanControlsBox = Box.createHorizontalBox();

        final JButton lanConnect = new JMinecraftButton(connectServer.getText());
        final JButton lanAdd = new JMinecraftButton(addServer.getText());
        final JButton lanRemove = new JMinecraftButton(removeServer.getText());
        final JButton lanRefresh = new JMinecraftButton(refresh.getText());

        lanConnect.setEnabled(false);
        lanAdd.setEnabled(false);
        lanRemove.setEnabled(false);

        lanConnect.addActionListener(alis);
        lanRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev) {
                lanListComponent.setListData(new ServerEntry[0]);
            }
        });

        lanControlsBox.add(lanConnect);
        lanControlsBox.add(lanAdd);
        lanControlsBox.add(lanRemove);
        lanControlsBox.add(lanRefresh);

        lanListComponent.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    selectedServer = lanListComponent.getSelectedValue();
                    if (selectedServer != null) {
                        lanConnect.setEnabled(true);
                    } else {
                        lanConnect.setEnabled(false);
                    }

                }
            }
        });

        lanListBox.add(lanListPane);
        lanListBox.add(lanControlsBox);
        lanListComponent.setMinimumSize(lanListBox.getPreferredSize());

        sTypesPane.addTab(Messages.getString("Main.serversTabInternet"), serverListBox);
        sTypesPane.addTab(Messages.getString("Main.serversTabLAN"), lanListBox);

        tabPane.addTab(Messages.getString("Main.serversListTab"), sTypesPane);

        final JMenu fileMenu = new JMenu(Messages.getString("Main.fileMenu")) {
            {
                setMnemonic(getText().charAt(0));
                add(new JMenuItem(Messages.getString("Main.fileMenuQuit")) {
                    {
                        addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(final ActionEvent ev) {
                                System.exit(0);
                            }
                        });
                    }
                });
            }
        };
        final JMenu optionMenu = new JMenu(Messages.getString("Main.optionsMenu")) {
            {
                setMnemonic(getText().charAt(0));
                add(new JMenuItem(Messages.getString("Main.optionsMenuSettings")) {
                    {
                        addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                showOptionsDialog();
                            }
                        });
                    }
                });
            }
        };

        JMenu helpMenu = new JMenu(Messages.getString("Main.helpMenu")) {
            {
                setMnemonic(getText().charAt(0));
                add(new JMenuItem(Messages.getString("Main.helpMenuAboutStats")) {
                    {
                        setEnabled(false);
                    }
                });
            }
        };

        JMenu pluginsMenu = new JMenu("Plugins") {
            {
                add(new JMenuItem("Plugin Manager") {
                    {
                        addActionListener(e -> {
                            showPluginManager();
                        });
                    }
                });
            }
        };

        win.setJMenuBar(new JMenuBar() {
            {
                add(fileMenu);
                add(optionMenu);
                add(helpMenu);
                add(pluginsMenu);
            }

        });
        win.setContentPane(tabPane);
        win.pack();
        SwingUtils.centerWindow(win);
        win.setVisible(true);

    }

    // TODO
    private void showPluginManager() {
        JDialog od = new JDialog(win);
        od.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        od.setModal(true);
        od.setResizable(false);
        od.setTitle("Plugin Manager");

        JTabbedPane tabs = new JTabbedPane();
        JPanel installedDisplay = new JPanel(new GridLayout(0, 1, 0, 10));
        JPanel availableDisplay = new JPanel(new GridLayout(0, 1, 0, 10));

        Runnable sync = () -> {
            Component installedLoadingCpt = createLoadingCpt("Verifying installed plugins...");
            setAllTabs(tabs, false);
            installedDisplay.add(installedLoadingCpt);
            int initial = tabs.getSelectedIndex();

            PluginDescription[] descs = Plugins.listPlugins();
            Plugins.verify(ex -> {
                SwingUtils.showErrorDialog(od, Messages.getString("ServerDetailsDialog.error"), ex,
                        "An error occured while verifying installed plugins");
            }, descs);
            for (PluginDescription desc : descs)
                installedDisplay.add(new PluginDisplayPanel(desc, false, null));

            installedDisplay.remove(installedLoadingCpt);
            setAllTabs(tabs, true, initial);
            tabs.repaint();
        };
        new Thread(sync).start();

        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 1) {
                new Thread(() -> {
                    Component installedLoadingCpt = createLoadingCpt("Fetching available plugins list...");
                    setAllTabs(tabs, false);
                    int initial = tabs.getSelectedIndex();

                    availableDisplay.removeAll();
                    availableDisplay.add(installedLoadingCpt);
                    for (PluginDescription plugin : Plugins.listRemotePlugins(ex -> {
                        SwingUtils.showErrorDialog(od, Messages.getString("ServerDetailsDialog.error"), ex,
                                "An error occured while fetching remote plugins list");
                    })) {
                        availableDisplay.add(new PluginDisplayPanel(plugin, true, desc -> {
                            Component downloadingCpt = createLoadingCpt("Downloading " + desc.getName() + "...");
                            availableDisplay.removeAll();
                            availableDisplay.add(downloadingCpt);
                            setAllTabs(tabs, false);
                            int initial2 = tabs.getSelectedIndex();

                            if (desc.getRemote() != null)
                                try (InputStream is = new URL(desc.getRemote()).openStream()) {
                                    File out = new File(Plugins.PLUGIN_DIR,
                                            desc.getName().replace(" ", "-") + "-" + desc.getVersion() + ".jar");
                                    while (out.exists()) {
                                        out = new File(Plugins.PLUGIN_DIR, "_" + out.getName());
                                    }
                                    try (OutputStream os = new FileOutputStream(out)) {
                                        byte[] buffer = new byte[1024];
                                        int read;
                                        while ((read = is.read(buffer)) > 0)
                                            os.write(buffer, 0, read);
                                        os.close();
                                    }
                                    is.close();
                                } catch (Exception e2) {
                                    SwingUtils.showErrorDialog(od, "Error!", e2,
                                            "There was an error while downloading the plugin!");
                                }

                            tabs.setSelectedIndex(0);
                            availableDisplay.remove(downloadingCpt);
                            setAllTabs(tabs, true, initial2);
                            tabs.repaint();
                            new Thread(sync).start();
                        }));
                    }

                    availableDisplay.remove(installedLoadingCpt);
                    setAllTabs(tabs, true, initial);
                    tabs.repaint();
                }).start();
            }
        });

        tabs.addTab("Installed", new JScrollPane(installedDisplay));
        tabs.addTab("Available", new JScrollPane(availableDisplay));
        tabs.setPreferredSize(new Dimension(SwingUtils.sSize.width / 3, (int) (SwingUtils.sSize.getHeight() / 3)));
        od.setContentPane(tabs);
        od.pack();
        SwingUtils.centerWindow(od);
        od.setVisible(true);
    }

    private static void setAllTabs(JTabbedPane pane, boolean state) {
        setAllTabs(pane, state, 0);
    }

    private static void setAllTabs(JTabbedPane pane, boolean state, int initial) {
        if (!state || pane.getSelectedIndex() == initial) for (int x = 0; x < pane.getTabCount(); x++)
            pane.setEnabledAt(x, state);
    }

    private static Component createLoadingCpt(String text) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel(text));
        panel.add(new JLabel(" "));
        panel.add(new JProgressBar() {
            {
                setAlignmentX(JPanel.LEFT_ALIGNMENT);
                setIndeterminate(true);
            }
        });

        return panel;
    }

    private void showOptionsDialog() {
        final JDialog od = new JDialog(win);
        od.setModal(true);
        od.setResizable(false);
        od.setTitle(Messages.getString("Main.settingsTitle"));

        final Box b = Box.createVerticalBox();

        final JTabbedPane jtp = new JTabbedPane();

        final JVBoxPanel rsBox = new JVBoxPanel();

        final JComboBox<Status> rPackBehaviorBox = new JComboBox<>(Status.values());
        rPackBehaviorBox.setToolTipText(Messages.getString("Main.rsBehaviorToolTip"));
        rPackBehaviorBox.setSelectedItem(up.getResourcePackBehavior());

        final JCheckBox rsPackShowCheck = new JCheckBox(Messages.getString("Main.rsPackShowCheck"),
                up.isShowResourcePackMessages());
        rsPackShowCheck.setToolTipText(Messages.getString("Main.rsPackShowToolTip"));

        final JPlaceholderField rsPackMsgText = new JPlaceholderField(Messages.getString("Main.rsPackMessageField"));
        rsPackMsgText.setToolTipText(Messages.getString("Main.rsPackMessageToolTip"));
        rsPackMsgText.setText(up.getResourcePackMessage());

        final JComboBox<Position> rsPackMessagePosition = new JComboBox<>(Position.values());
        rsPackMessagePosition.setSelectedItem(up.getResourcePackMessagePosition());

        rsBox.add(new JLabel(Messages.getString("Main.rsPackBehaviorLabel")));
        rsBox.add(rPackBehaviorBox);
        rsBox.add(new JLabel(" "));
        rsBox.add(rsPackShowCheck);
        rsBox.add(new JLabel(" "));
        rsBox.add(new JLabel(Messages.getString("Main.rsPackMessageLabel")));
        rsBox.add(rsPackMsgText);
        rsBox.add(new JLabel(" "));
        rsBox.add(new JLabel(Messages.getString("Main.rsPackPositionLabel")));
        rsBox.add(rsPackMessagePosition);
        rsBox.add(new JTextPane() {
            {
                setEditable(false);
                setOpaque(false);
            }
        });

        rsBox.alignAll();

        final JVBoxPanel skBox = new JVBoxPanel();
        skBox.add(new JLabel(Messages.getString("Main.skinFetchMetchodLabel")));
        final JComboBox<SkinRule> ruleBox = new JComboBox<>(SkinRule.values());
        ruleBox.setToolTipText(Messages.getString("Main.skinFetchToolTip"));
        ruleBox.setSelectedItem(up.getSkinFetchRule());
        skBox.add(ruleBox);
        skBox.add(new JTextPane() {
            {
                setText("\r\n" + Messages.getString("Main.skinFetchTip"));
                setEditable(false);
            }
        });

        skBox.alignAll();

        final JVBoxPanel pkBox = new JVBoxPanel();

        final JCheckBox forceLegacySLP = new JCheckBox(Messages.getString("Main.forceLegacySLP"));
        forceLegacySLP.setToolTipText(Messages.getString("Main.forceLegacySLPToolTip"));
        forceLegacySLP.setSelected(up.isForceLegacySLP());

        final JCheckBox ignoreKAPackets = new JCheckBox(Messages.getString("Main.ignoreKAPackets"));
        ignoreKAPackets.setToolTipText(Messages.getString("Main.ignoreKAPacketsToolTip"));
        ignoreKAPackets.setSelected(up.isIgnoreKeepAlive());

        final JCheckBox ignoreDSPackets = new JCheckBox(Messages.getString("Main.ignoreDSPackets"));
        ignoreDSPackets.setToolTipText(Messages.getString("Main.ignoreDSPacketsToolTip"));
        ignoreDSPackets.setSelected(up.isIgnoreDisconnect());

        final JTextField brandField = new JPlaceholderField(Messages.getString("Main.brandField"));
        brandField.setToolTipText(Messages.getString("Main.brandToolTip"));
        brandField.setText(up.getBrand());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                brandField.setOpaque(true);
            }
        });

        final JSpinner pingField = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        pingField.setToolTipText(Messages.getString("Main.pingToolTip"));
        pingField.setValue(up.getAdditionalPing());
        SwingUtils.alignSpinner(pingField);

        final JSpinner maxPacketsOnListField = new JSpinner(new SpinnerNumberModel(1, 1, 50000, 1));
        maxPacketsOnListField.setValue(up.getMaxPacketsOnList());
        SwingUtils.alignSpinner(maxPacketsOnListField);

        final JCheckBox disablePacketAnalyzer = new JCheckBox(Messages.getString("Main.optionsDisablePacketAnalyzer"));
        disablePacketAnalyzer.setToolTipText(Messages.getString("Main.optionsDisablePacketAnalyzerTooltip"));
        disablePacketAnalyzer.setSelected(up.isDisablePacketAnalyzer());

        pkBox.add(ignoreKAPackets);
        pkBox.add(ignoreDSPackets);
        pkBox.add(new JSeparator());
        pkBox.add(forceLegacySLP);
        pkBox.add(new JSeparator());
        pkBox.add(new JLabel(" "));
        pkBox.add(new JLabel(Messages.getString("Main.optionsAnalyzerMax")));
        pkBox.add(maxPacketsOnListField);
        pkBox.add(disablePacketAnalyzer);
        pkBox.add(new JLabel(" "));
        pkBox.add(new JSeparator());
        pkBox.add(new JLabel(" "));
        pkBox.add(new JLabel(Messages.getString("Main.pingLabel")));
        pkBox.add(new JLabel(Messages.getString("Main.pingLabel2")));
        pkBox.add(pingField);
        pkBox.add(new JLabel(" "));
        pkBox.add(new JLabel(Messages.getString("Main.brandLabel")));
        pkBox.add(brandField);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                brandField.setMaximumSize(new Dimension(brandField.getWidth(), 20));
            }
        });

        pkBox.add(new JTextPane() {
            {
                setEditable(false);
                setOpaque(false);
            }
        });
        pkBox.alignAll();

        final JVBoxPanel trBox = new JVBoxPanel();

        final JComboBox<String> trMessagesMode = new JComboBox<>(
                new String[] { Constants.TRAY_MESSAGES_KEY_ALWAYS, Constants.TRAY_MESSAGES_KEY_MENTION,
                        Constants.TRAY_MESSAGES_KEY_KEYWORD, Constants.TRAY_MESSAGES_KEY_NEVER });
        trMessagesMode.setToolTipText(Messages.getString("Main.trMessagesModeToolTip"));
        trMessagesMode.setSelectedItem(up.getTrayMessageMode());
        final JCheckBox showDMessages = new JCheckBox(Messages.getString("Main.showDMessages"));
        showDMessages.setToolTipText(Messages.getString("Main.showDMessagesToolTip"));
        showDMessages.setSelected(up.isTrayShowDisconnectMessages());

        final JMemList<String> trMessagesKeywords = new JMemList<>();
        trMessagesKeywords.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        trMessagesKeywords.setListData(up.getTrayKeyWords() == null ? new String[0] : up.getTrayKeyWords());

        final Box trKwControls = Box.createHorizontalBox();

        final JButton addKeyword = new JButton(Messages.getString("Main.keywordAdd"));
        final JButton removeKeyword = new JButton(Messages.getString("Main.keywordRemove"));

        removeKeyword.setEnabled(trMessagesKeywords.getSelectedIndex() != -1);

        trMessagesKeywords.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    removeKeyword.setEnabled(e.getFirstIndex() != -1);
                }
            }
        });

        removeKeyword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (trMessagesKeywords.getSelectedIndex() == -1) return;

                final String selected = trMessagesKeywords.getSelectedValue();
                final int index = trMessagesKeywords.getSelectedIndex();

                final List<String> ld = new ArrayList<>();
                Collections.addAll(ld, trMessagesKeywords.getListData());

                ld.remove(selected);

                String[] ss = new String[ld.size()];
                ss = ld.toArray(ss);

                trMessagesKeywords.setListData(ss);

                trMessagesKeywords.setSelectedIndex(index > 0 ? index - 1 : 0);

                removeKeyword.setEnabled(ss.length > 0);
            }
        });

        addKeyword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

                final JTextField kwField = new JPlaceholderField(Messages.getString("Main.kewyordField"));

                final int response = JOptionPane.showOptionDialog(od,
                        new Object[] { Messages.getString("Main.keywordDialogLabel"), kwField },
                        Messages.getString("Main.keywordDialogTitle"), JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        new String[] { Messages.getString("Main.ok"), Messages.getString("Main.qmCancelOption") }, 0);

                if (response == 0 && !kwField.getText().isEmpty()) {
                    final List<String> ld = new ArrayList<>();
                    Collections.addAll(ld, trMessagesKeywords.getListData());
                    if (ld.contains(kwField.getText())) return;
                    ld.add(kwField.getText());

                    String[] ss = new String[ld.size()];
                    ss = ld.toArray(ss);

                    trMessagesKeywords.setListData(ss);

                    removeKeyword.setEnabled(ss.length > 0);
                }
            }
        });

        trKwControls.add(addKeyword);
        trKwControls.add(removeKeyword);

        final JScrollPane trKeywordsScroll = new JScrollPane(trMessagesKeywords);

        final JButton clearRem = new JButton(Messages.getString("Main.clearRem"));
        if (up.getCloseMode() == Constants.WINDOW_CLOSE_ALWAYS_ASK) {
            clearRem.setEnabled(false);
        }

        clearRem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev2) {
                up.setCloseMode(0);
                clearRem.setEnabled(false);
            }
        });

        trBox.add(new JLabel(Messages.getString("Main.trMessagesModeLabel")));
        trBox.add(trMessagesMode);
        trBox.add(showDMessages);
        trBox.add(new JLabel(" "));
        trBox.add(clearRem);
        trBox.add(new JLabel(" "));
        trBox.add(new JLabel(Messages.getString("Main.keywordLabel")));
        trBox.add(trKeywordsScroll);
        trBox.add(trKwControls);
        trBox.add(new JTextPane() {
            {
                setEditable(false);
                setOpaque(false);
            }
        });

        trBox.alignAll();

        final JTabbedPane apPane = new JTabbedPane();

        final JVBoxPanel apButtonSettings = new JVBoxPanel();
        final JScrollPane apButtonSettingsSP = new JScrollPane(apButtonSettings);
        final JVBoxPanel apButtonSettingsFull = new JVBoxPanel();
        apButtonSettingsSP.setPreferredSize(new Dimension(0, 0));

        final ColorPreferences cp = Main.up.getColorPreferences();
        final ColorPreferences cprefCopy = new UserPreferences.ColorPreferences();
        cprefCopy.setColorDisabledButton(cp.getColorDisabledButton());
        cprefCopy.setColorEnabledButton(cp.getColorEnabledButton());
        cprefCopy.setColorEnabledHoverButton(cp.getColorEnabledHoverButton());
        cprefCopy.setColorText(cp.getColorText());
        cprefCopy.setDisabledColorText(cp.getDisabledColorText());

        final JColorChooserButton apButtonEnabled = new JColorChooserButton(cp.getColorEnabledButton(), od);
        final JColorChooserButton apButtonEnabledHover = new JColorChooserButton(cp.getColorEnabledHoverButton(), od);
        final JColorChooserButton apButtonDisabled = new JColorChooserButton(cp.getColorDisabledButton(), od);
        final JColorChooserButton apButtonText = new JColorChooserButton(cp.getColorText(), od);
        final JColorChooserButton apButtonTextDisabled = new JColorChooserButton(cp.getDisabledColorText(), od);

        final JCheckBox apButtonLockColors = new JCheckBox(Messages.getString("Main.apButtonLockColors"));
        apButtonLockColors.setSelected(true);
        final JButton apButtonReset = new JButton(Messages.getString("Main.apButtonReset"));

        final JMinecraftButton sampleButton = new JMinecraftButton("Test");
        final JMinecraftButton sampleDisabledButton = new JMinecraftButton("Test");
        sampleButton.setCp(cprefCopy);
        sampleDisabledButton.setCp(cprefCopy);
        sampleDisabledButton.setEnabled(false);

        apButtonSettings.add(apButtonLockColors);
        apButtonSettings.add(new JLabel(" " + Messages.getString("Main.apButtonSettingsBGLabel")));
        apButtonSettings.add(apButtonEnabled);
        apButtonSettings.add(new JLabel(" " + Messages.getString("Main.apButtonSettingsHoverLabel")));
        apButtonSettings.add(apButtonEnabledHover);
        apButtonSettings.add(new JLabel(" " + Messages.getString("Main.apButtonSettingsDisabledLabel")));
        apButtonSettings.add(apButtonDisabled);
        apButtonSettings.add(new JLabel(" " + Messages.getString("Main.apButtonSettingsTextColor")));
        apButtonSettings.add(apButtonText);
        apButtonSettings.add(new JLabel(" " + Messages.getString("Main.apButtonSettingsDTexTColor")));
        apButtonSettings.add(apButtonTextDisabled);
        apButtonSettings.add(new JLabel(" "));
        apButtonSettings.add(apButtonReset);
        apButtonSettings.add(new JLabel(" "));

        apButtonEnabled.addColorChangeListener(new ColorChangeListener() {

            @Override
            public void colorChanged(final Color c) {
                cprefCopy.setColorEnabledButton(SwingUtils.getHexRGB(c));
                if (apButtonLockColors.isSelected()) {
                    final Color hover = SwingUtils.brighten(c, 51);
                    final Color disabled = SwingUtils.brighten(c,
                            (int) -(((c.getRed() + c.getGreen() + c.getBlue()) / 3) / 1.3));
                    cprefCopy.setColorEnabledHoverButton(SwingUtils.getHexRGB(hover));
                    cprefCopy.setColorDisabledButton(SwingUtils.getHexRGB(disabled));
                    apButtonEnabledHover.setColor(hover);
                    apButtonDisabled.setColor(disabled);
                }
                sampleButton.repaint();
                sampleDisabledButton.repaint();
            }
        });
        apButtonEnabledHover.addColorChangeListener(new ColorChangeListener() {
            @Override
            public void colorChanged(final Color c) {
                cprefCopy.setColorEnabledHoverButton(SwingUtils.getHexRGB(c));
                sampleButton.repaint();
                sampleDisabledButton.repaint();
            }
        });

        apButtonDisabled.addColorChangeListener(new ColorChangeListener() {
            @Override
            public void colorChanged(final Color c) {
                cprefCopy.setColorDisabledButton(SwingUtils.getHexRGB(c));
                sampleButton.repaint();
                sampleDisabledButton.repaint();
            }
        });
        apButtonText.addColorChangeListener(new ColorChangeListener() {
            @Override
            public void colorChanged(final Color c) {
                cprefCopy.setColorText(SwingUtils.getHexRGB(c));
                sampleButton.repaint();
                sampleDisabledButton.repaint();
            }
        });
        apButtonTextDisabled.addColorChangeListener(new ColorChangeListener() {
            @Override
            public void colorChanged(final Color c) {
                cprefCopy.setDisabledColorText(SwingUtils.getHexRGB(c));
                sampleButton.repaint();
                sampleDisabledButton.repaint();
            }
        });
        apButtonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev2) {
                final ColorPreferences cp2 = UserPreferences.defaultColorPreferences;
                cprefCopy.setColorDisabledButton(cp2.getColorDisabledButton());
                cprefCopy.setColorEnabledButton(cp2.getColorEnabledButton());
                cprefCopy.setColorEnabledHoverButton(cp2.getColorEnabledHoverButton());
                cprefCopy.setColorText(cp2.getColorText());
                cprefCopy.setDisabledColorText(cp2.getDisabledColorText());

                apButtonDisabled.setColor(new Color(Integer.parseInt(cp2.getColorDisabledButton(), 16)));
                apButtonEnabled.setColor(new Color(Integer.parseInt(cp2.getColorEnabledButton(), 16)));
                apButtonEnabledHover.setColor(new Color(Integer.parseInt(cp2.getColorEnabledHoverButton(), 16)));
                apButtonText.setColor(new Color(Integer.parseInt(cp2.getColorText(), 16)));
                apButtonTextDisabled.setColor(new Color(Integer.parseInt(cp2.getDisabledColorText(), 16)));
                sampleButton.repaint();
                sampleDisabledButton.repaint();
            }
        });

        final Box apButtonSettingsSamples = Box.createHorizontalBox();
        apButtonSettingsSamples.add(sampleButton);
        apButtonSettingsSamples.add(sampleDisabledButton);

        apButtonSettingsFull.add(apButtonSettingsSP);
        apButtonSettingsFull.add(apButtonSettingsSamples);

        apButtonSettings.alignAll();

        final JVBoxPanel themeBox = new JVBoxPanel();

        themeBox.add(new JLabel("Look and Feel"));

        final JComboBox<String> lafBox = new JComboBox<String>(SwingUtils.getInstalledLookAndFeels());
        lafBox.setSelectedItem(up.getUiTheme());

        final JCheckBox customBtnsBox = new JCheckBox(Messages.getString("Main.disableCustomButtons"));
        customBtnsBox.setSelected(up.isDisableCustomButtons());

        themeBox.add(lafBox);
        themeBox.add(new JLabel(" "));
        themeBox.add(new JSeparator());
        themeBox.add(customBtnsBox);
        themeBox.add(new JLabel(" ") {
            {
                setPreferredSize(new Dimension(1, Integer.MAX_VALUE));
            }
        });
        themeBox.alignAll();

        apPane.addTab(Messages.getString("Main.appearancePaneTheme"), themeBox);
        apPane.addTab(Messages.getString("Main.appearancePaneButtons"), apButtonSettingsFull);

        if (up.isDisableCustomButtons()) {
            apPane.setEnabledAt(1, false);
        }

        final JVBoxPanel ivBox = new JVBoxPanel();

        final JCheckBox enableIVHandling = new JCheckBox(Messages.getString("Main.enableIVHandling"));
        final JCheckBox hideIncomingWindows = new JCheckBox(Messages.getString("Main.hideIncomingWindows"));
        final JCheckBox hiddenWindowsResponse = new JCheckBox(Messages.getString("Main.hiddenWindowsResponse"));
        final JCheckBox loadTextures = new JCheckBox(Messages.getString("Main.loadItemTextures"));
        final JCheckBox showWhenInTray = new JCheckBox(Messages.getString("Main.showWindowsInTray"));
        final JCheckBox sendClosePackets = new JCheckBox(Messages.getString("Main.sendClosePackets"));

        enableIVHandling.setToolTipText(Messages.getString("Main.enableIVHandlingToolTip"));
        loadTextures.setToolTipText(Messages.getString("Main.loadItemTexturesToolTip"));
        showWhenInTray.setToolTipText(Messages.getString("Main.showWindowsInTrayToolTip"));
        sendClosePackets.setToolTipText(Messages.getString("Main.sendClosePacketsToolTip"));
        hideIncomingWindows.setToolTipText(Messages.getString("Main.hideIncomingWindowsToolTip"));
        hiddenWindowsResponse.setToolTipText(Messages.getString("Main.hiddenWindowsResponseToolTip"));

        enableIVHandling.setSelected(up.isEnableInventoryHandling());
        loadTextures.setSelected(up.isLoadInventoryTextures());
        showWhenInTray.setSelected(up.isShowWindowsInTray());
        sendClosePackets.setSelected(up.isSendWindowClosePackets());
        hideIncomingWindows.setSelected(up.isHideIncomingWindows());
        hiddenWindowsResponse.setSelected(up.isHiddenWindowsResponse());
        hiddenWindowsResponse.setEnabled(hideIncomingWindows.isSelected());

        hideIncomingWindows.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev2) {
                hiddenWindowsResponse.setEnabled(hideIncomingWindows.isSelected());
            }
        });

        enableIVHandling.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                recDisable(ivBox);
            }

            private void recDisable(final Component ct) {
                if (ct instanceof Container) {
                    setEb(ct);
                    for (final Component cpt : ((Container) ct).getComponents()) {
                        recDisable(cpt);
                    }
                } else {
                    setEb(ct);
                }
            }

            private void setEb(final Component ct) {
                if ((ct instanceof JCheckBox) && !ct.equals(enableIVHandling)) {
                    ct.setEnabled(enableIVHandling.isSelected());
                    if (ct.equals(hiddenWindowsResponse)) {
                        ct.setEnabled(hideIncomingWindows.isSelected() && hideIncomingWindows.isEnabled());
                    }
                }
            }
        });

        ivBox.add(new JPanel() {
            {
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                add(enableIVHandling);
                add(new JButton("?") {
                    {
                        addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                JOptionPane.showOptionDialog(od, Messages.getString("Main.inventoryHandlingHelp"),
                                        Messages.getString("Main.inventoryHandlingHelpTitle"),
                                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                                        new Object[] { Messages.getString("Main.ok") }, 0);
                            }
                        });
                    }
                });
            }
        });
        ivBox.add(loadTextures);
        ivBox.add(showWhenInTray);
        ivBox.add(sendClosePackets);
        ivBox.add(new JSeparator());
        ivBox.add(hideIncomingWindows);
        ivBox.add(new JPanel() {
            {
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                add(Box.createHorizontalStrut(10));
                add(hiddenWindowsResponse);
            }
        });
        ivBox.add(new JSeparator());
        ivBox.add(new JTextPane() {
            {
                setEditable(false);
                setOpaque(false);
            }
        });

        for (final Component ct : ivBox.getComponents())
            if (!(ct instanceof JTextPane) && !ct.equals(enableIVHandling)) {
                ct.setEnabled(enableIVHandling.isSelected());
            }

        ivBox.alignAll();

        final JVBoxPanel gnBox = new JVBoxPanel();

        final JComboBox<Language> languages = new JComboBox<>(Language.values());
        languages.setSelectedItem(up.getAppLanguage());

        final JComboBox<String> unicodeDisplay = new JComboBox<>(
                new String[] { UserPreferences.Constants.UNICODECHARS_KEY_AUTO,
                        UserPreferences.Constants.UNICODECHARS_KEY_FORCE_CUSTOM,
                        UserPreferences.Constants.UNICODECHARS_KEY_FORCE_UNICODE });
        unicodeDisplay.setSelectedItem(up.getUnicodeCharactersMode());

        gnBox.add(new JLabel(Messages.getString("Main.settingsLangChangeLabel")));
        gnBox.add(languages);
        gnBox.add(new JLabel(" "));
        gnBox.add(new JSeparator(SwingConstants.HORIZONTAL));
        gnBox.add(new JLabel(Messages.getString("Main.unicodeDisplayMode")));
        gnBox.add(unicodeDisplay);
        gnBox.add(new JTextPane() {
            {
                setEditable(false);
                setOpaque(false);
            }
        });

        gnBox.alignAll();

        final JVBoxPanel dscBox = new JVBoxPanel();
        final JCheckBox disablePresence = new JCheckBox(Messages.getString("Main.disablePresenceCheckbox"));
        final JCheckBox hideSrv = new JCheckBox(Messages.getString("Main.hideAddressCheckbox"));
        final JCheckBox hideNick = new JCheckBox(Messages.getString("Main.hideNicknameCheckbox"));

        disablePresence.setSelected(up.isDisableDiscordPresence());
        hideSrv.setSelected(up.isHideDiscordServer());
        hideNick.setSelected(up.isHideDiscordNickname());

        dscBox.add(disablePresence);
        dscBox.add(new JSeparator() {
            {
                setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            }
        });
        dscBox.add(hideSrv);
        dscBox.add(hideNick);
        dscBox.add(new JLabel(" "));
        dscBox.add(new JLabel(" ") {
            {
            }
        });
        dscBox.alignAll();

        jtp.add(Messages.getString("Main.settingsTabGeneral"), gnBox);
        jtp.add(Messages.getString("Main.settingsTabAppearance"), apPane);
        jtp.add(Messages.getString("Main.settingsTabTray"), trBox);
        jtp.add(Messages.getString("Main.settingsTabResourcePacks"), rsBox);
        jtp.add(Messages.getString("Main.settingsTabSkins"), skBox);
        jtp.add(Messages.getString("Main.settingsTabProtocol"), pkBox);
        jtp.add(Messages.getString("Main.settingsTabInventory"), ivBox);
        jtp.add(Messages.getString("Main.settingsTabDiscord"), dscBox);
        jtp.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                final JTabbedPane tp = (JTabbedPane) e.getSource();
                if (tp.getSelectedIndex() == 5) {
                    JOptionPane.showOptionDialog(win, Messages.getString("Main.protocolSettingsWarning"),
                            Messages.getString("Main.protocolSettingsWarningTitle"), JOptionPane.OK_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, new Object[] { Messages.getString("Main.ok") }, e);
                }
            }
        });
        b.add(jtp);

        final JButton sOk = new JButton(Messages.getString("Main.ok"));
        final JButton sCancel = new JButton(Messages.getString("Main.settingsCancel"));

        sOk.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final Status rsBehavior = (Status) rPackBehaviorBox.getSelectedItem();
                final boolean showResourcePackMessages = rsPackShowCheck.isSelected();
                final String resourcePackMessage = rsPackMsgText.getText();
                final Position resourcePackMessagePosition = (Position) rsPackMessagePosition.getSelectedItem();

                final SkinRule skinFetchRule = (SkinRule) ruleBox.getSelectedItem();

                final boolean ignoreKeepAlive = ignoreKAPackets.isSelected();
                final boolean ignoreDisconnect = ignoreDSPackets.isSelected();
                final boolean forceLegacyPing = forceLegacySLP.isSelected();
                final String brand = brandField.getText();
                final boolean sendMCBrand = !brand.isEmpty();

                final boolean themeChanged = !lafBox.getSelectedItem().equals(up.getUiTheme())
                        || (customBtnsBox.isSelected() != up.isDisableCustomButtons());

                up.setDisableDiscordPresence(disablePresence.isSelected());
                up.setHideDiscordNickname(hideNick.isSelected());
                up.setHideDiscordServer(hideSrv.isSelected());
                discordIntegr.update();
                discordIntegr.start();
                up.setDisableCustomButtons(customBtnsBox.isSelected());
                up.setUiTheme((String) lafBox.getSelectedItem());
                up.setMaxPacketsOnList((int) maxPacketsOnListField.getValue());
                up.setDisablePacketAnalyzer(disablePacketAnalyzer.isSelected());
                up.setResourcePackBehavior(rsBehavior);
                up.setShowResourcePackMessages(showResourcePackMessages);
                up.setResourcePackMessage(resourcePackMessage.replace("&", "\u00a7"));
                up.setResourcePackMessagePosition(resourcePackMessagePosition);

                up.setSkinFetchRule(skinFetchRule);

                up.setIgnoreKeepAlive(ignoreKeepAlive);
                up.setIgnoreDisconnect(ignoreDisconnect);
                up.setForceLegacySLP(forceLegacyPing);
                up.setAdditionalPing((int) pingField.getValue());
                up.setBrand(brand);
                up.setSendMCBrand(sendMCBrand);

                up.setTrayMessageMode((String) trMessagesMode.getSelectedItem());
                up.setTrayShowDisconnectMessages(showDMessages.isSelected());
                up.setTrayKeyWords(trMessagesKeywords.getListData());

                if (!enableIVHandling.isSelected()) {
                    for (final MinecraftClient cl : clients.values()) {
                        for (final ItemsWindow iw : cl.getOpenWindows().values()) {
                            iw.closeWindow();
                        }
                        cl.getInventory().closeWindow();
                    }
                }

                if ((!enableIVHandling.isSelected() || !loadTextures.isSelected())
                        && ((up.isEnableInventoryHandling() != enableIVHandling.isSelected())
                                || (up.isLoadInventoryTextures() != loadTextures.isSelected()))
                        && SwingItemsWindow.getTexturesSize() > 0) {
                    final int response = JOptionPane.showOptionDialog(od,
                            Messages.getString("Main.inventoryHandlingDisabled"),
                            Messages.getString("Main.inventoryHandlingDisabledTitle"), JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null,
                            new Object[] { Messages.getString("Main.inventoryHandlingDisabledYes"),
                                    Messages.getString("Main.inventoryHandlingDisabledNo") },
                            0);
                    if (response == 0) {
                        SwingItemsWindow.clearTextures(Main.this);
                    }
                }

                if ((enableIVHandling.isSelected() && loadTextures.isSelected())
                        && (up.isEnableInventoryHandling() != enableIVHandling.isSelected())
                        || (up.isLoadInventoryTextures() != loadTextures.isSelected() && loadTextures.isSelected())) {
                    final int response = JOptionPane.showOptionDialog(od, Messages.getString("Main.itemLoadingEnabled"),
                            Messages.getString("Main.itemLoadingEnabledTitle"), JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null,
                            new Object[] { Messages.getString("Main.itemLoadingEnabledYes"),
                                    Messages.getString("Main.itemLoadingEnabledNo") },
                            0);
                    if (response == 0) {
                        od.dispose();
                        SwingItemsWindow.initTextures(Main.this, false);
                        if (clients.size() > 0) {
                            JOptionPane.showOptionDialog(od, Messages.getString("Main.itemTexturesLoaded"),
                                    Messages.getString("Main.itemTexturesLoadedTitle"), JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE, null,
                                    new Object[] { Messages.getString("Main.ok") }, 0);
                        }
                    }
                }

                if (enableIVHandling.isSelected() && !up.isEnableInventoryHandling() && (clients.size() > 0)) {
                    JOptionPane.showOptionDialog(od, Messages.getString("Main.inventoryHandlingEnabled"),
                            Messages.getString("Main.inventoryHandlingEnabledTitle"), JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, new Object[] { Messages.getString("Main.ok") }, 0);
                }

                up.setEnableInventoryHandling(enableIVHandling.isSelected());
                up.setHideIncomingWindows(hideIncomingWindows.isSelected());
                up.setHiddenWindowsResponse(hiddenWindowsResponse.isSelected());
                up.setLoadInventoryTextures(loadTextures.isSelected());
                up.setShowWindowsInTray(showWhenInTray.isSelected());
                up.setSendWindowClosePackets(sendClosePackets.isSelected());

                final boolean langChanged = up.getAppLanguage() != languages.getSelectedItem()
                        || !(up.getUnicodeCharactersMode().equals(unicodeDisplay.getSelectedItem()));
                up.setAppLanguage((Language) languages.getSelectedItem());
                up.setUnicodeCharactersMode((String) unicodeDisplay.getSelectedItem());

                final ColorPreferences cp2 = up.getColorPreferences();
                cp2.setColorDisabledButton(SwingUtils.getHexRGB(apButtonDisabled.getColor()));
                cp2.setColorEnabledButton(SwingUtils.getHexRGB(apButtonEnabled.getColor()));
                cp2.setColorEnabledHoverButton(SwingUtils.getHexRGB(apButtonEnabledHover.getColor()));
                cp2.setColorText(SwingUtils.getHexRGB(apButtonText.getColor()));
                cp2.setDisabledColorText(SwingUtils.getHexRGB(apButtonTextDisabled.getColor()));

                upSaveRunnable.run();
                PlayerSkinCache.getSkincache().clear();

                if (themeChanged) {
                    JOptionPane.showOptionDialog(od, Messages.getString("Main.themeChangedLabel"),
                            Messages.getString("Main.themeChangedDialogTitle"), JOptionPane.OK_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null,
                            new Object[] { Messages.getString("Main.langChangedLabelDialogOptionRestart") }, 0);
                    System.exit(0);
                }

                if (langChanged) {
                    final int response = JOptionPane.showOptionDialog(od, Messages.getString("Main.langChangedLabel"),
                            Messages.getString("Main.langChangedDialogTitle"), JOptionPane.OK_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null,
                            new Object[] { Messages.getString("Main.langChangedLabelDialogOptionRestart"),
                                    Messages.getString("Main.langChangedLabelDialogOptionContinue") },
                            0);
                    if (response == 0) {
                        System.exit(0);
                    }
                }

                od.dispose();
                win.repaint();
            }
        });

        sCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev2) {
                od.dispose();
            }
        });

        final Box sControls = Box.createHorizontalBox();

        sControls.add(sOk);
        sControls.add(sCancel);

        b.add(sControls);
        od.setContentPane(b);
        od.pack();
        SwingUtils.centerWindow(od);
        od.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    private JSplitPane createServerPane(final ServerEntry entry, final String username, final String password,
            final AuthType authType, final Proxy proxy) {
        final JSplitPane fPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        final Box box = Box.createVerticalBox();

        final JTextPane pane = new JTextPane();
        pane.setBackground(new Color(35, 35, 35));
        pane.setForeground(Color.white);
        pane.setEditable(false);
        pane.setFont(mcFont.deriveFont(13.5f));

        final JScrollPane jsc = new JScrollPane(pane);
        jsc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final Box chatControls = Box.createHorizontalBox();

        final JTextField chatInput = new JMinecraftField(Messages.getString("Main.chatField"));
        chatInput.setEnabled(false);

        final JButton chatSend = new JMinecraftButton(Messages.getString("Main.chatSendButton"));
        chatSend.setEnabled(false);
        chatSend.setMargin(new Insets(5, 5, 5, 5));

        for (final Component ct : chatControls.getComponents()) {
            ct.setFont(ct.getFont().deriveFont(13.5f));
        }

        chatControls.add(chatInput);
        chatControls.add(chatSend);
        chatControls.setMaximumSize(new Dimension(SwingUtils.sSize.width, 0));

        final JTextPane hotbar = new JTextPane();
        hotbar.setBackground(new Color(35, 35, 35));
        hotbar.setForeground(Color.white);
        hotbar.setEditable(false);
        hotbar.setFont(mcFont.deriveFont(13.5f));

        final JScrollPane hjsc = new JScrollPane(hotbar);
        hjsc.setMaximumSize(chatControls.getMaximumSize());

        final JTabbedPane controlsTabPane = new JTabbedPane();
//		JScrollPane controlsScrollPane = new JScrollPane(controlsTabPane);

        win.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        fPane.setDividerLocation(0.8);
                    }
                });
            }
        });

        final JVBoxPanel playerBox = new JVBoxPanel();
        final JPanel statisticsContainer = new JPanel();

        final JCheckBox toggleSneak = new JCheckBox(Messages.getString("Main.toggleSneak"));
        toggleSneak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev) {
                try {
                    final MinecraftClient cl = clients.get(fPane);
                    cl.toggleSneaking();
                    toggleSneak.setSelected(cl.isSneaking());
                } catch (final Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        playerBox.add(toggleSneak);

        final JCheckBox toggleSprint = new JCheckBox(Messages.getString("Main.toggleSprint"));
        toggleSprint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent ev) {
                try {
                    final MinecraftClient cl = clients.get(fPane);
                    cl.toggleSprinting();
                    toggleSprint.setSelected(cl.isSprinting());
                } catch (final Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        final JProgressBar healthBar = new JProgressBar(0, 0);
        final JProgressBar foodBar = new JProgressBar(0, 0);

        healthBar.setStringPainted(true);
        foodBar.setStringPainted(true);

        healthBar.setString(Messages.getString("Main.healthBar"));
        foodBar.setString(Messages.getString("Main.foodBar"));

        final JButton[] movementButtons = new JButton[] { new BasicArrowButton(SwingConstants.NORTH),
                new BasicArrowButton(SwingConstants.SOUTH_WEST), new BasicArrowButton(SwingConstants.WEST),
                new BasicArrowButton(SwingConstants.NORTH_WEST), new BasicArrowButton(SwingConstants.SOUTH),
                new BasicArrowButton(SwingConstants.NORTH_EAST), new BasicArrowButton(SwingConstants.EAST),
                new BasicArrowButton(SwingConstants.SOUTH_EAST) };

        final JButton jumpButton = new BasicArrowButton(SwingConstants.NORTH_EAST);
        jumpButton.setEnabled(false);

        final JCheckBox lockPos = new JCheckBox(Messages.getString("Main.lockPlayerPosition"));
        final JSpinner speed = new JSpinner(new SpinnerNumberModel(0.3, 0.1, 1, 0.1));
        final JSpinner blocks = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 0.1));

        SwingUtils.alignSpinner(speed);
        SwingUtils.alignSpinner(blocks);

        final Box speedBox = Box.createHorizontalBox();
        final Box blocksBox = Box.createHorizontalBox();

        speedBox.add(new JLabel(Messages.getString("Main.movementSpeed")));
        blocksBox.add(new JLabel(Messages.getString("Main.distanceToWalk")));
        speedBox.add(speed);
        blocksBox.add(blocks);

        for (int x = 0; x < movementButtons.length; x++) {
            final int direction = x;
            movementButtons[x].addActionListener(new ActionListener() {
                final int directionL = direction;

                @Override
                public void actionPerformed(final ActionEvent arg0) {
                    try {
                        final MinecraftClient cl = clients.get(fPane);
                        cl.move(directionL, (double) speed.getValue(), (double) blocks.getValue(),
                                lockPos.isSelected());
                    } catch (final Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }

        final JPanel movementPanel = new JPanel(new GridLayout(3, 3));
        movementPanel.add(movementButtons[1]);
        movementPanel.add(movementButtons[0]);
        movementPanel.add(movementButtons[7]);
        movementPanel.add(movementButtons[2]);
        movementPanel.add(jumpButton);
        movementPanel.add(movementButtons[6]);
        movementPanel.add(movementButtons[3]);
        movementPanel.add(movementButtons[4]);
        movementPanel.add(movementButtons[5]);

        movementPanel.setMaximumSize(new Dimension(180, 180));
        movementPanel.setPreferredSize(new Dimension(180, 180));

        final JLabel xLabel = new JLabel("X: 0");
        final JLabel yLabel = new JLabel("Y: 0");
        final JLabel zLabel = new JLabel("Z: 0");

        playerBox.add(toggleSneak);
        playerBox.add(toggleSprint);
        playerBox.add(healthBar);
        playerBox.add(foodBar);
        playerBox.add(new JLabel(" "));
        playerBox.add(new JLabel(Messages.getString("Main.playerPosition")));
        playerBox.add(xLabel);
        playerBox.add(yLabel);
        playerBox.add(zLabel);
        playerBox.add(new JLabel(" "));
        playerBox.add(new JLabel(Messages.getString("Main.playerMovement")));
        playerBox.add(lockPos);
        playerBox.add(new JLabel(" "));
        playerBox.add(speedBox);
        playerBox.add(blocksBox);
        playerBox.add(new JLabel(" "));
        playerBox.add(movementPanel);
//        playerBox.add(new JLabel("") {
//            {
//                setPreferredSize(new Dimension(1, Integer.MAX_VALUE));
//            }
//        });
        playerBox.alignAll();

        final Box playerListBox = Box.createVerticalBox();

        final JPlaceholderField filterField = new JMinecraftField(Messages.getString("Main.playerNamesFilter"));
        filterField.setMaximumSize(new Dimension(SwingUtils.sSize.width, 0));

        final JMinecraftPlayerList playerList = new JMinecraftPlayerList(filterField, win, entry.getHost());
        final JScrollPane playerListPane = new JScrollPane(playerList);

        playerList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    final JMinecraftPlayerList pl = (JMinecraftPlayerList) e.getSource();
                    final PlayerInfo pInfo = pl.getSelectedValue();
                    if (pInfo != null && chatInput.isEnabled()) {
                        final String uName = pInfo.getName();
                        String ct = chatInput.getText();
                        final boolean prependSpace = !(ct.isEmpty() || (ct.substring(ct.length() - 1).equals(" ")));
                        if (prependSpace) {
                            ct += " ";
                        }
                        chatInput.setText(ct + uName + " ");
                        chatInput.requestFocus();
                    }
                }
            }
        });

        filterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                playerListPane.getVerticalScrollBar().setValue(0);
                playerList.setListData(playerList.getListData());
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        playerList.repaint();
                    }
                });
            }
        });

        playerListBox.add(filterField);
        playerListBox.add(playerListPane);

        final JVBoxPanel statisticsBox = new JVBoxPanel();
        final JScrollPane statisticsPane = new JScrollPane(statisticsBox);

        final JButton refreshStats = new JButton(Messages.getString("Main.refreshStatsButton"));
        refreshStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final MinecraftClient cl = clients.get(fPane);
                if (cl != null) {
                    try {
                        cl.refreshStatistics();
                    } catch (final IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        statisticsBox.add(refreshStats);
        statisticsBox.add(statisticsContainer);
        statisticsBox.alignAll();

        final JVBoxPanel inventoryBox = new JVBoxPanel();

        final JButton showInventory = new JButton(Messages.getString("Main.showInventoryButton"));
        showInventory.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (!up.isEnableInventoryHandling()) return;

                final MinecraftClient cl = clients.get(fPane);
                ItemsWindow iw = cl.getInventory();
                if (iw instanceof SwingItemsWindow)
                    ((SwingItemsWindow) cl.getInventory()).openWindow(win, up.isSendWindowClosePackets());
            }
        });

        inventoryBox.add(showInventory);
        inventoryBox.alignAll();

        final JVBoxPanel worldBox = new JVBoxPanel();

        final Box timeBox = Box.createHorizontalBox();

        final JLabel timeLabel = new JLabel(Messages.getString("Main.worldTimeLabel"));
        final JLabel timeValueLabel = new JLabel("-:-");

        timeBox.add(timeLabel);
        timeBox.add(timeValueLabel);

        final JScrollPane drawingScroll = new JScrollPane();
        final JScrollBar hbar = drawingScroll.getHorizontalScrollBar();
        final JScrollBar vbar = drawingScroll.getVerticalScrollBar();

        final JButton openRadarBtn = new JButton(Messages.getString("Main.openEntityRadar"));
        openRadarBtn.addActionListener(new ActionListener() {
            JFrame rWin;

            JRadioButtonMenuItem displayNames = new JRadioButtonMenuItem(Messages.getString("Main.erDisplayNames"));
            JRadioButtonMenuItem realNames = new JRadioButtonMenuItem(Messages.getString("Main.erRealNames")) {
                {
                    setSelected(true);
                }
            };
            JRadioButtonMenuItem noNames = new JRadioButtonMenuItem(Messages.getString("Main.erNoNames"));

            JCheckBoxMenuItem displayPlayers = new JCheckBoxMenuItem(Messages.getString("Main.erShowPlayers")) {
                {
                    setSelected(true);
                }
            };
            JCheckBoxMenuItem displayEntities = new JCheckBoxMenuItem(Messages.getString("Main.erShowEntities")) {
                {

                    setSelected(true);
                }
            };

            Entity selectedEntity = null;

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (rWin != null) {
                    rWin.dispose();
                    rWin = null;
                }
                rWin = new JFrame("Entity Radar: " + username + " (" + selectedServer.getName() + ")");
                rWin.setAlwaysOnTop(true);
                rWin.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                final ButtonGroup displayNamesGroup = new ButtonGroup();
                displayNamesGroup.add(displayNames);
                displayNamesGroup.add(realNames);
                displayNamesGroup.add(noNames);

                final JMenuBar radarBar = new JMenuBar();

                final JMenu radarViewMenu = new JMenu(Messages.getString("Main.erMenuView"));
                final JMenu radarViewDisplayNames = new JMenu(Messages.getString("Main.erOpPlayerNames"));
                final JMenu radarViewEntities = new JMenu(Messages.getString("Main.erOpEntities"));

                radarViewDisplayNames.add(displayNames);
                radarViewDisplayNames.add(realNames);
                radarViewDisplayNames.add(noNames);

                radarViewEntities.add(displayPlayers);
                radarViewEntities.add(displayEntities);

                radarViewMenu.add(new JCheckBoxMenuItem(Messages.getString("Main.erOpAlwaysOnTop")) {
                    {
                        setSelected(rWin.isAlwaysOnTop());
                        addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                rWin.setAlwaysOnTop(isSelected());
                            }
                        });
                    }
                });
                radarViewMenu.add(radarViewDisplayNames);
                radarViewMenu.add(radarViewEntities);
                radarBar.add(radarViewMenu);

                final JPanel drawingPanel = new JPanel() {

                    Toolkit tk = Toolkit.getDefaultToolkit();

                    private int scale = 10;
                    private int scaleTimer = 0;

                    Color background = new Color(0, 50, 0);
                    Color backgroundLines = new Color(0, 100, 0);
                    Color mainPlayerColor = new Color(0, 255, 0);
                    Color playerColor = new Color(255, 255, 0);

                    MinecraftClient client = clients.get(fPane);

                    {
                        addMouseMotionListener(new MouseMotionAdapter() {

                            @Override
                            public void mouseMoved(final MouseEvent e) {
                                selectedEntity = null;
                            }
                        });
                        addMouseWheelListener(new MouseWheelListener() {

                            @Override
                            public void mouseWheelMoved(final MouseWheelEvent e) {
                                if (e.getWheelRotation() < 0) {
                                    increaseScale();
                                } else {
                                    decreaseScale();
                                }
                            }
                        });

                        rWin.addKeyListener(new KeyListener() {

                            private boolean isCtrlDown = false;

                            @Override
                            public void keyTyped(final KeyEvent e) {
                            }

                            @Override
                            public void keyReleased(final KeyEvent e) {
                                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                                    isCtrlDown = false;
                                }
                            }

                            @Override
                            public void keyPressed(final KeyEvent e) {
                                switch (e.getKeyCode()) {
                                    case KeyEvent.VK_CONTROL: {
                                        isCtrlDown = true;
                                        break;
                                    }
                                    case KeyEvent.VK_PLUS:
                                    case KeyEvent.VK_EQUALS: {
                                        if (isCtrlDown) {
                                            increaseScale();
                                        }
                                        break;
                                    }
                                    case KeyEvent.VK_MINUS: {
                                        if (isCtrlDown) {
                                            decreaseScale();
                                        }
                                        break;
                                    }
                                    default:
                                        break;
                                }
                            }
                        });
                    }

                    private void increaseScale() {
                        if (scale < 20) {
                            scale++;
                        }
                        scaleTimer = 30;
                    }

                    private void decreaseScale() {
                        if (scale > 1) {
                            scale--;
                        }
                        scaleTimer = 30;
                    }

                    @Override
                    protected void paintComponent(final Graphics g) {
                        final int width = getWidth();
                        final int height = getHeight();
                        g.setColor(background);
                        g.fillRect(0, 0, width, height);
                        g.setFont(mcFont.deriveFont((float) 12));

                        final int centerX = width / 2;
                        final int centerZ = height / 2;

                        g.setColor(backgroundLines);
                        int i = 0;
                        while (i < width) {
                            i += 5 * scale;
                            g.drawLine(i, 0, i, height);
                            g.drawLine(0, i, width, i);
                        }

                        if (scaleTimer > 0) {
                            scaleTimer -= 1;
                            g.setColor(mainPlayerColor);
                            g.drawString("Scale: x" + scale, 5 + hbar.getValue(), 25 + vbar.getValue());
                        }

                        for (final int id : client.getStoredEntities().keySet().toArray(new Integer[0])) {
                            final Entity entity = client.getEntity(id);
                            if (entity != null) {
                                int x = (int) (client.getX() - entity.getX());
                                int z = (int) (client.getZ() - entity.getZ());
                                x *= scale;
                                z *= scale;
                                x += centerX;
                                z += centerZ;
                                final Point mouse = getMousePosition();
                                if (!(entity instanceof Player)) {
                                    if (displayEntities.isSelected()) {
                                        if (mouse != null && new Rectangle(x - 4, z - 4, 8, 8).contains(mouse)
                                                && selectedEntity == null) {
                                            selectedEntity = entity;
                                        }
                                        drawMob(x, z, g, Color.red, entity.equals(selectedEntity),
                                                client.isTracked(entity));
                                    }
                                } else if (displayPlayers.isSelected()) {
                                    if (mouse != null && new Rectangle(x - 12, z - 12, 24, 24).contains(mouse)
                                            && selectedEntity == null) {
                                        selectedEntity = entity;
                                    }
                                    drawPlayer(x, z, entity.getUid(), g, playerColor, entity.equals(selectedEntity),
                                            client.isTracked(entity));
                                }
                            }
                        }

                        if (client != null) {
                            drawPlayer(centerX, centerZ, client.getUid(), g, mainPlayerColor, false, false);
                        }

                        try {
                            Thread.sleep(1000 / 100);
                        } catch (final InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        tk.sync();
                        repaint();
                    }

                    private byte frameTimer = 0;

                    private final Color darkRed = new Color(55, 0, 0);
                    private final Color red = new Color(255, 0, 0);

                    private void drawPlayer(final int x, final int y, final UUID uid, final Graphics g,
                            final Color color, final boolean selected, final boolean tracked) {
                        final BufferedImage image = PlayerSkinCache.getHead(uid);
                        if (image != null) {
                            if (selected || tracked) {
                                if (selected) {
                                    g.setColor(Color.white);
                                } else if (tracked) {
                                    if (client.isEntityAttacking()) {
                                        g.setColor(frameTimer < 5 ? red : darkRed);
                                    } else {
                                        g.setColor(frameTimer < 5 ? Color.white : Color.gray);
                                    }
                                    frameTimer++;
                                    if (frameTimer > 10) {
                                        frameTimer = 0;
                                    }
                                }
                                g.fillRect(x - 14, y - 14, 28, 28);
                            }
                            g.drawImage(image, x - 12, y - 12, 24, 24, null);
                        }

                        final PlayerInfo info = client.getPlayersTabList().get(uid);
                        if (info != null) {
                            if (!noNames.isSelected()) {
                                final String name = realNames.isSelected() || info.getDisplayName() == null
                                        ? info.getName()
                                        : ChatMessages.removeColors(info.getDisplayName());
                                g.setColor(color);
                                g.drawString(name, x - (g.getFontMetrics().stringWidth(name) / 2), y - 16);
                            }
                        }
                    }

                    private void drawMob(final int x, final int y, final Graphics g, final Color color,
                            final boolean selected, final boolean tracked) {

                        if (selected || tracked) {
                            if (selected) {
                                g.setColor(Color.white);
                            } else if (tracked) {
                                if (client.isEntityAttacking()) {
                                    g.setColor(frameTimer < 5 ? red : darkRed);
                                } else {
                                    g.setColor(frameTimer < 5 ? Color.white : Color.gray);
                                }
                                frameTimer++;
                                if (frameTimer > 10) {
                                    frameTimer = 0;
                                }
                            }
                            g.fillRect(x - 6, y - 6, 12, 12);
                        }
                        g.setColor(color);
                        g.fillRect(x - 4, y - 4, 8, 8);
                    }
                };

                drawingPanel.addMouseListener(new MouseAdapter() {

                    MinecraftClient client = clients.get(fPane);
                    Entity currentEntity = null;

                    ActionListener lClick = new ActionListener() {

                        @Override
                        public void actionPerformed(final ActionEvent e) {
                            client.trackEntity(currentEntity, false);
                        }
                    };

                    JMenuItem currentEntityItem = new JMenuItem("") {
                        {
                            setEnabled(false);
                        }
                    };

                    JPopupMenu entityMenu = new JPopupMenu("") {
                        {
                            add(currentEntityItem);
                            add(new JMenuItem("Track Entity") {
                                {
                                    setFont(getFont().deriveFont(Font.BOLD));
                                    addActionListener(lClick);
                                }
                            });
                            add(new JMenuItem("Attack Entity") {
                                {
                                    addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(final ActionEvent e) {
                                            client.trackEntity(currentEntity, true);
                                        }
                                    });
                                }
                            });
                            add(new JMenuItem("Interact with Entity") {
                                {
                                    addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(final ActionEvent e) {
                                            try {
                                                client.interact(currentEntity, UseType.INTERACT);
                                            } catch (final IOException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            });
                            add(new JMenuItem("Hit Entity") {
                                {
                                    addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(final ActionEvent e) {
                                            try {
                                                client.interact(currentEntity, UseType.ATTACK);
                                            } catch (final IOException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    };

                    @Override
                    public void mouseClicked(final MouseEvent e) {
                        currentEntity = selectedEntity;
                        if (currentEntity != null) {
                            final Point scrLoc = e.getPoint();
                            if (e.getButton() == MouseEvent.BUTTON1) {
                                lClick.actionPerformed(new ActionEvent(entityMenu, 0, ""));
                            } else if (e.getButton() == MouseEvent.BUTTON3) {

                                String name;
                                BufferedImage skin = null;
                                if (currentEntity instanceof Player) {
                                    final PlayerInfo inf = client.getPlayersTabList().get(currentEntity.getUid());
                                    if (inf != null) {
                                        name = inf.getName();
                                    } else {
                                        name = currentEntity.getUid().toString();
                                    }

                                    skin = PlayerSkinCache.getHead(currentEntity.getUid());
                                    if (skin != null) {
                                        final BufferedImage img = new BufferedImage(16, 16,
                                                BufferedImage.TYPE_INT_ARGB);
                                        final Graphics g = img.createGraphics();
                                        g.drawImage(skin, 0, 0, img.getWidth(), img.getHeight(), null);
                                        skin = img;
                                    }

                                } else {
                                    name = Integer.toString(currentEntity.getType());
                                }

                                currentEntityItem.setText(name);
                                if (skin != null) {
                                    currentEntityItem.setIcon(new ImageIcon(skin));
                                } else {
                                    currentEntityItem.setIcon(null);
                                }

                                entityMenu.show(drawingPanel, (int) scrLoc.getX(), (int) scrLoc.getY());
                            }
                        }
                    }
                });

                drawingPanel.setPreferredSize(new Dimension(2048, 2048));
                drawingScroll.setViewportView(drawingPanel);
                drawingScroll.setPreferredSize(new Dimension(512, 512));

                rWin.setJMenuBar(radarBar);
                rWin.setContentPane(drawingScroll);
                rWin.pack();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        final Rectangle bounds = drawingScroll.getViewport().getViewRect();
                        hbar.setValue((hbar.getMaximum() - bounds.width) / 2);
                        vbar.setValue((vbar.getMaximum() - bounds.height) / 2);
                    }
                });

                SwingUtils.centerWindow(rWin);
                rWin.setVisible(true);
            }
        });

        final Box trackingBox = Box.createHorizontalBox();

        final JTextField trackingField = new JTextField();
        trackingField.setEditable(false);
        final JButton stopTrackingBtn = new JButton("Stop tracking");
        stopTrackingBtn.setEnabled(false);
        stopTrackingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                clients.get(fPane).setTrackedEntity(-1, false);
            }
        });

        trackingBox.add(new JLabel(Messages.getString("Main.trackingCurrent") + ": "));
        trackingBox.add(trackingField);

        final JVBoxPanel autoTrackBox = new JVBoxPanel();

        final JCheckBox autoTrackEnable = new JCheckBox(Messages.getString("Main.trackingEnable"));
        autoTrackEnable.setFont(autoTrackEnable.getFont().deriveFont(Font.BOLD));
        final JCheckBox autoTrackPlayers = new JCheckBox(Messages.getString("Main.trackingPlayers"));
        final JCheckBox autoTrackEntities = new JCheckBox(Messages.getString("Main.trackingEntities"));

        final JVBoxPanel autoAttackPanel = new JVBoxPanel();
        final JVBoxPanel attackPanel = new JVBoxPanel();

        final JRadioButton attackHit = new JRadioButton(Messages.getString("Main.attackHit"));
        attackHit.setSelected(true);
        final JRadioButton attackUse = new JRadioButton(Messages.getString("Main.attackItem"));

        final ButtonGroup attackGroup = new ButtonGroup();
        attackGroup.add(attackHit);
        attackGroup.add(attackUse);

        final JCheckBox autoAttackEnable = new JCheckBox(Messages.getString("Main.attackingEnable"));
        autoAttackEnable.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (autoAttackEnable.isSelected()) {
                    autoTrackEnable.setSelected(true);
                }
            }
        });
        autoAttackEnable.setFont(autoAttackEnable.getFont().deriveFont(Font.BOLD));
        final JSpinner autoAttackRate = new JSpinner(new SpinnerNumberModel(25, 0, 1000, 1));
        SwingUtils.alignSpinner(autoAttackRate);

        final JSpinner attackUseDuration = new JSpinner(new SpinnerNumberModel(20, 0, 1000, 1));
        SwingUtils.alignSpinner(attackUseDuration);
        final JSpinner attackUseRange = new JSpinner(new SpinnerNumberModel(20, 0, 1000, 1));
        SwingUtils.alignSpinner(attackUseRange);

        autoAttackPanel.add(autoAttackEnable);
        attackPanel.add(attackHit);
        attackPanel.add(attackUse);
        attackPanel.add(new JLabel(Messages.getString("Main.attackItemDuration") + ":"));
        attackPanel.add(attackUseDuration);
        attackPanel.add(new JLabel(Messages.getString("Main.attackItemRange") + ":"));
        attackPanel.add(attackUseRange);
        attackPanel.add(new JLabel(Messages.getString("Main.attackRate") + ":"));
        attackPanel.add(autoAttackRate);

        attackPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("Main.attackSettings")));
        attackPanel.alignAll();
        autoAttackPanel.setBorder(BorderFactory.createTitledBorder(Messages.getString("Main.autoAttack")));
        autoAttackPanel.alignAll();

        autoTrackBox.add(autoTrackEnable);
        autoTrackBox.add(autoTrackPlayers);
        autoTrackBox.add(autoTrackEntities);
        autoTrackBox.add(autoAttackPanel);
        autoTrackBox.setBorder(BorderFactory.createTitledBorder(Messages.getString("Main.autoTracking")));
        autoTrackBox.alignAll();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            }
        });
        worldBox.add(timeBox);
        worldBox.add(new JLabel(" "));
        worldBox.add(openRadarBtn);
        worldBox.add(new JLabel(" "));
        worldBox.add(trackingBox);
        worldBox.add(stopTrackingBtn);
        worldBox.add(new JLabel(" "));
        worldBox.add(autoTrackBox);
        worldBox.add(attackPanel);
        worldBox.add(new JLabel("") {
            {
                setPreferredSize(new Dimension(1, Integer.MAX_VALUE));
            }
        });
        worldBox.alignAll();

        final JVBoxPanel autoMsgBox = new JVBoxPanel();

        final JCheckBox autoMsgEnable = new JCheckBox(Messages.getString("Main.enabled"));

        final JSpinner autoMsgDelay = new JSpinner(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
        SwingUtils.alignSpinner(autoMsgDelay);
        final JSpinner autoMsgInterval = new JSpinner(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1));
        SwingUtils.alignSpinner(autoMsgInterval);

        final ChangeListener cls = new ChangeListener() {
            boolean permitted = false;

            @Override
            public void stateChanged(final ChangeEvent e) {
                final JSpinner src = (JSpinner) e.getSource();
                final int val = (int) src.getValue();
                if (val < 1 && !permitted) {
                    src.setValue(1);
                    final String msg = Messages.getString("Main.autoValWarning");
                    final int resp = JOptionPane.showOptionDialog(win, msg,
                            Messages.getString("Main.autoValWarningTitle"), JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE, null,
                            new Object[] { Messages.getString("Main.yes"), Messages.getString("Main.no") }, 0);
                    if (resp == 0) {
                        permitted = true;
                    }
                } else if (val > 1 && permitted) {
                    permitted = false;
                }
            }
        };

        autoMsgDelay.addChangeListener(cls);
        autoMsgInterval.addChangeListener(cls);

        final JRadioButton intSeconds = new JRadioButton(Messages.getString("Main.seconds"));
        intSeconds.setSelected(true);
        final JRadioButton intMinutes = new JRadioButton(Messages.getString("Main.minutes"));
        final ButtonGroup gp = new ButtonGroup();
        gp.add(intMinutes);
        gp.add(intSeconds);

        final JMemList<String> autoMessages = new JMemList<String>();
        final JScrollPane autoMsgPane = new JScrollPane(autoMessages);

        final JButton addMsg = new JButton("+");
        final JButton removeMsg = new JButton("-");
        final JButton downMsg = new JButton("v");
        final JButton upMsg = new JButton("^");

        removeMsg.setEnabled(false);
        upMsg.setEnabled(false);
        downMsg.setEnabled(false);

        autoMessages.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    final JMemList<String> ls = (JMemList<String>) e.getSource();

                    final boolean eb = ls.getSelectedIndex() != -1 && ls.getSelectedValue() != null;
                    removeMsg.setEnabled(eb);
                    upMsg.setEnabled(eb && ls.getSelectedIndex() > 0);
                    downMsg.setEnabled(eb && ls.getSelectedIndex() < ls.getListData().length - 1);
                }
            }
        });

        upMsg.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (autoMessages.getSelectedIndex() > 0) {
                    final String[] ld = autoMessages.getListData();
                    final int x = autoMessages.getSelectedIndex();
                    final String bf = ld[x - 1];
                    ld[x - 1] = ld[x];
                    ld[x] = bf;
                    autoMessages.setListData(ld);
                    autoMessages.setSelectedIndex(x - 1);
                }
            }
        });

        downMsg.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (autoMessages.getSelectedIndex() < autoMessages.getListData().length - 1) {
                    final String[] ld = autoMessages.getListData();
                    final int x = autoMessages.getSelectedIndex();
                    final String bf = ld[x + 1];
                    ld[x + 1] = ld[x];
                    ld[x] = bf;
                    autoMessages.setListData(ld);
                    autoMessages.setSelectedIndex(x + 1);
                }
            }
        });

        removeMsg.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (autoMessages.getSelectedIndex() != -1) {
                    final int z = autoMessages.getSelectedIndex();
                    final String[] ld = autoMessages.getListData();
                    final List<String> ls = new ArrayList<>();
                    for (int x = 0; x < ld.length; x++)
                        if (x != z) {
                            ls.add(ld[x]);
                        }
                    autoMessages.setListData(ls.toArray(new String[ls.size()]));
                    autoMessages.setSelectedIndex(
                            z >= autoMessages.getListData().length ? autoMessages.getListData().length - 1 : z);
                }
            }
        });

        addMsg.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JVBoxPanel autoMsgAddPanel = new JVBoxPanel();
                autoMsgAddPanel.add(new JLabel(Messages.getString("Main.autoMsgAddLabel")));

                final JPlaceholderField jpf = new JPlaceholderField(Messages.getString("Main.message"));
                autoMsgAddPanel.add(jpf);

                autoMsgAddPanel.alignAll();

                final int resp = JOptionPane.showOptionDialog(win, autoMsgAddPanel,
                        Messages.getString("Main.autoMsgAddTitle"), JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, 0);

                if (resp == 0 && !jpf.getText().replace(" ", "").isEmpty()) {
                    String[] ld = autoMessages.getListData();
                    ld = ld == null ? new String[0] : ld;
                    final String[] nld = new String[ld.length + 1];
                    for (int x = 0; x < ld.length; x++) {
                        nld[x] = ld[x];
                    }
                    nld[nld.length - 1] = jpf.getText();
                    autoMessages.setListData(nld);
                }
            }
        });

        final Box msgCtlBox = Box.createHorizontalBox();
        msgCtlBox.add(addMsg);
        msgCtlBox.add(removeMsg);
        msgCtlBox.add(upMsg);
        msgCtlBox.add(downMsg);

        final ActionListener ac = new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final boolean eb = autoMsgEnable.isSelected();
                for (final Component ct : autoMsgBox.getComponents()) {
                    if (ct instanceof JRadioButton || ct instanceof JSpinner) {
                        ct.setEnabled(eb);
                    }
                }
            }
        };
        autoMsgEnable.addActionListener(ac);

        final Box autoMsgSvCtl = Box.createHorizontalBox();

        final JButton autoMsgSave = new JButton(Messages.getString("Main.save"));
        final JButton autoMsgLoad = new JButton(Messages.getString("Main.load"));

        autoMsgSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JFileChooser fc = new JFileChooser(new File("."));
                fc.setAcceptAllFileFilterUsed(false);
                fc.setApproveButtonText(Messages.getString("Main.save"));
                fc.setDialogTitle(Messages.getString("Main.autoMsgSaveTitle"));
                fc.setSelectedFile(new File("autoMessages.amf"));
                fc.setFileFilter(new FileNameExtensionFilter(Messages.getString("Main.autoMsgFileName"), "amf"));
                final int ret = fc.showSaveDialog(win);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    final File out = IOUtils.forceExtension(fc.getSelectedFile(), ".amf");
                    if (out.exists()) {
                        final int ov = JOptionPane.showOptionDialog(win, Messages.getString("Main.overwriteFile"),
                                Messages.getString("Main.overwriteFileTitle"), JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE, null,
                                new Object[] { Messages.getString("Main.yes"), Messages.getString("Main.no") }, 0);
                        if (ov != 0) return;
                    }
                    try {
                        IOUtils.saveAmfFile(out, autoMessages.getListData());
                    } catch (final IOException e1) {
                        e1.printStackTrace();
                        SwingUtils.showErrorDialog(win, Messages.getString("Main.errorTitle"), e1,
                                Messages.getString("Main.autoMsgSaveError"));
                    }
                }
            }
        });

        autoMsgLoad.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JFileChooser fc = new JFileChooser(new File("."));
                fc.setAcceptAllFileFilterUsed(false);
                fc.setApproveButtonText(Messages.getString("Main.load"));
                fc.setDialogTitle(Messages.getString("Main.autoMsgLoadTitle"));
                fc.setFileFilter(new FileNameExtensionFilter(Messages.getString("Main.autoMsgFileName"), "amf"));
                final int ret = fc.showOpenDialog(win);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    final File f = fc.getSelectedFile();
                    if (f.exists()) {
                        try {
                            autoMessages.setListData(IOUtils.loadAmfFile(f));
                        } catch (final IOException e1) {
                            e1.printStackTrace();
                            SwingUtils.showErrorDialog(win, Messages.getString("Main.errorTitle"), e1,
                                    Messages.getString("Main.autoMsgLoadError"));
                        }
                    }
                }
            }
        });

        autoMsgSvCtl.add(autoMsgSave);
        autoMsgSvCtl.add(autoMsgLoad);

        autoMsgBox.add(autoMsgEnable);
        autoMsgBox.add(new JLabel(" "));
        autoMsgBox.add(new JLabel(Messages.getString("Main.autoMsgDelay")));
        autoMsgBox.add(autoMsgDelay);
        autoMsgBox.add(intSeconds);
        autoMsgBox.add(intMinutes);
        autoMsgBox.add(new JLabel(" "));
        autoMsgBox.add(new JLabel(Messages.getString("Main.autoMsgInterval")));
        autoMsgBox.add(autoMsgInterval);
        autoMsgBox.add(new JLabel(" "));
        autoMsgBox.add(new JLabel(Messages.getString("Main.autoMsgLabel")));
        autoMsgBox.add(msgCtlBox);
        autoMsgBox.add(autoMsgPane);
        autoMsgBox.add(autoMsgSvCtl);
        autoMsgBox.add(new JLabel("") {
            {
                setPreferredSize(new Dimension(1, Integer.MAX_VALUE));
            }
        });

        autoMsgBox.alignAll();

        final JVBoxPanel autoRespBox = new JVBoxPanel();

        final JCheckBox autoRespEnabled = new JCheckBox(Messages.getString("Main.enabled"));

        final JAutoResponseList autoResponses = new JAutoResponseList();
        final JScrollPane autoResponsesPane = new JScrollPane(autoResponses);

        final JButton addAutoResponse = new JButton("+");
        final JButton removeAutoResponse = new JButton("-");
        final JButton editAutoResponse = new JButton(Messages.getString("Main.edit"));

        removeAutoResponse.setEnabled(false);
        editAutoResponse.setEnabled(false);

        autoResponses.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    final JAutoResponseList rs = (JAutoResponseList) e.getSource();
                    final boolean en = rs.getSelectedIndex() != -1;
                    removeAutoResponse.setEnabled(en);
                    editAutoResponse.setEnabled(en);
                }
            }
        });

        final ActionListener acl = new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {

                AutoResponseRule rule = null;

                if (e.getActionCommand().equals("edit")) {
                    rule = autoResponses.getSelectedValue();
                }

                final JVBoxPanel addResponseBox = new JVBoxPanel();

                final JTextField ruleNameField = new JTextField(
                        Messages.getString("Main.autoResponseDefaultRuleName") + " " + Integer.toString(
                                autoResponses.getListData() == null ? 1 : autoResponses.getListData().length + 1));
                final JStringMemList triggers = new JStringMemList();
                final JStringMemList noTriggers = new JStringMemList();
                final JStringMemList effects = new JStringMemList();

                final JScrollPane triggersPane = new JScrollPane(triggers);
                final JScrollPane noTriggersPane = new JScrollPane(noTriggers);
                final JScrollPane effectsPane = new JScrollPane(effects);

                final Box triggersCtl = Box.createHorizontalBox();
                final Box noTriggersCtl = Box.createHorizontalBox();
                final Box effectsCtl = Box.createHorizontalBox();

                final JButton addTrigger = new JButton("+");
                final JButton addNotTrigger = new JButton("+");
                final JButton addEffect = new JButton("+");

                final JButton removeTrigger = new JButton("-");
                final JButton removeNotTrigger = new JButton("-");
                final JButton removeEffect = new JButton("-");

                removeTrigger.setEnabled(false);
                removeNotTrigger.setEnabled(false);
                removeEffect.setEnabled(false);

                final ListSelectionListener sl = new ListSelectionListener() {
                    @Override
                    public void valueChanged(final ListSelectionEvent ev) {
                        final Object source = ev.getSource();
                        final int index = ((JList<String>) source).getSelectedIndex();
                        if (!ev.getValueIsAdjusting()) {
                            if (source.equals(triggers)) {
                                removeTrigger.setEnabled(index != -1);
                            }
                            if (source.equals(noTriggers)) {
                                removeNotTrigger.setEnabled(index != -1);
                            }
                            if (source.equals(effects)) {
                                removeEffect.setEnabled(index != -1);
                            }
                        }
                    }
                };

                triggers.addListSelectionListener(sl);
                noTriggers.addListSelectionListener(sl);
                effects.addListSelectionListener(sl);

                final ActionListener remAc = new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent ev) {
                        final Object src = ev.getSource();
                        if (src.equals(removeTrigger)) {
                            triggers.removeString(triggers.getSelectedIndex());
                        }
                        if (src.equals(removeNotTrigger)) {
                            noTriggers.removeString(noTriggers.getSelectedIndex());
                        }
                        if (src.equals(removeEffect)) {
                            effects.removeString(effects.getSelectedIndex());
                        }
                    }
                };

                final ActionListener addAc = new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent ev) {
                        final Object src = ev.getSource();
                        String label = "";
                        if (src.equals(addTrigger)) {
                            label = Messages.getString("Main.addTrigger");
                        }
                        if (src.equals(addNotTrigger)) {
                            label = Messages.getString("Main.addException");
                        }
                        if (src.equals(addEffect)) {
                            label = Messages.getString("Main.addEffect");
                        }

                        final JTextField jtf = new JTextField();
                        final JVBoxPanel jvb = new JVBoxPanel();
                        jvb.add(new JLabel(label + ":"));
                        jvb.add(jtf);
                        jvb.alignAll();
                        jtf.requestFocus();
                        final int resp = JOptionPane.showOptionDialog(win, jvb, label, JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, null, 0);
                        if (resp == 0 && !jtf.getText().replace(" ", "").isEmpty()) {
                            final String txt = jtf.getText();
                            if (src.equals(addTrigger)) {
                                triggers.addString(txt);
                            }
                            if (src.equals(addNotTrigger)) {
                                noTriggers.addString(txt);
                            }
                            if (src.equals(addEffect)) {
                                effects.addString(txt);
                            }
                        }
                    }
                };

                addTrigger.addActionListener(addAc);
                addNotTrigger.addActionListener(addAc);
                addEffect.addActionListener(addAc);

                removeTrigger.addActionListener(remAc);
                removeNotTrigger.addActionListener(remAc);
                removeEffect.addActionListener(remAc);

                triggersCtl.add(addTrigger);
                triggersCtl.add(removeTrigger);
                noTriggersCtl.add(addNotTrigger);
                noTriggersCtl.add(removeNotTrigger);
                effectsCtl.add(addEffect);
                effectsCtl.add(removeEffect);

                final JRadioButton effectsRandom = new JRadioButton(Messages.getString("Main.random"));
                final JRadioButton effectsOrdered = new JRadioButton(Messages.getString("Main.ordered"));
                final JRadioButton effectsAll = new JRadioButton(Messages.getString("Main.all"));
                effectsRandom.setSelected(true);
                final ButtonGroup bgp = new ButtonGroup();
                bgp.add(effectsOrdered);
                bgp.add(effectsRandom);
                bgp.add(effectsAll);

                final JRadioButton triggersAnd = new JRadioButton(Messages.getString("Main.and"));
                final JRadioButton triggersOr = new JRadioButton(Messages.getString("Main.or"));
                triggersOr.setSelected(true);
                final ButtonGroup tgp = new ButtonGroup();
                tgp.add(triggersAnd);
                tgp.add(triggersOr);

                final Box triggersBox = Box.createHorizontalBox();
                triggersBox.add(triggersOr);
                triggersBox.add(triggersAnd);

                final JSpinner allInterval = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
                allInterval.setEnabled(effectsAll.isSelected());

                final ActionListener radioListener = new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        allInterval.setEnabled(effectsAll.isSelected());
                    }
                };

                effectsRandom.addActionListener(radioListener);
                effectsOrdered.addActionListener(radioListener);
                effectsAll.addActionListener(radioListener);

                final Box radioBox = Box.createHorizontalBox();
                radioBox.add(effectsRandom);
                radioBox.add(effectsOrdered);
                radioBox.add(effectsAll);

                addResponseBox.add(new JLabel("<html><h3>"
                        + Messages.getString(e.getActionCommand().equals("edit") ? "Main.autoResponseEditTitle"
                                : "Main.autoResponseAddTitle")
                        + "</ht></html>"));
                addResponseBox.add(new JLabel(Messages.getString("Main.autoResponseRuleName") + ":"));
                addResponseBox.add(ruleNameField);
                addResponseBox.add(new JLabel(" "));
                addResponseBox.add(new JLabel(Messages.getString("Main.triggers") + ":"));
                addResponseBox.add(triggersPane);
                addResponseBox.add(triggersCtl);
                addResponseBox.add(new JLabel(Messages.getString("Main.type") + ":"));
                addResponseBox.add(triggersBox);
                addResponseBox.add(new JLabel(" "));
                addResponseBox.add(new JLabel(Messages.getString("Main.except") + ":"));
                addResponseBox.add(noTriggersPane);
                addResponseBox.add(noTriggersCtl);
                addResponseBox.add(new JLabel(" "));
                addResponseBox.add(new JLabel(Messages.getString("Main.effects") + ":"));
                addResponseBox.add(effectsPane);
                addResponseBox.add(effectsCtl);
                addResponseBox.add(radioBox);
                addResponseBox.add(new JLabel(Messages.getString("Main.messageInterval") + ":"));
                addResponseBox.add(allInterval);
                addResponseBox.add(new JLabel(" "));
                SwingUtils.alignSpinner(allInterval);

                addResponseBox.alignAll();

                if (rule != null) {
                    final String name = rule.getName();
                    final List<String> rTriggers = rule.getTriggers();
                    final List<String> rExceptions = rule.getExceptions();
                    final List<String> rEffects = rule.getEffects();
                    final EffectType effectType = rule.getType();
                    final TriggerType triggerType = rule.getTriggerType();
                    final int interval = rule.getInterval();

                    ruleNameField.setText(name);
                    triggers.setListData(rTriggers.toArray(new String[rTriggers.size()]));
                    noTriggers.setListData(rExceptions.toArray(new String[rExceptions.size()]));
                    effects.setListData(rEffects.toArray(new String[rEffects.size()]));

                    allInterval.setValue(interval);

                    if (triggerType == TriggerType.AND) {
                        triggersAnd.doClick();
                    }

                    switch (effectType) {
                        case ALL: {
                            effectsAll.doClick();
                            break;
                        }
                        case ORDERED: {
                            effectsOrdered.doClick();
                            break;
                        }
                        default: {
                            effectsRandom.doClick();
                            break;
                        }
                    }
                }

                final Box jj = Box.createHorizontalBox();
                jj.add(new JLabel(" "));
                jj.add(addResponseBox);
                jj.add(new JLabel(" "));
                final JScrollPane sp = new JScrollPane(jj);
                sp.setPreferredSize(new Dimension(SwingUtils.sSize.width / 3, SwingUtils.sSize.height / 2));
                final int resp = JOptionPane.showOptionDialog(win, sp, Messages.getString("Main.autoResponseAddTitle"),
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, 0);
                if (resp == 0) {
                    String ruleName = ruleNameField.getText();
                    final String[] triggersList = triggers.getListData();
                    final String[] exceptionsList = noTriggers.getListData();
                    final String[] effectsList = effects.getListData();
                    final EffectType eType = effectsRandom.isSelected() ? EffectType.RANDOM
                            : effectsOrdered.isSelected() ? EffectType.ORDERED : EffectType.ALL;
                    final int interval = (int) (allInterval.getValue());

                    if (ruleName.replace(" ", "").isEmpty()) {
                        ruleName = Messages.getString("Main.autoResponseDefaultRuleName") + " "
                                + ((autoResponses.getListData() == null) ? "1"
                                        : (Integer.toString(autoResponses.getListData().length + 1)));
                    }

                    if ((triggersList == null || triggersList.length <= 0)
                            || (effectsList == null || effectsList.length <= 0)) {
                        JOptionPane.showOptionDialog(win, Messages.getString("Main.autoResponseAddEmptyError"),
                                Messages.getString("Main.errorTitle"), JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE,
                                null, new Object[] { Messages.getString("Main.ok") }, 0);
                        return;
                    }

                    final TriggerType triggerType = triggersAnd.isSelected() ? TriggerType.AND : TriggerType.OR;

                    if (e.getActionCommand().equals("edit")) {
                        rule.setEffects(effectsList);
                        rule.setExceptions(exceptionsList);
                        rule.setInterval(interval);
                        rule.setName(ruleName);
                        rule.setTriggers(triggersList);
                        rule.setType(eType);
                        rule.setTriggerType(triggerType);
                        autoResponses.repaint();
                    } else {
                        autoResponses.addRule(new AutoResponseRule(ruleName, eType, triggerType, interval, triggersList,
                                exceptionsList, effectsList));
                    }

                }
            }
        };
        addAutoResponse.addActionListener(acl);

        removeAutoResponse.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                autoResponses.removeRule(autoResponses.getSelectedIndex());
            }
        });

        editAutoResponse.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                acl.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "edit"));
            }
        });

        final Box autoResponseCtl = Box.createHorizontalBox();
        autoResponseCtl.add(addAutoResponse);
        autoResponseCtl.add(removeAutoResponse);
        autoResponseCtl.add(editAutoResponse);

        final Box autoResponsesIOCtl = Box.createHorizontalBox();

        final JButton autoRespSave = new JButton(Messages.getString("Main.save"));
        final JButton autoRespLoad = new JButton(Messages.getString("Main.load"));

        autoRespLoad.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JFileChooser fc = new JFileChooser(new File("."));
                fc.setAcceptAllFileFilterUsed(false);
                fc.setDialogTitle(Messages.getString("Main.autoMsgLoadTitle"));
                fc.setFileFilter(new FileNameExtensionFilter(Messages.getString("Main.autoRespFileName"), "arf"));
                fc.setSelectedFile(new File("automaticResponses.arf"));
                fc.setApproveButtonText(Messages.getString("Main.load"));
                final int resp = fc.showOpenDialog(win);
                if (resp == JFileChooser.APPROVE_OPTION) {
                    final File in = fc.getSelectedFile();
                    if (in.exists()) {
                        try {
                            final List<AutoResponseRule> rules = IOUtils.loadArfFile(in);
                            autoResponses.setListData(rules.toArray(new AutoResponseRule[rules.size()]));
                        } catch (final Exception e2) {
                            SwingUtils.showErrorDialog(win, Messages.getString("Main.errorTitle"), e2,
                                    Messages.getString("Main.autoMsgLoadError"));
                            e2.printStackTrace();
                        }
                        autoResponses.repaint();
                    }
                }
            }
        });

        autoRespSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final JFileChooser fc = new JFileChooser(new File("."));
                fc.setAcceptAllFileFilterUsed(false);
                fc.setDialogTitle(Messages.getString("Main.autoMsgSaveTitle"));
                fc.setFileFilter(new FileNameExtensionFilter(Messages.getString("Main.autoRespFileName"), "arf"));
                fc.setSelectedFile(new File("automaticResponses.arf"));
                fc.setApproveButtonText(Messages.getString("Main.save"));
                final int resp = fc.showSaveDialog(win);
                if (resp == JFileChooser.APPROVE_OPTION) {
                    final File out = IOUtils.forceExtension(fc.getSelectedFile(), ".arf");
                    if (out.exists()) {
                        final int rsp = JOptionPane.showOptionDialog(win, Messages.getString("Main.overwriteFile"),
                                Messages.getString("Main.overwriteFileTitle"), JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE, null,
                                new Object[] { Messages.getString("Main.yes"), Messages.getString("Main.no") }, 0);
                        if (rsp != 0) return;
                    }
                    try {
                        IOUtils.writeArfFile(out, autoResponses.getListData());
                    } catch (final Exception e2) {
                        e2.printStackTrace();
                        SwingUtils.showErrorDialog(win, Messages.getString("Main.errorTitle"), e2,
                                Messages.getString("Main.autoMsgSaveError"));
                    }
                }
            }
        });

        autoResponsesIOCtl.add(autoRespSave);
        autoResponsesIOCtl.add(autoRespLoad);

        autoRespBox.add(autoRespEnabled);
        autoRespBox.add(new JLabel(" "));
        autoRespBox.add(new JLabel(Messages.getString("Main.autoResponseRules")));
        autoRespBox.add(autoResponseCtl);
        autoRespBox.add(autoResponsesPane);
        autoRespBox.add(autoResponsesIOCtl);

        autoRespBox.alignAll();

        final JVBoxPanel packetsBtnPane = new JVBoxPanel();
        final JTabbedPane packetsPane = new JTabbedPane();
        final JTabbedPane packetAnalyzerPane = new JTabbedPane();
        final JTabbedPane outAnalyzerPane = new JTabbedPane();
        final JTabbedPane inAnalyzerPane = new JTabbedPane();
        final JTabbedPane allAnalyzerPane = new JTabbedPane();
        final JTabbedPane unknownAnalyzerPane = new JTabbedPane();
        final JTabbedPane searchAnalyzerPane = new JTabbedPane();

        packetsBtnPane.add(new JButton("Open Protocol Manager") {
            {
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        if (pWin != null) {
                            pWin.dispose();
                            pWin = null;
                        }
                        pWin = new JFrame("Packet Manager");
                        if (logoImage != null) {
                            pWin.setIconImage(logoImage);
                        }
                        pWin.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                        pWin.setContentPane(packetsPane);
                        pWin.pack();
                        SwingUtils.centerWindow(pWin);
                        pWin.setVisible(true);
                    }
                });
            }
        });

        final DefaultTableModel inModel = new DefaultTableModel();
        final JTable inTable = new JTable(inModel);
        final TablePacketButton inButtons = initTableColumns(inTable, inModel);

        inButtons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showPacketPanel(inButtons.getPacket());
            }
        });
        inAnalyzerPane.add(new JScrollPane(inTable));

        final DefaultTableModel outModel = new DefaultTableModel();
        final JTable outTable = new JTable(outModel);
        final TablePacketButton outButtons = initTableColumns(outTable, outModel);

        outButtons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showPacketPanel(outButtons.getPacket());
            }
        });
        outAnalyzerPane.add(new JScrollPane(outTable));

        final DefaultTableModel allModel = new DefaultTableModel();
        final JTable allTable = new JTable(allModel);
        final TablePacketButton allButtons = initTableColumns(allTable, allModel);

        allButtons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showPacketPanel(allButtons.getPacket());
            }
        });
        allAnalyzerPane.add(new JScrollPane(allTable));

        final DefaultTableModel unknownModel = new DefaultTableModel();
        final JTable unknownTable = new JTable(unknownModel);
        final TablePacketButton unknownButtons = initTableColumns(unknownTable, unknownModel);

        unknownButtons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showPacketPanel(unknownButtons.getPacket());
            }
        });
        unknownAnalyzerPane.add(new JScrollPane(unknownTable));

        final DefaultTableModel searchModel = new DefaultTableModel();
        final JTable searchTable = new JTable(searchModel);
        final TablePacketButton searchButtons = initTableColumns(searchTable, searchModel);

        searchButtons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showPacketPanel(searchButtons.getPacket());
            }
        });
        searchAnalyzerPane.add(new JScrollPane(searchTable));

        final JVBoxPanel packetAnalyzerBox = new JVBoxPanel();

        final Box packetAnalyzerControlBox = Box.createHorizontalBox();

        final JButton packetAnalyzerClearBtn = new JButton("Clear");
        final JToggleButton packetAnalyzerPauseBtn = new JToggleButton("Pause");
        final JButton packetAnalyzerSearchBtn = new JButton("Search");

        packetAnalyzerSearchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {

                final JVBoxPanel optionsPanel = new JVBoxPanel();
                final JTextField input = new JTextField();
                final JRadioButton byID = new JRadioButton("By ID");
                byID.setSelected(true);
                final JRadioButton byName = new JRadioButton("By Packet Name");
                final ButtonGroup optionsGroup = new ButtonGroup();
                optionsGroup.add(byID);
                optionsGroup.add(byName);
                final Box hBox = Box.createHorizontalBox();
                hBox.add(byID);
                hBox.add(byName);

                optionsPanel.add(new JLabel("Enter search phrase:"));
                optionsPanel.add(input);
                optionsPanel.add(hBox);
                optionsPanel.alignAll();

                final int response = JOptionPane.showOptionDialog(pWin, optionsPanel, "Search...",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        new Object[] { "Ok", "Cancel" }, 0);

                String phrase;
                if (response == 0 && (phrase = input.getText()) != null && !phrase.replace(" ", "").isEmpty()) {
                    searchModel.setRowCount(0);
                    final Object[] dataVector = allModel.getDataVector().toArray(new Object[0]);
                    for (final Object obj : dataVector) {
                        if (obj instanceof Vector<?>) {
                            final Vector<Object> nVector = (Vector<Object>) obj;
                            final String toCompare = byID.isSelected() ? nVector.get(1).toString()
                                    : nVector.get(3).toString();
                            if (toCompare.toLowerCase().contains(phrase.toLowerCase())) {
                                searchModel.addRow(nVector);
                            }
                        }
                    }
                    packetAnalyzerPane.setSelectedIndex(4);
                }
            }
        });

        packetAnalyzerClearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                inModel.setRowCount(0);
                outModel.setRowCount(0);
                allModel.setRowCount(0);
                unknownModel.setRowCount(0);
                searchModel.setRowCount(0);
            }
        });

        packetAnalyzerControlBox.add(packetAnalyzerClearBtn);
        packetAnalyzerControlBox.add(packetAnalyzerPauseBtn);
        packetAnalyzerControlBox.add(packetAnalyzerSearchBtn);

        packetAnalyzerPane.add(Messages.getString("Main.packetAnalyzerIn"), inAnalyzerPane);
        packetAnalyzerPane.add(Messages.getString("Main.packetAnalyzerOut"), outAnalyzerPane);
        packetAnalyzerPane.add(Messages.getString("Main.packetAnalyzerAll"), allAnalyzerPane);
        packetAnalyzerPane.add(Messages.getString("Main.packetAnalyzerUnknown"), unknownAnalyzerPane);
        packetAnalyzerPane.add(Messages.getString("Main.packetAnalyzerSearch"), searchAnalyzerPane);

        packetAnalyzerBox.add(packetAnalyzerControlBox);
        packetAnalyzerBox.add(packetAnalyzerPane);

        packetsPane.addTab(Messages.getString("Main.packetAnalyzerTab"), packetAnalyzerBox);

        final JTabbedPane messagesTabPane = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        messagesTabPane.addTab(Messages.getString("Main.intervalMessagesTab"), autoMsgBox);
        messagesTabPane.addTab(Messages.getString("Main.autoResponsesTab"), autoRespBox);

        controlsTabPane.addTab(Messages.getString("Main.playerListTab"), playerListBox);
        controlsTabPane.addTab(Messages.getString("Main.playerTab"), playerBox);
        controlsTabPane.addTab(Messages.getString("Main.statisticsTab"), statisticsPane);
        controlsTabPane.addTab(Messages.getString("Main.inventoryTab"), inventoryBox);
        controlsTabPane.addTab(Messages.getString("Main.worldTab"), worldBox);
        controlsTabPane.addTab(Messages.getString("Main.autoMessagesTab"), messagesTabPane);
        controlsTabPane.addTab(Messages.getString("Main.packetsTab"), packetsBtnPane);

        fPane.add(box);
        fPane.add(controlsTabPane);

        final Runnable rn = new Runnable() {
            @Override
            public void run() {
//                autoMsgInterval.setMaximumSize(new Dimension(win.getWidth(), 20));
//                autoMsgDelay.setMaximumSize(new Dimension(win.getWidth(), 20));
//                autoAttackRate.setMaximumSize(new Dimension(autoAttackRate.getWidth(), 20));
//                for (final Component ct : speedBox.getComponents()) {
//                    ct.setMaximumSize(new Dimension(ct.getWidth(), 20));
//                }
//                for (final Component ct : blocksBox.getComponents()) {
//                    ct.setMaximumSize(new Dimension(ct.getWidth(), 20));
//                }
//                for (final Component ct : trackingBox.getComponents()) {
//                    ct.setMaximumSize(new Dimension(ct.getWidth(), 20));
//                }
                ac.actionPerformed(null);
            }
        };

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                rn.run();
                fPane.setDividerLocation(0.8);
            }
        });

        box.add(hjsc);
        box.add(jsc);
        box.add(chatControls);
        new Thread(new Runnable() {
            @Override
            public void run() {

                final String host = entry.getHost();
                final boolean forge = entry.isForge();
                final int port = entry.getPort();
                final ForgeMode forgeMode = entry.getForgeMode();
                int protocol = -1;
                switch (entry.getVersion()) {
                    case "Auto": {
                        try {
                            protocol = MinecraftStat.serverListPing(host, port).getProtocol();
                            boolean contains = false;
                            for (final ProtocolNumber num : ProtocolNumber.values())
                                if (num.protocol == protocol) {
                                    contains = true;
                                    break;
                                }
                            if (!contains) {
                                protocol = -2;
                            }
                        } catch (final Exception e) {
                            SwingUtils.appendColoredText(
                                    "\u00a7c" + Messages.getString("Main.connectionFailedChatMessage") + e.toString(),
                                    pane);
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "Always Ask": {
                        protocol = -2;
                        break;
                    }
                    default: {
                        protocol = ProtocolNumber.getForName(entry.getVersion()).protocol;
                        break;
                    }
                }

                if (protocol == -2) {

                    final Box bb = Box.createVerticalBox();

                    final JComboBox<String> pcBox = new JComboBox<>();
                    pcBox.setAlignmentX(Component.LEFT_ALIGNMENT);
                    for (final ProtocolNumber num : ProtocolNumber.values()) {
                        pcBox.addItem(num.name);
                    }

                    bb.add(new JLabel(Messages.getString("Main.chooseMinecraftVersionLabel")));
                    bb.add(pcBox);

                    JOptionPane.showOptionDialog(win, bb, Messages.getString("Main.chooseMinecraftVersionTitle"),
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                            new String[] { Messages.getString("Main.ok") }, null);

                    protocol = ProtocolNumber.getForName((String) pcBox.getSelectedItem()).protocol;
                }

                if (protocol != -1) {
                    final int iprotocol = protocol;
                    try {
                        if (iprotocol >= 755) {
                            showInventory.setEnabled(false);
                        }
                        if (iprotocol >= 393) {
                            controlsTabPane.setEnabledAt(2, false);
                        }
                        final MinecraftClient cl = new MinecraftClient(host, port, iprotocol,
                                forgeMode == ForgeMode.AUTO ? forge : forgeMode == ForgeMode.NEVER == false);
                        clients.put(fPane, cl);

                        if (proxy != null) {
                            cl.setProxy(proxy);
                        }

                        cl.addInputPacketListener(new InternalPacketListener() {

                            @Override
                            public void packetReceived(final Packet packet, final PacketRegistry registry) {
                                if (packetAnalyzerPauseBtn.isSelected()) return;
                                if (up.isDisablePacketAnalyzer()) return;
                                if (packet instanceof UnknownPacket) {
                                    unknownModel.insertRow(0,
                                            new Object[] { packet, "0x" + Integer.toHexString(packet.getID()), "C",
                                                    packet.getClass().getSimpleName(), packet.getSize() });
                                    if (unknownModel.getRowCount() > up.getMaxPacketsOnList()) {
                                        unknownModel.setRowCount(up.getMaxPacketsOnList());
                                    }
                                } else {
                                    inModel.insertRow(0,
                                            new Object[] { packet, "0x" + Integer.toHexString(packet.getID()), "C",
                                                    packet.getClass().getSimpleName(), packet.getSize() });
                                    if (inModel.getRowCount() > up.getMaxPacketsOnList()) {
                                        inModel.setRowCount(up.getMaxPacketsOnList());
                                    }
                                }
                                allModel.insertRow(0, new Object[] { packet, "0x" + Integer.toHexString(packet.getID()),
                                        "C", packet.getClass().getSimpleName(), packet.getSize() });
                                if (allModel.getRowCount() > up.getMaxPacketsOnList()) {
                                    allModel.setRowCount(up.getMaxPacketsOnList());
                                }

                            }
                        });
                        cl.addOutputPacketListener(new InternalPacketListener() {

                            @Override
                            public void packetReceived(final Packet packet, final PacketRegistry registry) {
                                if (packetAnalyzerPauseBtn.isSelected()) return;
                                if (up.isDisablePacketAnalyzer()) return;
                                outModel.insertRow(0, new Object[] { packet, "0x" + Integer.toHexString(packet.getID()),
                                        "S", packet.getClass().getSimpleName(), packet.getSize() });
                                allModel.insertRow(0, new Object[] { packet, "0x" + Integer.toHexString(packet.getID()),
                                        "S", packet.getClass().getSimpleName(), packet.getSize() });
                                if (outModel.getRowCount() > up.getMaxPacketsOnList()) {
                                    outModel.setRowCount(up.getMaxPacketsOnList());
                                }
                                if (allModel.getRowCount() > up.getMaxPacketsOnList()) {
                                    allModel.setRowCount(up.getMaxPacketsOnList());
                                }
                            }
                        });

                        final Thread autoMessagesThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (cl.isConnected()) {
                                    try {
                                        int sleepVal = (int) (autoMsgDelay.getValue()) * 1000;
                                        if (intMinutes.isSelected()) {
                                            sleepVal *= 60;
                                        }
                                        Thread.sleep(sleepVal);
                                        if (autoMsgEnable.isSelected() && autoMessages.getListData() != null) {
                                            final List<String> msgs = new ArrayList<String>();
                                            Collections.addAll(msgs, autoMessages.getListData());
                                            for (int x = 0; x < msgs.size(); x++) {
                                                if (!cl.isConnected()) return;
                                                cl.sendChatMessage(msgs.get(x));
                                                if (x < msgs.size() - 1) {
                                                    try {
                                                        Thread.sleep((int) autoMsgInterval.getValue() * 1000);
                                                    } catch (final Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                if (!autoMsgEnable.isSelected()) {
                                                    break;
                                                }
                                            }
                                        }
                                    } catch (final Exception e2) {
                                    }
                                }
                            }
                        });

                        cl.getPlayersTabList().addChangeListener(new MapChangeListener<UUID, PlayerInfo>() {
                            @Override
                            public void itemRemoved(final Object key, final PlayerInfo value,
                                    final HashMap<UUID, PlayerInfo> map) {
                                final List<PlayerInfo> pl = new ArrayList<PlayerInfo>();
                                for (final UUID ukey : map.keySet()) {
                                    pl.add(map.get(ukey));
                                }
                                if (pl.size() <= 0) return;
                                PlayerInfo[] infs = new PlayerInfo[pl.size()];
                                infs = pl.toArray(infs);
                                playerList.setListData(infs);
                            }

                            @Override
                            public void itemAdded(final UUID key, final PlayerInfo value,
                                    final HashMap<UUID, PlayerInfo> map) {
                                final List<PlayerInfo> pl = new ArrayList<PlayerInfo>();
                                for (final UUID ukey : map.keySet()) {
                                    pl.add(map.get(ukey));
                                }
                                if (pl.size() <= 0) return;
                                PlayerInfo[] infs = new PlayerInfo[pl.size()];
                                infs = pl.toArray(infs);
                                playerList.setListData(infs);
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        playerList.repaint();
                                    }
                                });
                            }
                        });
                        cl.addClientListener(new ClientListener() {

                            final JTextPane jtp = pane;
                            final JTextPane hjtp = hotbar;

                            @Override
                            public boolean messageReceived(final String message, final Position pos,
                                    MinecraftClient cl) {
                                if (pos == Position.HOTBAR) {
                                    hjtp.setText("");
                                    SwingUtils.appendColoredText(message, hjtp);
                                } else {
                                    if (trayIcon != null) {
                                        boolean shouldDisplay = (up.getTrayMessageMode()
                                                .equals(Constants.TRAY_MESSAGES_KEY_ALWAYS))
                                                || (up.getTrayMessageMode().equals(Constants.TRAY_MESSAGES_KEY_MENTION)
                                                        && message.toLowerCase()
                                                                .contains(cl.getUsername().toLowerCase()));
                                        if (!shouldDisplay && up.getTrayMessageMode()
                                                .equals(Constants.TRAY_MESSAGES_KEY_KEYWORD)) {
                                            final String[] keyWords = up.getTrayKeyWords();
                                            if (keyWords != null) {
                                                for (final String keyWord : keyWords)
                                                    if (message.toLowerCase().contains(keyWord.toLowerCase())) {
                                                        shouldDisplay = true;
                                                    }
                                            }
                                        }

                                        if (shouldDisplay) {
                                            trayLastMessageType = 0;
                                            trayLastMessageSender = cl;
                                            final String ttext = ChatMessages.removeColors(message);
                                            trayIcon.displayMessage(
                                                    cl.getHost() + ":" + cl.getPort() + " (" + cl.getUsername() + ")",
                                                    ttext, MessageType.NONE);
                                        }
                                    }

                                    SwingUtils.appendColoredText(message + "\r\n", jtp);
                                    if (autoRespEnabled.isSelected() && autoResponses.getListData() != null) {
                                        final String rText = ChatMessages.removeColors(message);
                                        for (final AutoResponseRule rule : autoResponses.getListData()) {
                                            final String[] matches = rule.match(rText);
                                            if (matches != null && matches.length > 0) {
                                                final int interval = rule.getInterval() * 1000;
                                                final Timer tm = new Timer(true);
                                                tm.scheduleAtFixedRate(new TimerTask() {
                                                    String[] localM = matches;
                                                    int index = 0;

                                                    @Override
                                                    public void run() {
                                                        if (!cl.isConnected()) {
                                                            cl.close();
                                                            cancel();
                                                        }
                                                        try {
                                                            cl.sendChatMessage(localM[index]);
                                                            index++;
                                                            if (index >= localM.length) {
                                                                cancel();
                                                            }
                                                        } catch (final Exception e) {
                                                            e.printStackTrace();
                                                            for (final ClientListener cll : cl
                                                                    .getClientListeners(true)) {
                                                                cll.disconnected(e.toString(), cl);
                                                            }
                                                            cl.close();
                                                        }
                                                    }
                                                }, 250, interval);
                                            }
                                        }
                                    }
                                    try {
                                        Thread.sleep(10);
                                    } catch (final InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            jsc.repaint();
                                        }
                                    });

                                    if (!jsc.getVerticalScrollBar().getValueIsAdjusting()) {
                                        jsc.getVerticalScrollBar()
                                                .setValue(jsc.getVerticalScrollBar().getMaximum() * 2);
                                    }
                                }
                                return false;
                            }

                            @Override
                            public void disconnected(final String reason, MinecraftClient cl) {
                                autoMessagesThread.interrupt();
                                SwingUtils.appendColoredText(
                                        "\u00a7c" + Messages.getString("Main.connectionLostChatMessage") + ": \r\n"
                                                + reason + "\r\n",
                                        jtp);

                                if (trayIcon != null && up.isTrayShowDisconnectMessages()
                                        && !reason.equals(Messages.getString("Main.trayClosedReason"))) {
                                    trayLastMessageType = 1;
                                    final String ttext = Messages.getString("Main.connectionLostTrayMessage")
                                            + ChatMessages.removeColors(reason);
                                    trayIcon.displayMessage(
                                            cl.getHost() + ":" + cl.getPort() + " (" + cl.getUsername() + ")", ttext,
                                            MessageType.ERROR);
                                    final PopupMenu pm = trayIcon.getPopupMenu();
                                    for (int x1 = 0; x1 < pm.getItemCount(); x1++) {
                                        final MenuComponent ct = pm.getItem(x1);
                                        if (ct instanceof Menu) {
                                            final Menu cm = (Menu) ct;
                                            final String lbl = cm.getLabel();
                                            if (lbl.equals(cl.getHost() + ":" + cl.getPort())) {
                                                for (final int y1 = 0; y1 < cm.getItemCount(); x1++) {
                                                    final Menu pmenu = (Menu) cm.getItem(y1);
                                                    if (pmenu.getLabel().equals(cl.getUsername())) {
                                                        cm.remove(y1);
                                                    }
                                                }
                                            }
                                            if (cm.getItemCount() == 0) {
                                                pm.remove(cm);
                                            }
                                        }
                                    }
                                }

                                showInventory.setEnabled(false);
                                for (final Component ct : chatControls.getComponents()) {
                                    ct.setEnabled(false);
                                }
                                for (int x = 0; x < tabPane.getTabCount(); x++) {
                                    final Component ct = tabPane.getTabComponentAt(x);
                                    if (ct == null) {
                                        continue;
                                    }
                                    if (ct.getName().equals(entry.getHost() + "_" + entry.getName() + "_" + username)
                                            && (ct instanceof Box)) {
                                        final Box ctb = (Box) ct;
                                        for (final Component ctt : ctb.getComponents())
                                            if (ctt instanceof JLabel) {
                                                ctt.setForeground(Color.gray);
                                            }
                                    }
                                }
                                cl.close();
                                clients.remove(fPane);
                                discordIntegr.update();
                            }

                            @Override
                            public void healthUpdate(final float health, final int food, MinecraftClient cl) {
                                if (health > healthBar.getMaximum()) {
                                    healthBar.setMaximum((int) health);
                                }
                                if (food > foodBar.getMaximum()) {
                                    foodBar.setMaximum(food);
                                }

                                healthBar.setValue((int) health);
                                foodBar.setValue(food);

                                healthBar.setString(
                                        Messages.getString("Main.healthBarText") + Integer.toString((int) health) + "/"
                                                + Integer.toString(healthBar.getMaximum()) + ")");
                                foodBar.setString(Messages.getString("Main.foodBarText") + Integer.toString(food) + "/"
                                        + Integer.toString(foodBar.getMaximum()) + ")");
                            }

                            @Override
                            public void positionChanged(final double x, final double y, final double z,
                                    MinecraftClient cl) {
                                String sx = Double.toString(x);
                                String sy = Double.toString(y);
                                String sz = Double.toString(z);
                                if (sx.contains(".")) {
                                    sx = sx.substring(0, sx.lastIndexOf(".") + 2);
                                }
                                if (sy.contains(".")) {
                                    sy = sy.substring(0, sy.lastIndexOf(".") + 2);
                                }
                                if (sz.contains(".")) {
                                    sz = sz.substring(0, sz.lastIndexOf(".") + 2);
                                }

                                xLabel.setText("X: " + sx);
                                yLabel.setText("Y: " + sy);
                                zLabel.setText("Z: " + sz);
                            }

                            Map<String, Integer> trueValues = new HashMap<String, Integer>();

                            @Override
                            public void statisticsReceived(final Map<String, Integer> values, MinecraftClient cl) {
                                if (values.size() == 0) return;
                                statisticsContainer.removeAll();
                                for (final String key : values.keySet()) {
                                    final String tkey = TranslationUtils.translateKey(key);
                                    if (tkey == key) {
                                        continue;
                                    }
                                    trueValues.put(tkey, values.get(key));
                                }

                                statisticsContainer.setLayout(new GridLayout(trueValues.size(), 2));
                                for (final String key : trueValues.keySet()) {
                                    statisticsContainer.add(new JLabel(key));
                                    statisticsContainer.add(new JLabel("   " + Integer.toString(trueValues.get(key))));
                                }
                                statisticsContainer.revalidate();
                                statisticsContainer.repaint();
                            }

                            @Override
                            public void windowOpened(final int id, final ItemsWindow win, final PacketRegistry reg,
                                    MinecraftClient cl) {
                                if (up.isHideIncomingWindows()) {
                                    if (up.isHiddenWindowsResponse()) {
                                        try {
                                            cl.sendPacket(
                                                    PacketFactory.constructPacket(reg, "ClientCloseWindowPacket", id));
                                        } catch (final Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    return;
                                }
                                if (!up.isShowWindowsInTray() && trayIcon != null) return;
                                if (win instanceof SwingItemsWindow)
                                    ((SwingItemsWindow) win).openWindow(Main.this.win, up.isSendWindowClosePackets());
                            }

                            @Override
                            public void timeUpdated(long time, final long worldAge, MinecraftClient cl) {
                                if (time < 0) {
                                    time = time * -1;
                                }

                                time = time % 24000;

                                int hours = 6 + (int) (time / 1000);
                                if (hours >= 24) {
                                    hours -= 24;
                                }
                                double minutesDouble = time % 1000;
                                minutesDouble = (minutesDouble / 1000) * 60;
                                int minutes = (int) Math.round(minutesDouble);
                                if (minutes == 60) {
                                    minutes = 0;
                                    hours++;
                                }

                                final String timeString = IOUtils.padString(Integer.toString(hours), 2, "0", 1) + ":"
                                        + IOUtils.padString(Integer.toString(minutes), 2, "0", 1);

                                timeValueLabel.setText(timeString);

                            }

                            @Override
                            public void changedTrackedEntity(final int id, MinecraftClient cl) {
                                if (id == -1) {
                                    trackingField.setText("");
                                    stopTrackingBtn.setEnabled(false);
                                    return;
                                }
                                final Entity et = cl.getEntity(id);
                                stopTrackingBtn.setEnabled(true);

                                String customName = null;
                                if (et != null && et instanceof Player) {
                                    final PlayerInfo info = cl.getPlayersTabList().get(et.getUid());
                                    if (info != null) {
                                        customName = info.getName();
                                    }
                                }
                                trackingField
                                        .setText(et != null ? customName != null ? customName : et.getUid().toString()
                                                : Integer.toString(id));
                            }

                            @Override
                            public void entityMoved(final Entity entity, final int id, MinecraftClient cl) {

                                if (autoTrackEnable.isSelected()) {
                                    double closestDistance = Double.MAX_VALUE;
                                    Entity closestEntity = null;
                                    for (final Entity ent : cl.getStoredEntities().values().toArray(new Entity[0])) {
                                        final double dist = cl.distanceTo(ent);
                                        if (dist < closestDistance) {
                                            if ((ent instanceof Player && autoTrackPlayers.isSelected())
                                                    || (!(ent instanceof Player) && autoTrackEntities.isSelected())) {
                                                closestEntity = ent;
                                                closestDistance = dist;
                                            }
                                        }
                                    }
                                    if (closestEntity != null) {
                                        cl.trackEntity(closestEntity, autoAttackEnable.isSelected());
                                    }
                                }

                                if (id == cl.getTrackedEntity()) {
                                    try {
                                        cl.lookAt(entity);
                                    } catch (final IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }

                            private int attackTicks = 0;
                            private boolean isUsing = false;

                            @Override
                            public void tick(MinecraftClient cl) throws IOException {
                                if (cl.isEntityAttacking()) {
                                    attackTicks++;
                                    if (attackTicks > (int) autoAttackRate.getValue()) {
                                        attackTicks = 0;
                                        final Entity entity = cl.getEntity(cl.getTrackedEntity());
                                        if (entity != null) {
                                            if (attackHit.isSelected() && cl.distanceTo(entity) <= 4) {
                                                cl.interact(entity, UseType.ATTACK);
                                            } else if (attackUse.isSelected() && !isUsing
                                                    && cl.distanceTo(entity) <= (int) attackUseRange.getValue()) {
                                                if (cl.getProtocol() > 47) {
                                                    cl.sendPacket(PacketFactory.constructPacket(cl.getReg(),
                                                            "ClientUseItemPacket"));
                                                    isUsing = true;
                                                    new Timer(true).schedule(new TimerTask() {

                                                        @Override
                                                        public void run() {
                                                            try {
                                                                isUsing = false;
                                                                cl.sendPacket(PacketFactory.constructPacket(cl.getReg(),
                                                                        "ClientPlayerDiggingPacket",
                                                                        net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientPlayerDiggingPacket.Status.FINISH_ACTION,
                                                                        0, 0, 0, (byte) 0));
                                                            } catch (final IOException ex) {
                                                                ex.printStackTrace();
                                                            }
                                                        }
                                                    }, ((int) attackUseDuration.getValue()) * 1000 / 20);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        });

                        cl.connect(authType, username, password);
                        discordIntegr.update();

                        try {
                            Thread.sleep(1000);
                        } catch (final InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        autoMessagesThread.start();

                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                fPane.setDividerLocation(0.8);
                            }
                        });

                        playerList.setMcl(cl);
                        PlayerSkinCache.getSkincache().clear();

                        chatInput.addKeyListener(new KeyAdapter() {

                            @Override
                            public void keyPressed(final KeyEvent e) {

                                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                    chatSend.requestFocusInWindow();
                                    chatSend.doClick();
                                    chatInput.requestFocusInWindow();
                                }
                            }
                        });

                        chatSend.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                final String message = chatInput.getText();
                                if (!message.isEmpty()) {
                                    try {
                                        cl.sendChatMessage(message);
                                    } catch (final IOException e1) {
                                        SwingUtils.appendColoredText(
                                                "\u00a7c" + Messages.getString("Main.connectionLostChatMessage2")
                                                        + ": \r\n" + e1.toString(),
                                                pane);
                                        e1.printStackTrace();
                                        for (final Component ct : chatControls.getComponents()) {
                                            ct.setEnabled(false);
                                        }
                                    }
                                    chatInput.setText("");
                                }
                            }
                        });

                        for (final Component ct : chatControls.getComponents()) {
                            ct.setEnabled(true);
                        }

                    } catch (

                    final IOException e) {
                        SwingUtils.appendColoredText("\u00a7c" + Messages.getString("Main.connectionFailedChatMessage2")
                                + "\r\n\r\n" + e.toString(), pane);
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return fPane;
    }

    JFrame pWin = null;

    private TablePacketButton initTableColumns(final JTable table, final DefaultTableModel model) {
        model.addColumn(" ");
        model.addColumn("ID");
        model.addColumn("Direction");
        model.addColumn("Name");
        model.addColumn("Size");

        table.setDefaultEditor(Object.class, null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        final TablePacketButton btn = new TablePacketButton();
        btn.init(table);
        return btn;
    }

    private void showPacketPanel(final Packet packet) {
        final JDialog dialog = new JDialog(pWin, "Packet Information: " + packet.getClass().getSimpleName());
        dialog.setModal(true);

        final JTabbedPane tabs = new JTabbedPane();
        final JVBoxPanel infoPanel = new JVBoxPanel();
        final JVBoxPanel fieldsPanel = new JVBoxPanel();
        final JVBoxPanel dumpPanel = new JVBoxPanel();

        final JTextPane pane = new JTextPane();
        pane.setEditable(false);

        final int compressed = packet.getCompressed();
        final String compressedString = compressed == 1 ? "\u00a74No (Disabled)"
                : compressed == 2 ? "\u00a74No" : compressed == 3 ? "\u00a72Yes" : "Unknown";
        final String encryptedString = packet.isEncrypted() ? "\u00a72Yes" : "\u00a74No";

        pane.setText("Packet Name: " + packet.getClass().getSimpleName() + "\n" + "Packet Class: "
                + packet.getClass().getName() + "\nPacket ID: 0x" + Integer.toHexString(packet.getID()) + "\nRegistry: "
                + packet.getReg().getClass().getSimpleName() + "\nPacket Size: " + packet.getSize() + "\nCompressed: ");
        SwingUtils.appendColoredText(compressedString + "\u00a70\nEncrypted: " + encryptedString, pane);

        infoPanel.add(pane);

        final DefaultTableModel fieldsModel = new DefaultTableModel();
        fieldsModel.addColumn("Name");
        fieldsModel.addColumn("Type");
        fieldsModel.addColumn("Value");
        final JTable fieldsTable = new JTable(fieldsModel);

        try {
            for (final Field field : packet.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                fieldsModel
                        .addRow(new Object[] { field.getName(), field.getType().getSimpleName(), field.get(packet) });
                field.setAccessible(false);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        fieldsPanel.add(new JScrollPane(fieldsTable));

        final JTextArea dumpField = new JTextArea(bytesToHex(packet.getData(false)));
        dumpField.setLineWrap(true);
        dumpField.setWrapStyleWord(true);
        dumpField.setEditable(false);
        dumpField.setFont(dumpField.getFont().deriveFont((float) 15));

        JTextArea sdumpField;
        try {
            sdumpField = new JTextArea(new String(packet.getData(false), "Utf-8"));
        } catch (final UnsupportedEncodingException ex) {
            sdumpField = new JTextArea(new String(packet.getData(false)));
        }
        sdumpField.setLineWrap(true);
        sdumpField.setWrapStyleWord(true);
        sdumpField.setEditable(false);
        sdumpField.setFont(sdumpField.getFont().deriveFont((float) 15));

        final JTabbedPane dumpTabs = new JTabbedPane();

        dumpTabs.add("Hex", new JScrollPane(dumpField));
        dumpTabs.add("String", new JScrollPane(sdumpField));
        dumpPanel.add(dumpTabs);

        tabs.addTab("Info", infoPanel);
        tabs.addTab("Fields", fieldsPanel);
        tabs.addTab("Dump", dumpPanel);
        dialog.setContentPane(tabs);
        dialog.pack();
        SwingUtils.centerWindow(dialog);
        dialog.setVisible(true);
    }

    public ActionListener getConnectionACL() {
        return alis;
    }

    public JFrame getMainWindow() {
        return win;
    }

    public MinecraftClient[] getClients() {
        return clients.values().toArray(new MinecraftClient[0]);
    }

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    public static String bytesToHex(final byte[] bytes) {
        final byte[] hexChars = new byte[bytes.length * 3];
        for (int j = 0; j < bytes.length; j++) {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        final StringBuilder bd = new StringBuilder();
        for (int x = 0; x < hexChars.length; x += 2) {
            if (x < hexChars.length - 1) {
                bd.append(new String(new byte[] { hexChars[x], hexChars[x + 1] }) + " ");
            } else {
                bd.append(new String(new byte[] { hexChars[x] }) + " ");
            }

        }
        return bd.toString();
    }
}
