package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;
import java.util.UUID;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class AbstractSpawnPlayerPacket extends Packet {

    protected int id;
    protected UUID uid;
    protected double x;
    protected double y;
    protected double z;

    protected AbstractSpawnPlayerPacket(PacketRegistry reg, byte[] data) throws IOException {
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
