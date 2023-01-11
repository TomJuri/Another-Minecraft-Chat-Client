package net.defekt.mc.chatclient.protocol;

import java.util.ArrayList;
import java.util.List;

import net.defekt.mc.chatclient.protocol.event.ClientListener;
import net.defekt.mc.chatclient.protocol.event.MinecraftPacketListener;

public class GlobalListeners {
    private static final List<MinecraftPacketListener> packetListeners = new ArrayList<>();
    private static final List<ClientListener> clientListeners = new ArrayList<>();

    public static void registerListener(ClientListener clistener) {
        synchronized (clientListeners) {
            clientListeners.add(clistener);
        }
    }

    public static void registerListener(MinecraftPacketListener listener) {
        synchronized (packetListeners) {
            if (!packetListeners.contains(listener)) packetListeners.add(listener);
        }
    }

    public static MinecraftPacketListener[] getListeners() {
        return packetListeners.toArray(new MinecraftPacketListener[0]);
    }

    public static ClientListener[] getClientListeners() {
        return clientListeners.toArray(new ClientListener[0]);
    }
}
