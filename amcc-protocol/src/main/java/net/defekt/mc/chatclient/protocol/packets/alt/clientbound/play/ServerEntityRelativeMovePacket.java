package net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerEntityRelativeMovePacket;

import java.io.IOException;

/**
 * An alternative version of
 * {@link net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerEntityRelativeMovePacket}
 *
 * @author Defective4
 * @see net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerEntityRelativeMovePacket
 */
public class ServerEntityRelativeMovePacket extends BaseServerEntityRelativeMovePacket {

    /**
     * Constructs new {@link ServerEntityRelativeMovePacket}
     *
     * @param reg  packet registry used to contruct this packet
     * @param data packet data
     * @throws IOException never thrown
     */
    public ServerEntityRelativeMovePacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        entityID = is.readVarInt();
        deltaX = is.readByte();
        deltaY = is.readByte();
        deltaZ = is.readByte();

        deltaX /= 32;
        deltaY /= 32;
        deltaZ /= 32;
    }

}
