package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket.Position;

public class AbstractServerChatMessagePacket extends Packet {
    
    protected String message;
    protected byte position;

    public AbstractServerChatMessagePacket(PacketRegistry reg, byte[] data) throws IOException {
        super(reg, data);
    }

    public String getMessage() {
        return message;
    }

    public Position getPosition() {
        switch (position) {
            case 0: {
                return Position.CHAT;
            }
            case 1: {
                return Position.SYSTEM;
            }
            case 2: {
                return Position.HOTBAR;
            }
            default: {
                return Position.CHAT;
            }
        }
    }

}
