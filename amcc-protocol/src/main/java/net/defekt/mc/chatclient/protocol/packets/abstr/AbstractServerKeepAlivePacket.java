package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class AbstractServerKeepAlivePacket extends Packet {

    protected long pid;
    protected boolean legacy = false;
    
    protected AbstractServerKeepAlivePacket(PacketRegistry reg, byte[] data) throws IOException {
        super(reg, data);
    }

    public long getPid() {
        return pid;
    }

    public boolean isLegacy() {
        return legacy;
    }

}
