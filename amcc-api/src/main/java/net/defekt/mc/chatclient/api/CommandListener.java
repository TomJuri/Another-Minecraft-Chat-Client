package net.defekt.mc.chatclient.api;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.defekt.mc.chatclient.api.command.CommandHandler;
import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.event.AnnotatedMinecraftPacketListener;
import net.defekt.mc.chatclient.protocol.event.PacketHandler;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseClientChatMessagePacket;

/**
 * Represents a command listener
 * 
 * @author Defective4
 *
 */
public class CommandListener extends AnnotatedMinecraftPacketListener {

    private final Map<String, CommandHandler> commands = new ConcurrentHashMap<String, CommandHandler>();

    /**
     * Register a command.<br>
     * Should be called from {@link AMCPlugin}
     * 
     * @param cmd
     * @param handler
     */
    protected void registerCommand(final String cmd, final CommandHandler handler) {
        commands.put(cmd.toLowerCase(), handler);
    }

    /**
     * Unregister a command.<br>
     * Should be called from {@link AMCPlugin}
     * 
     * @param cmd
     */
    protected void unregisterCommand(final String cmd) {
        commands.remove(cmd);
    }

    @SuppressWarnings("javadoc")
    @PacketHandler
    protected void chatSending(final BaseClientChatMessagePacket packet, final MinecraftClient cl) {
        try {
            String message = packet.getMessage();
            if (message.startsWith("/")) {
                message = message.substring(1);
                final String[] split = message.split(" ");
                final String cmd = split[0];
                final String[] args = split.length > 1 ? Arrays.copyOfRange(split, 1, split.length) : new String[0];

                for (final Entry<String, CommandHandler> entry : commands.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase(cmd)) {
                        packet.setCancelled(true);
                        entry.getValue().userCommand(cmd, args, cl);
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
