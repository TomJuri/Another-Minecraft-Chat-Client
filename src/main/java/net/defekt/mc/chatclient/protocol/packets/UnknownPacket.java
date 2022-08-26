package net.defekt.mc.chatclient.protocol.packets;

import java.io.IOException;

/**
 * Class representing an uknown packet.<br>
 * Unknown packets still store all data received from server.<br>
 * The difference between Unknown Packets, and regular {@link Packet}s is
 * that<br>
 * the unknown ones can't be extended, and can't be handled by Packet Handler.
 * 
 * @see PacketFactory
 * @see PacketRegistry
 * @see Packet
 * @author Defective4
 *
 */
public final class UnknownPacket extends Packet {

    /**
     * Constructs a raw unknown packet
     * 
     * @param reg  packet registry
     * @param id   packet ID
     * @param data packet data
     * @throws IOException never actually thrown
     */
    public UnknownPacket(final PacketRegistry reg, final int id, final byte[] data) throws IOException {
        super(reg, id, data);

    }

}
