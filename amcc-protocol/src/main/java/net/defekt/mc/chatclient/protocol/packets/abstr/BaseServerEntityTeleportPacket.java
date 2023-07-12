package net.defekt.mc.chatclient.protocol.packets.abstr;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

import java.io.IOException;

/**
 * Base class for all entity teleportation packets
 *
 * @author Defective4
 */
@SuppressWarnings("javadoc")
public class BaseServerEntityTeleportPacket extends Packet {

    /**
     * ID of an entity
     */
    protected int id;

    /**
     * Destination X
     */
    protected double x;

    /**
     * Destination Y
     */
    protected double y;

    /**
     * Destination Z
     */
    protected double z;

    /**
     * Default constructor
     *
     * @param reg
     * @param data
     * @throws IOException
     */
    protected BaseServerEntityTeleportPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

}
