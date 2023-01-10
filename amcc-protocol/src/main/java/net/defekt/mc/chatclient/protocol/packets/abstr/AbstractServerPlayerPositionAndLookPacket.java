package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class AbstractServerPlayerPositionAndLookPacket extends Packet {

    protected double x;
    protected double y;
    protected double z;
    protected float yaw;
    protected float pitch;
    protected byte flags;
    protected int teleportID;

    protected AbstractServerPlayerPositionAndLookPacket(PacketRegistry reg, byte[] data) throws IOException {
        super(reg, data);
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

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public byte getFlags() {
        return flags;
    }

    public int getTeleportID() {
        return teleportID;
    }

}
