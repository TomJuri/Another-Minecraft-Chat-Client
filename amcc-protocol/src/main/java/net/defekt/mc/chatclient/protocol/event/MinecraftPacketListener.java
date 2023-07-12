package net.defekt.mc.chatclient.protocol.event;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.packets.Packet;

/**
 * The listener interface for capturing packet sent and received from server.
 *
 * @author Defective4
 */
public interface MinecraftPacketListener {
    /**
     * Invoked when a packet is being received and before it is handled by
     * client.<br>
     * Packets can be cancelled here to prevent further handling.
     *
     * @param inPacket
     * @param client
     * @return new packet replacing the received one, or <code>null</code> to leave
     * intact.
     */
    public Packet packetReceiving(Packet inPacket, MinecraftClient client);

    /**
     * Invoked before a packet gets sent.<br>
     * Packets can be cancelled here to prevent them fron sending.
     *
     * @param outPacket
     * @param client
     * @return new packet replacing the sending one, or <code>null</code> to leave
     * intact.
     */
    public Packet packetSending(Packet outPacket, MinecraftClient client);

    /**
     * Invoked after a packet gets received and handled by a client.<br>
     * Cancelling packets here has no effect.
     *
     * @param inPacket
     * @param client
     */
    public void packetReceived(Packet inPacket, MinecraftClient client);

    /**
     * Invoked after a packet gets sent to the server.<br>
     * Cancelling packets here has no effect.
     *
     * @param outPacket
     * @param client
     */
    public void packetSent(Packet outPacket, MinecraftClient client);
}
