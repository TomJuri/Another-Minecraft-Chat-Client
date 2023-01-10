package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.AbstractServerEntityTeleportPacket;

@SuppressWarnings("javadoc")
public class ServerEntityTeleportPacket extends AbstractServerEntityTeleportPacket {

    public ServerEntityTeleportPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        id = is.readVarInt();
        x = is.readDouble();
        y = is.readDouble();
        z = is.readDouble();
    }

}
