package net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * An alternative version of
 * {@link net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerEntityTeleportPacket}
 * 
 * @see net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerEntityTeleportPacket
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public class ServerEntityTeleportPacket extends Packet {

    protected int id;
    protected double x;
    protected double y;
    protected double z;

    /**
     * Constructs new {@link ServerEntityTeleportPacket}
     * 
     * @param reg  packet registry used to contruct this packet
     * @param data packet data
     * @throws IOException never thrown
     */
    public ServerEntityTeleportPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        id = is.readVarInt();
        x = is.readInt();
        y = is.readInt();
        z = is.readInt();
        x /= 32;
        y /= 32;
        z /= 32;
    }

    /**
     * Get X coordinate of teleported entity
     * 
     * @return X coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Get Y coordinate of teleported entity
     * 
     * @return Y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Get Z coordinate of teleported entity
     * 
     * @return Z coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * Get ID of teleported entity
     * 
     * @return entity ID
     */
    public int getId() {
        return id;
    }

}
