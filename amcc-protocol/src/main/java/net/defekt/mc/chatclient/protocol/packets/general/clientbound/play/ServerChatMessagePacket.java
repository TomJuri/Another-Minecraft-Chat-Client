package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.AbstractServerChatMessagePacket;

/**
 * Sent by server when client received a chat message
 * 
 * @author Defective4
 *
 */
public class ServerChatMessagePacket extends AbstractServerChatMessagePacket {
    public enum Position {
        /**
         * Message position is in chat box
         */
        CHAT,
        /**
         * System message
         */
        SYSTEM,
        /**
         * Message is displayed above hotbar
         */
        HOTBAR
    }

    /**
     * Constructs {@link ServerChatMessagePacket}
     * 
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerChatMessagePacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        this.message = is.readString();
        this.position = is.readByte();
    }

}
