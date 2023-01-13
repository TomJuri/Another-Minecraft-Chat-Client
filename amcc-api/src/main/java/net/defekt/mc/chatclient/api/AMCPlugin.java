package net.defekt.mc.chatclient.api;

import net.defekt.mc.chatclient.api.command.CommandHandler;
import net.defekt.mc.chatclient.api.ui.GUIComponents;
import net.defekt.mc.chatclient.protocol.GlobalListeners;
import net.defekt.mc.chatclient.protocol.event.ClientListener;
import net.defekt.mc.chatclient.protocol.event.MinecraftPacketListener;

/**
 * Represents a AMCC plugin
 * 
 * @author Defective4
 *
 */
public abstract class AMCPlugin {

    private PluginDescription description = null;
    private CommandListener ls = new CommandListener();

    /**
     * Default constructor
     */
    public AMCPlugin() {
        registerPacketListener(ls);
    }

    /**
     * Called when plugin is enabled
     */
    public abstract void onLoaded();

    /**
     * Called when GUI fully initializes
     * 
     * @param components
     */
    public void onGUIInitialized(GUIComponents components) {

    }

    /**
     * Get plugin description
     * 
     * @return {@link PluginDescription} associated with this plugin
     */
    public PluginDescription getDescription() {
        return description;
    }

    /**
     * Register new packet listener
     * 
     * @param listener
     */
    protected void registerPacketListener(MinecraftPacketListener listener) {
        GlobalListeners.registerListener(listener);
    }

    /**
     * Register new client listener
     * 
     * @param listener
     */
    protected void registerClientListener(ClientListener listener) {
        GlobalListeners.registerListener(listener);
    }

    /**
     * Register new command
     * 
     * @param cmd
     * @param handler
     */
    protected void registerCommand(String cmd, CommandHandler handler) {
        ls.registerCommand(cmd, handler);
    }

    /**
     * Unregister a command
     * 
     * @param cmd
     */
    protected void unregisterCommand(String cmd) {
        ls.unregisterCommand(cmd);
    }
}
