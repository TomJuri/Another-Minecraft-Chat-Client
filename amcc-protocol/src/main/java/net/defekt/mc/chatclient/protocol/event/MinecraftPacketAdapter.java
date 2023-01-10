package net.defekt.mc.chatclient.protocol.event;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.packets.Packet;

public class MinecraftPacketAdapter implements MinecraftPacketListener {

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
