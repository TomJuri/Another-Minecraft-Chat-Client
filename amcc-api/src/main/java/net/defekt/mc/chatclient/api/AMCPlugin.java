package net.defekt.mc.chatclient.api;

import net.defekt.mc.chatclient.protocol.GlobalListeners;
import net.defekt.mc.chatclient.protocol.event.ClientListener;
import net.defekt.mc.chatclient.protocol.event.MinecraftPacketListener;

public abstract class AMCPlugin {

    private PluginDescription description = null;

    public abstract void onEnable();

    public PluginDescription getDescription() {
        return description;
    }

    protected void registerPacketListener(MinecraftPacketListener listener) {
        GlobalListeners.registerListener(listener);
    }

    protected void registerClientListener(ClientListener listener) {
        GlobalListeners.registerListener(listener);
    }
}
