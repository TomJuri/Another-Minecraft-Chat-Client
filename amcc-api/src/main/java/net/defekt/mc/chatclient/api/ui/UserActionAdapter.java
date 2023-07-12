package net.defekt.mc.chatclient.api.ui;

import net.defekt.mc.chatclient.protocol.MinecraftClient;

import javax.swing.*;
import java.awt.*;

/**
 * An abstract adapter class for {@link UserActionListener}
 *
 * @author Defective4
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
