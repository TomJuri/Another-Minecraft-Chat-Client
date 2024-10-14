package net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerPlayerPositionAndLookPacket;

import java.io.IOException;

/**
 * An older version of
 * {@link net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPlayerPositionAndLookPacket}
 *
 * @author Defective4
 * @see net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPlayerPositionAndLookPacket
 */
public class ServerPlayerPositionAndLookPacket extends BaseServerPlayerPositionAndLookPacket {

    /**
     * Constructs new {@link ServerPlayerPositionAndLookPacket}
     *
     * @param reg  packet registry used to contruct this packet
     * @param data packet data
     * @throws IOException never thrown
     */
    public ServerPlayerPositionAndLookPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        x = is.readDouble();
        y = is.readDouble();
        z = is.readDouble();

        yaw = is.readFloat();
        pitch = is.readFloat();

        flags = is.readByte();
    }

}
