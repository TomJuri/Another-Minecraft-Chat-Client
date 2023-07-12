package net.defekt.mc.chatclient.api.ui;

import net.defekt.mc.chatclient.protocol.AuthType;
import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.ProtocolEntry;
import net.defekt.mc.chatclient.protocol.ProtocolNumber;
import net.defekt.mc.chatclient.protocol.data.ForgeMode;

import javax.swing.*;
import java.awt.*;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Container for all modifiable GUI components of the main application.<br>
 * Please not that the listeners defined here are shared between all active
 * plugins!
 *
 * @author Defective4
 */
@SuppressWarnings("javadoc")
public abstract class GUIComponents {
    private final JFrame mainWindow;
    private final JMenuBar mainMenuBar;
    private final JTabbedPane mainTabPane;
    private final TrayIcon mainTrayIcon;

    private final List<UserActionListener> actionListeners = new ArrayList<>();

    public GUIComponents(final JFrame mainWindow, final JMenuBar mainMenuBar, final JTabbedPane tabPane, final TrayIcon mainTrayIcon) {
        this.mainWindow = mainWindow;
        this.mainMenuBar = mainMenuBar;
        this.mainTabPane = tabPane;
        this.mainTrayIcon = mainTrayIcon;
    }

    public void addActionListener(final UserActionListener listener) {
        if (!actionListeners.contains(listener)) actionListeners.add(listener);
    }

    public void removeActionListener(final UserActionListener listener) {
        actionListeners.remove(listener);
    }

    public UserActionListener[] getActionListeners() {
        return actionListeners.toArray(new UserActionListener[0]);
    }

    /**
     * Get all clients connected and visible in user's interface
     *
     * @return array of Minecraft clients
     */
    public abstract MinecraftClient[] getConnectedClients();

    /**
     * Get main window of the application
     *
     * @return
     */
    public JFrame getMainWindow() {
        return mainWindow;
    }

    /**
     * Get main menu bar (with "File", "Option", etc.) of the application
     *
     * @return
     */
    public JMenuBar getMainMenuBar() {
        return mainMenuBar;
    }

    /**
     * Revalidates all components of the application. You might want to use this if
     * components you add are not displayed correctly.
     */
    public void revalidateAll() {
        mainWindow.revalidate();
        mainMenuBar.revalidate();
        mainTabPane.revalidate();
    }

    /**
     * Get main tab pane (with "Internet" and "Lan" tabs by default) of the
     * application
     *
     * @return
     */
    public JTabbedPane getMainTabPane() {
        return mainTabPane;
    }

    /**
     * Get tray icon of the application.<br>
     *
     * @return tray icon if application is minimized. <code>null</code> otherwise
     */
    public TrayIcon getMainTrayIcon() {
        return mainTrayIcon;
    }

    /**
     * Creates new MinecraftClient, connects it, and adds a widget to main tabs
     * list.
     *
     * @param host
     * @param port
     * @param protocol   protocol of the client. Leave null for automatic detection.
     *                   Otherwise see {@link ProtocolNumber#getForName(String)} or
     *                   {@link ProtocolNumber#getForNumber(int)}
     * @param forge      set to ForgeMode.AUTO if not sure
     * @param username
     * @param password   can be null
     * @param authType
     * @param proxy      set to null for no proxy
     * @param serverName name of the server as displayed in a tab
     * @return new pane containing the connected client. Can later be used to
     * retrieve corresponding {@link MinecraftClient}
     */
    public abstract JSplitPane createClient(String host, int port, ProtocolEntry protocol, ForgeMode forge, final String username, final String password, final AuthType authType, final Proxy proxy, String serverName);

    /**
     * Get {@link MinecraftClient} from a pane previously created with
     * {@link #createClient(String, int, ProtocolEntry, ForgeMode, String, String, AuthType, Proxy, String)}
     *
     * @param pane
     * @return client associated with pane, <code>null</code> if none
     */
    public abstract MinecraftClient getClient(JSplitPane pane);
}
