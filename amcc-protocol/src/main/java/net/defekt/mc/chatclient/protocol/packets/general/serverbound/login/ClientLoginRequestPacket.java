package net.defekt.mc.chatclient.protocol.packets.general.serverbound.login;

import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseClientLoginRequestPacket;

/**
 * Sent by client to start login process
 * 
 * @author Defective4
 *
 */
public class ClientLoginRequestPacket extends BaseClientLoginRequestPacket {

    /**
     * Constructs new {@link ClientLoginRequestPacket}
     * 
     * @param reg      packet registry used to contruct this packet
     * @param username player's username
     */
    public ClientLoginRequestPacket(final PacketRegistry reg, final String username) {
        super(reg, username);
        putString(username);
        id = 0;
    }

}
