package net.defekt.mc.chatclient.api.ui;

import java.awt.TrayIcon;

import javax.swing.JSplitPane;

import net.defekt.mc.chatclient.protocol.MinecraftClient;

/**
 * An abstract adapter class for {@link UserActionListener}
 * 
 * @author Defective4
 *
 */
public abstract class UserActionAdapter implements UserActionListener {

    @Override
    public void clientCreated(final JSplitPane pane, final MinecraftClient client) {
    }

    @Override
    public void minimizedToTray(final TrayIcon icon) {
    }

    @Override
    public void maximized() {
    }

}
