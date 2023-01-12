package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket.Position;

/**
 * Base class for all clientbound chat message packets
 * 
 * @author Defective4
 *
 */
public class BaseServerChatMessagePacket extends Packet {

    /**
     * Json chat message received from server
     */
    protected String message;

    /**
     * Chat message position.<br>
     * See {@link Position}
     */
    protected byte position;

    /**
     * Default constructor
     * 
     * @param reg
     * @param data
     * @throws IOException
     */
    public BaseServerChatMessagePacket(PacketRegistry reg, byte[] data) throws IOException {
        super(reg, data);
    }

    /**
     * Get received message
     * 
     * @return json message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get message position
     * 
     * @return {@link Position}
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
