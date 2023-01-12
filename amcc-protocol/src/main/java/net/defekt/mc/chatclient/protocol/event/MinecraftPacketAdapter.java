package net.defekt.mc.chatclient.protocol.event;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.packets.Packet;

/**
 * An abstract adapter class for {@link MinecraftPacketListener}<br>
 * All methods of this class are empty by default.<br>
 * For easier packet type checking and overall easier usage, please see
 * {@link AnnotatedMinecraftPacketListener}
 * 
 * @see AnnotatedMinecraftPacketListener
 * @author Defective4
 *
 */
public abstract class MinecraftPacketAdapter implements MinecraftPacketListener {

    @Override
    public Packet packetReceiving(Packet inPacket, MinecraftClient client) {
        return inPacket;
    }

    @Override
    public Packet packetSending(Packet outPacket, MinecraftClient client) {
        return outPacket;
    }

    @Override
    public void packetReceived(Packet inPacket, MinecraftClient client) {
    }

    @Override
    public void packetSent(Packet outPacket, MinecraftClient client) {
    }

}
