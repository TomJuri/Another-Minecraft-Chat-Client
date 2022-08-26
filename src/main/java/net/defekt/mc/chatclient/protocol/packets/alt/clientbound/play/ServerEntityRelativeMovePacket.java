package net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class ServerEntityRelativeMovePacket extends Packet {

    private final int entityID;
    private double deltaX;
    private double deltaY;
    private double deltaZ;

    public ServerEntityRelativeMovePacket(PacketRegistry reg, byte[] data) throws IOException {
        super(reg, data);
        VarInputStream is = getInputStream();
        entityID = is.readVarInt();
        deltaX = is.readByte();
        deltaY = is.readByte();
        deltaZ = is.readByte();

        deltaX /= 32;
        deltaY /= 32;
        deltaZ /= 32;
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
