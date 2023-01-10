package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class BaseServerEntityTeleportPacket extends Packet {

    protected int id;
    protected double x;
    protected double y;
    protected double z;

    protected BaseServerEntityTeleportPacket(PacketRegistry reg, byte[] data) throws IOException {
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
