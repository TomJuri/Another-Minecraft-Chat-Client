package net.defekt.mc.chatclient.protocol.packets.general.serverbound.play;

import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseClientChatMessagePacket;

/**
 * Sent by client when it's trying to send a chat message
 *
 * @author Defective4
 */
public class ClientChatMessagePacket extends BaseClientChatMessagePacket {

    /**
     * Constructs new {@link ClientChatMessagePacket}
     *
     * @param reg     packet registry used to construct this packet
     * @param message chat message
     */
    public ClientChatMessagePacket(final PacketRegistry reg, final String message) {
        super(reg, message, false);
        putString(message);
    }

}
