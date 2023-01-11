package net.defekt.mc.chatclient.api;

import net.defekt.mc.chatclient.api.command.CommandHandler;
import net.defekt.mc.chatclient.protocol.GlobalListeners;
import net.defekt.mc.chatclient.protocol.event.ClientListener;
import net.defekt.mc.chatclient.protocol.event.MinecraftPacketListener;

public abstract class AMCPlugin {

    private PluginDescription description = null;
    private CommandListener ls = new CommandListener();

    public AMCPlugin() {
        registerPacketListener(ls);
    }

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

    protected void registerCommand(String cmd, CommandHandler handler) {
        ls.registerCommand(cmd, handler);
    }

    protected void unregisterCommand(String cmd) {
        ls.unregisterCommand(cmd);
    }
}
