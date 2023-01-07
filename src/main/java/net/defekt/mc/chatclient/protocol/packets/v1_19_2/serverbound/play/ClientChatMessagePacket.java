package net.defekt.mc.chatclient.protocol.packets.v1_19_2.serverbound.play;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class ClientChatMessagePacket extends Packet {

    public ClientChatMessagePacket(PacketRegistry reg, String message) {
        super(reg);
        putString(message);
        putLong(System.currentTimeMillis());
        putLong(0);
        putVarInt(0);
        putBoolean(false);
        putVarInt(0);
        putBoolean(false);
    }

}
