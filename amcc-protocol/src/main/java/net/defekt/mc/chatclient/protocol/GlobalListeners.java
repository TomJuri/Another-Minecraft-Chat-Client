package net.defekt.mc.chatclient.protocol;

import java.util.ArrayList;
import java.util.List;

import net.defekt.mc.chatclient.protocol.event.ClientListener;
import net.defekt.mc.chatclient.protocol.event.MinecraftPacketListener;

/**
 * Class containing listeners that are by default applied to all new
 * {@link MinecraftClient} instances.
 * 
 * @author Defective4
 *
 */
public class GlobalListeners {
    private static final List<MinecraftPacketListener> packetListeners = new ArrayList<>();
    private static final List<ClientListener> clientListeners = new ArrayList<>();

    /**
     * Register new client listener.<br>
     * Do not call this method directly
     * 
     * @param clistener
     */
    public static void registerListener(final ClientListener clistener) {
        synchronized (clientListeners) {
            clientListeners.add(clistener);
        }
    }

    /**
     * Register new packet listener.<br>
     * Do not call this method directly
     * 
     * @param listener
     */
    public static void registerListener(final MinecraftPacketListener listener) {
        synchronized (packetListeners) {
            if (!packetListeners.contains(listener)) packetListeners.add(listener);
        }
    }

    /**
     * Get all registered packet listeners
     * 
     * @return array of packet listeners
     */
    public static MinecraftPacketListener[] getListeners() {
        return packetListeners.toArray(new MinecraftPacketListener[0]);
    }

    /**
     * Get all registered client listeners
     * 
     * @return array of client listeners
     */
    public static ClientListener[] getClientListeners() {
        return clientListeners.toArray(new ClientListener[0]);
    }
}
