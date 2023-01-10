package net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerEntityTeleportPacket;

/**
 * An alternative version of
 * {@link net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerEntityTeleportPacket}
 * 
 * @see net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerEntityTeleportPacket
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public class ServerEntityTeleportPacket extends BaseServerEntityTeleportPacket {

    /**
     * Constructs new {@link ServerEntityTeleportPacket}
     * 
     * @param reg  packet registry used to contruct this packet
     * @param data packet data
     * @throws IOException never thrown
     */
    public ServerEntityTeleportPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        id = is.readVarInt();
        x = is.readInt();
        y = is.readInt();
        z = is.readInt();
        x /= 32;
        y /= 32;
        z /= 32;
    }

}
