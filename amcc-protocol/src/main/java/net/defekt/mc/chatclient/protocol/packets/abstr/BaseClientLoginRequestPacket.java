package net.defekt.mc.chatclient.protocol.packets.abstr;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Base class for all serverbound client login request packets
 * 
 * @author Defective4
 *
 */
public class BaseClientLoginRequestPacket extends Packet {

    /**
     * Client's username
     */
    protected final String name;

    /**
     * Default constructor
     * 
     * @param reg
     * @param name
     */
    protected BaseClientLoginRequestPacket(PacketRegistry reg, String name) {
        super(reg);
        this.name = name;
    }

}
