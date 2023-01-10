package net.defekt.mc.chatclient.protocol.packets.v1_19.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket.Position;

public class ServerPlayerChatMessagePacket extends Packet {

    private final String message;
    private final byte position;

    /**
     * Constructs {@link ServerPlayerChatMessagePacket}
     * 
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerPlayerChatMessagePacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        String message = is.readString();
        if (is.readBoolean()) message = is.readString();
        this.message = message;
        this.position = is.readByte();
    }

    /**
     * Get JSON message
     * 
     * @return raw JSON message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get message's position
     * 
     * @return message's position
     */
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
