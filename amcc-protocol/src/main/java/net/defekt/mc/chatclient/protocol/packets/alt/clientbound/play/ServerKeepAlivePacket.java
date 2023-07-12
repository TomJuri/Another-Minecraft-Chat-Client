package net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play;

import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerKeepAlivePacket;

import java.io.IOException;

/**
 * An older version of
 * {@link net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerKeepAlivePacket}
 * packet used in protocol versions below 340
 *
 * @author Defective4
 * @see net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerKeepAlivePacket
 */
public class ServerKeepAlivePacket extends BaseServerKeepAlivePacket {
    /**
     * Constructs new {@link ServerKeepAlivePacket}
     *
     * @param reg  packet registry used to contruct this packet
     * @param data packet data
     * @throws IOException never thrown
     */
    public ServerKeepAlivePacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        this.pid = getInputStream().readVarInt();
        legacy = true;
    }

}
