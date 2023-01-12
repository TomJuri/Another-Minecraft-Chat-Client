package net.defekt.mc.chatclient.api.command;

import net.defekt.mc.chatclient.protocol.MinecraftClient;

/**
 * This interface contains a method for handling incoming commands
 * 
 * @author Defective4
 *
 */
public interface CommandHandler {
    /**
     * Called when user enters a command
     * 
     * @param cmd
     * @param args
     * @param client
     */
    public void userCommand(String cmd, String[] args, MinecraftClient client);
}
