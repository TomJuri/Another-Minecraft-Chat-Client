package net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.AbstractServerKeepAlivePacket;

/**
 * An older version of
 * {@link net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerKeepAlivePacket}
 * packet used in protocol versions below 340
 * 
 * @see net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerKeepAlivePacket
 * @author Defective4
 *
 */
public class ServerKeepAlivePacket extends AbstractServerKeepAlivePacket {
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
