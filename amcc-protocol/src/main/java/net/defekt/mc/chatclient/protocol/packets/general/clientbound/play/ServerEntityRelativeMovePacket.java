package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.AbstractServerEntityRelativeMovePacket;

@SuppressWarnings("javadoc")
public class ServerEntityRelativeMovePacket extends AbstractServerEntityRelativeMovePacket {

    public ServerEntityRelativeMovePacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        entityID = is.readVarInt();
        deltaX = is.readShort();
        deltaY = is.readShort();
        deltaZ = is.readShort();

        deltaX /= 32;
        deltaY /= 32;
        deltaZ /= 32;

        deltaX /= 128;
        deltaY /= 128;
        deltaZ /= 128;
    }

}
