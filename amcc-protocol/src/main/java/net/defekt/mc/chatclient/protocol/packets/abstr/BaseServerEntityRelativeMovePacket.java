package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Base class for all entity relavie move packets.
 * 
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public class BaseServerEntityRelativeMovePacket extends Packet {

    /**
     * ID of an entity
     */
    protected int entityID;

    /**
     * Delta X
     */
    protected double deltaX;

    /**
     * Delta Y
     */
    protected double deltaY;

    /**
     * Delta Z
     */
    protected double deltaZ;

    /**
     * Default constructor
     * 
     * @param reg
     * @param data
     * @throws IOException
     */
    protected BaseServerEntityRelativeMovePacket(PacketRegistry reg, byte[] data) throws IOException {
        super(reg, data);
    }

    public int getEntityID() {
        return entityID;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getDeltaZ() {
        return deltaZ;
    }

}
