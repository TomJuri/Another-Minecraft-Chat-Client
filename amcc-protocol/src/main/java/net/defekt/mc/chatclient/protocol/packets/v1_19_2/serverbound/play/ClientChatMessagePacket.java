package net.defekt.mc.chatclient.protocol.packets.v1_19_2.serverbound.play;

import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseClientChatMessagePacket;

public class ClientChatMessagePacket extends BaseClientChatMessagePacket {

    public ClientChatMessagePacket(PacketRegistry reg, String message) {
        super(reg, message, false);
        putString(message);
        putLong(System.currentTimeMillis());
        putLong(0);
        putVarInt(0);
        putBoolean(false);
        putVarInt(0);
        putBoolean(false);
    }

}
