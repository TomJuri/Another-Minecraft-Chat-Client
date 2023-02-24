package net.defekt.mc.chatclient.protocol.packets.v1_19_2.serverbound.play;

import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseClientChatMessagePacket;

/**
 * 1.19.2 version of {@link BaseClientChatMessagePacket}
 * 
 * @author Defective4
 *
 */
public class ClientChatMessagePacket extends BaseClientChatMessagePacket {

    /**
     * Default constructor
     * 
     * @param reg
     * @param message
     */
    public ClientChatMessagePacket(final PacketRegistry reg, final String message) {
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
