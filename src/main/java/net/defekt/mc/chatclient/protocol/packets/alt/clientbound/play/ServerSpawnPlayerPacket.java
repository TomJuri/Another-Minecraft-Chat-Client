package net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play;

import java.io.IOException;
import java.util.UUID;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class ServerSpawnPlayerPacket extends Packet {

    protected int id;
    protected UUID uid;
    protected double x;
    protected double y;
    protected double z;

    public ServerSpawnPlayerPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        id = is.readVarInt();
        uid = is.readUUID();
        x = is.readInt();
        y = is.readInt();
        z = is.readInt();

        x /= 32;
        y /= 32;
        z /= 32;
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

    public int getId() {
        return id;
    }

}
