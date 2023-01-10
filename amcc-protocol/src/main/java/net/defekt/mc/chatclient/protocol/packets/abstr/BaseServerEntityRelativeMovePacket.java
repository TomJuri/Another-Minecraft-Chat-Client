package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class BaseServerEntityRelativeMovePacket extends Packet {

    protected int entityID;
    protected double deltaX;
    protected double deltaY;
    protected double deltaZ;

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
