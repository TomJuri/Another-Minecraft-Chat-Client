package net.defekt.mc.chatclient.protocol.packets.abstr;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class BaseClientChatMessagePacket extends Packet {

    private final String message;
    private final boolean command;

    protected BaseClientChatMessagePacket(PacketRegistry reg, String message, boolean command) {
        super(reg);
        this.message = message;
        this.command = command;
    }

    public String getMessage() {
        return message;
    }

    public boolean isCommand() {
        return command;
    }

}
