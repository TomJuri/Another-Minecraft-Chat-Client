package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class AbstractServerLoginSuccessPacket extends Packet {
    protected String uuid;
    protected String username;

    protected AbstractServerLoginSuccessPacket(PacketRegistry reg, byte[] data) throws IOException {
        super(reg, data);
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }
}
