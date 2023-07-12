package net.defekt.mc.chatclient.protocol.packets.v1_19.serverbound.login;

import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseClientLoginRequestPacket;

/**
 * 1.19 version of {@link BaseClientLoginRequestPacket}
 *
 * @author Defective4
 */
public class ClientLoginRequestPacket extends BaseClientLoginRequestPacket {

    /**
     * Default constructor
     *
     * @param reg
     * @param name
     */
    public ClientLoginRequestPacket(final PacketRegistry reg, final String name) {
        super(reg, name);
        putString(name);
        putBoolean(false);
        id = 0;
    }

}
