package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;
import java.util.UUID;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class BaseServerSpawnEntityPacket extends Packet {

    protected int id;
    protected UUID uid;
    protected int type;
    protected double x;
    protected double y;
    protected double z;

    protected BaseServerSpawnEntityPacket(PacketRegistry reg, byte[] data) throws IOException {
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
