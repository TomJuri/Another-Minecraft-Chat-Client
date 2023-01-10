package net.defekt.mc.chatclient.protocol;

import java.util.ArrayList;
import java.util.List;

import net.defekt.mc.chatclient.protocol.event.MinecraftPacketListener;

public class GlobalListeners {
    private static final List<MinecraftPacketListener> packetListeners = new ArrayList<>();

    public static void registerListener(MinecraftPacketListener listener) {
        synchronized (packetListeners) {
            if (!packetListeners.contains(listener)) packetListeners.add(listener);
        }
    }

    public static MinecraftPacketListener[] getListeners() {
        return packetListeners.toArray(new MinecraftPacketListener[0]);
    }
}
