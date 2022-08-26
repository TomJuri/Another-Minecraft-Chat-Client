package net.defekt.mc.chatclient.protocol.packets;

import java.io.IOException;

public class UnknownPacket extends Packet {

    public UnknownPacket(final PacketRegistry reg, final int id, final byte[] data) throws IOException {
        super(reg, id, data);

    }

}
