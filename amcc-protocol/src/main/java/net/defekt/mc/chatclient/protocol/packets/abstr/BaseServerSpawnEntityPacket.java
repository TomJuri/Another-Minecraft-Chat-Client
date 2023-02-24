package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;
import java.util.UUID;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Base class for all clientbound entity spawn packets
 * 
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public class BaseServerSpawnEntityPacket extends Packet {

    /**
     * ID of the entity
     */
    protected int id;

    /**
     * Unique ID of the entity
     */
    protected UUID uid;

    /**
     * Entity type
     */
    protected int type;
    protected double x;
    protected double y;
    protected double z;

    /**
     * Default constructor
     * 
     * @param reg
     * @param data
     * @throws IOException
     */
    protected BaseServerSpawnEntityPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
    }

    public int getId() {
        return id;
    }

    public UUID getUid() {
        return uid;
    }

    public int getType() {
        return type;
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
