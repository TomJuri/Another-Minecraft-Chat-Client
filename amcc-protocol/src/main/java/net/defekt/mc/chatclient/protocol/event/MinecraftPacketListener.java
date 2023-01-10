package net.defekt.mc.chatclient.protocol.event;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.packets.Packet;

public interface MinecraftPacketListener {
    public Packet packetReceiving(Packet inPacket, MinecraftClient client);

    public Packet packetSending(Packet outPacket, MinecraftClient client);

    public void packetReceived(Packet inPacket, MinecraftClient client);

    public void packetSent(Packet outPacket, MinecraftClient client);
}
