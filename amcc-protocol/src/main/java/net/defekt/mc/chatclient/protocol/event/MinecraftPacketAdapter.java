package net.defekt.mc.chatclient.protocol.event;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.packets.Packet;

/**
 * An abstract adapter class for {@link MinecraftPacketListener}<br>
 * All methods of this class are empty by default.<br>
 * For easier packet type checking and overall easier usage, please see
 * {@link AnnotatedMinecraftPacketListener}
 *
 * @author Defective4
 * @see AnnotatedMinecraftPacketListener
 */
public abstract class MinecraftPacketAdapter implements MinecraftPacketListener {

    @Override
    public Packet packetReceiving(final Packet inPacket, final MinecraftClient client) {
        return inPacket;
    }

    @Override
    public Packet packetSending(final Packet outPacket, final MinecraftClient client) {
        return outPacket;
    }

    @Override
    public void packetReceived(final Packet inPacket, final MinecraftClient client) {
    }

    @Override
    public void packetSent(final Packet outPacket, final MinecraftClient client) {
    }

}
