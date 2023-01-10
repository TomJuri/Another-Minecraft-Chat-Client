package net.defekt.mc.chatclient.protocol.packets.general.clientbound.login;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * A packet sent by server during login process to indicate that encryption has
 * been set.
 * 
 * @author Defective4
 *
 */
public class ServerLoginSetCompressionPacket extends Packet {

    private final int threshold;

    /**
     * Contructs {@link ServerLoginSetCompressionPacket}
     * 
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerLoginSetCompressionPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        this.threshold = getInputStream().readVarInt();
    }

    /**
     * Get compression threshold
     * 
     * @return compression threshol
     */
    public int getThreshold() {
        return threshold;
    }

}
