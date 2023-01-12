package net.defekt.mc.chatclient.protocol.packets.v1_19.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerChatMessagePacket;

/**
 * 1.19 version of {@link BaseServerChatMessagePacket}
 * 
 * @author Defective4
 *
 */
public class ServerPlayerChatMessagePacket extends BaseServerChatMessagePacket {

    /**
     * Constructs {@link ServerPlayerChatMessagePacket}
     * 
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerPlayerChatMessagePacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        String message = is.readString();
        if (is.readBoolean()) message = is.readString();
        super.message = message;
        super.position = is.readByte();
    }

}
