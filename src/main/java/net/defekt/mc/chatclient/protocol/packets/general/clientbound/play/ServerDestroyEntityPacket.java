package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class ServerDestroyEntityPacket extends Packet {

    private final int entityID;

    public ServerDestroyEntityPacket(PacketRegistry reg, byte[] data) throws IOException {
        super(reg, data);
        VarInputStream is = getInputStream();
        entityID = is.readVarInt();
    }

    public int getEntityIDs() {
        return entityID;
    }

}
