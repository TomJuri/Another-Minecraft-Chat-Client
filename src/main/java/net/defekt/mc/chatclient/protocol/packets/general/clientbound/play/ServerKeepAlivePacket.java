package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerKeepAlivePacket;

import java.io.IOException;

/**
 * Sent by server to ensure that client is still alive.<br>
 * Client has to respons with the same packet, with the same ID.
 *
 * @author Defective4
 */
public class ServerKeepAlivePacket extends BaseServerKeepAlivePacket {

    /**
     * constructs {@link ServerKeepAlivePacket}
     *
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerKeepAlivePacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        this.pid = getInputStream().readLong();
    }

}
