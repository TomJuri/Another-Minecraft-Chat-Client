package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;
import java.util.UUID;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Base class for all clientbound player spawn packets
 * 
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public class BaseServerSpawnPlayerPacket extends Packet {

    /**
     * Player's entity ID
     */
    protected int id;

    /**
     * Player's UUID
     */
    protected UUID uid;
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
    protected BaseServerSpawnPlayerPacket(PacketRegistry reg, byte[] data) throws IOException {
        super(reg, data);
    }

    public int getId() {
        return id;
    }

    public UUID getUid() {
        return uid;
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
