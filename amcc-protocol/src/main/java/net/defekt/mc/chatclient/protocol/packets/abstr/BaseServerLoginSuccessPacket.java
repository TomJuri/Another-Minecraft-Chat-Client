package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class BaseServerLoginSuccessPacket extends Packet {
    protected String uuid;
    protected String username;

    protected BaseServerLoginSuccessPacket(PacketRegistry reg, byte[] data) throws IOException {
        super(reg, data);
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }
}
