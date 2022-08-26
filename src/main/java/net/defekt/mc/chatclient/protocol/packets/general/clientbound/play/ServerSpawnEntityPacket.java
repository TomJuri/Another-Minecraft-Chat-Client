package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;
import java.util.UUID;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class ServerSpawnEntityPacket extends Packet {

    protected int id;
    protected UUID uid;
    protected int type;
    protected double x;
    protected double y;
    protected double z;

    public ServerSpawnEntityPacket(PacketRegistry reg, byte[] data) throws IOException {
        super(reg, data);
        VarInputStream is = getInputStream();
        id = is.readVarInt();
        uid = is.readUUID();
        type = is.readVarInt();
        x = is.readDouble();
        y = is.readDouble();
        z = is.readDouble();
    }

    public int getType() {
        return type;
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
