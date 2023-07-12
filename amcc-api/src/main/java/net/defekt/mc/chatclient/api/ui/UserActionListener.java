package net.defekt.mc.chatclient.api.ui;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.event.ClientListener;

import javax.swing.*;
import java.awt.*;

/**
 * Not to be confused with {@link ClientListener}.<br>
 * It's used to listen forr user actions in application's interface.
 *
 * @author Defective4
 * @see GUIComponents
 */
public interface UserActionListener {

    /**
     * Called when a new client is created and added to tabs pane.<br>
     * Please not that thie method is also invoked when the
     * {@link GUIComponents#createClient(String, int, net.defekt.mc.chatclient.protocol.ProtocolEntry, net.defekt.mc.chatclient.protocol.data.ForgeMode, String, String, net.defekt.mc.chatclient.protocol.AuthType, java.net.Proxy, String)}
     * is used.<br>
     * This method is called whether or not the client is connected!.<br>
     * The client may occassionally be <code>null</code>! Prepare to handle that.
     *
     * @param pane
     * @param client
     */
    public void clientCreated(JSplitPane pane, MinecraftClient client);

    /**
     * Invoked when user completely minimizes the application to tray icon.<br>
     * The tray icon may be null.
     *
     * @param icon
     */
    public void minimizedToTray(TrayIcon icon);

    /**
     * Invoked when user returns to normal application view from tray icon.
     */
    public void maximized();
}
