package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerSpawnEntityPacket;

import java.io.IOException;

@SuppressWarnings("javadoc")
public class ServerSpawnEntityPacket extends BaseServerSpawnEntityPacket {

    public ServerSpawnEntityPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        id = is.readVarInt();
        uid = is.readUUID();
        type = is.readVarInt();
        x = is.readDouble();
        y = is.readDouble();
        z = is.readDouble();
    }

}
