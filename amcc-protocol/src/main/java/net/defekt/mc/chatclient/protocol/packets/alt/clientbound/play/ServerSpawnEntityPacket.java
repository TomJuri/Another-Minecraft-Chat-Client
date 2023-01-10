package net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play;

import java.io.IOException;
import java.util.UUID;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.AbstractServerSpawnEntityPacket;

@SuppressWarnings("javadoc")
public class ServerSpawnEntityPacket extends AbstractServerSpawnEntityPacket {

    public ServerSpawnEntityPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        id = is.readVarInt();
        type = is.readUnsignedByte();
        x = is.readInt();
        y = is.readInt();
        z = is.readInt();
        uid = UUID.randomUUID();

        x /= 32;
        y /= 32;
        z /= 32;
    }

}
