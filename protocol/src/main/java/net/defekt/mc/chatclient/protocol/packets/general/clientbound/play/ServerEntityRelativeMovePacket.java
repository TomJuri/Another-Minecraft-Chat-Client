package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

@SuppressWarnings("javadoc")
public class ServerEntityRelativeMovePacket extends Packet {

    private final int entityID;
    private double deltaX;
    private double deltaY;
    private double deltaZ;

    public ServerEntityRelativeMovePacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        entityID = is.readVarInt();
        deltaX = is.readShort();
        deltaY = is.readShort();
        deltaZ = is.readShort();

        deltaX /= 32;
        deltaY /= 32;
        deltaZ /= 32;

        deltaX /= 128;
        deltaY /= 128;
        deltaZ /= 128;
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
