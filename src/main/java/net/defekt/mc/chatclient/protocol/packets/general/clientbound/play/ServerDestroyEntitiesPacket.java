package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

@SuppressWarnings("javadoc")
public class ServerDestroyEntitiesPacket extends Packet {

    private final int[] entityIDs;

    public ServerDestroyEntitiesPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        entityIDs = new int[is.readVarInt()];
        for (int x = 0; x < entityIDs.length; x++) {
            entityIDs[x] = is.readVarInt();
        }
    }

    public int[] getEntityIDs() {
        return entityIDs;
    }

}
