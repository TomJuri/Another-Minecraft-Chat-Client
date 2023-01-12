package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Base class for all clientbound login success packets
 * 
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public class BaseServerLoginSuccessPacket extends Packet {
    /**
     * UUID of the player
     */
    protected String uuid;

    /**
     * Player's username
     */
    protected String username;

    /**
     * Default constructor
     * 
     * @param reg
     * @param data
     * @throws IOException
     */
    protected BaseServerLoginSuccessPacket(PacketRegistry reg, byte[] data) throws IOException {
        super(reg, data);
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }
}
