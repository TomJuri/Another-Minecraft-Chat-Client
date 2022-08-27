package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class ServerDestroyEntityPacket extends Packet {

    private final int entityID;

    public ServerDestroyEntityPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        entityID = is.readVarInt();
    }

    public int getEntityIDs() {
        return entityID;
    }

}
