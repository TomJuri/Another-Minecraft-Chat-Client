package net.defekt.mc.chatclient.api.command;

import net.defekt.mc.chatclient.protocol.MinecraftClient;

public interface CommandHandler {
    public void userCommand(String cmd, String[] args, MinecraftClient client);
}
