package net.defekt.mc.chatclient.protocol.packets.v1_19_2.serverbound.login;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class ClientLoginRequestPacket extends Packet {

    public ClientLoginRequestPacket(PacketRegistry reg, String name) {
        super(reg);
        putString(name);
        putBoolean(false);
        putBoolean(false);
        id = 0;
    }

}
