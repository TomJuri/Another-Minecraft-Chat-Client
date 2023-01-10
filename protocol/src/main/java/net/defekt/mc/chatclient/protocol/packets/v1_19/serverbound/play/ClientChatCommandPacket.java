package net.defekt.mc.chatclient.protocol.packets.v1_19.serverbound.play;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class ClientChatCommandPacket extends Packet {

    public ClientChatCommandPacket(PacketRegistry reg, String message) {
        super(reg);
        String cmd = message;
        if (cmd.startsWith("/")) cmd = cmd.substring(1);
        putString(cmd);
        putLong(System.currentTimeMillis());
        putLong(0);
        putVarInt(0);
        putBoolean(false);
    }

}
