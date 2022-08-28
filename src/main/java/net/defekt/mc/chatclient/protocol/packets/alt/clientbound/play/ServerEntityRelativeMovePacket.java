package net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * An alternative version of
 * {@link net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerEntityRelativeMovePacket}
 * 
 * @see net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerEntityRelativeMovePacket
 * @author Defective4
 *
 */
public class ServerEntityRelativeMovePacket extends Packet {

    private final int entityID;
    private double deltaX;
    private double deltaY;
    private double deltaZ;

    /**
     * Constructs new {@link ServerEntityRelativeMovePacket}
     * 
     * @param reg  packet registry used to contruct this packet
     * @param data packet data
     * @throws IOException never thrown
     */
    public ServerEntityRelativeMovePacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        entityID = is.readVarInt();
        deltaX = is.readByte();
        deltaY = is.readByte();
        deltaZ = is.readByte();

        deltaX /= 32;
        deltaY /= 32;
        deltaZ /= 32;
    }

    /**
     * Get moved entity's ID
     * 
     * @return entity ID
     */
    public int getEntityID() {
        return entityID;
    }

    /**
     * Get delta X of moved entity
     * 
     * @return delta X
     */
    public double getDeltaX() {
        return deltaX;
    }

    /**
     * Get delta Y of moved entity
     * 
     * @return delta Y
     */
    public double getDeltaY() {
        return deltaY;
    }

    /**
     * Get delta Z of moved entity
     * 
     * @return delta Z
     */
    public double getDeltaZ() {
        return deltaZ;
    }

}
