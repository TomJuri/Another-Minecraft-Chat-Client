package net.defekt.mc.chatclient.protocol.packets.general.clientbound.login;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.AbstractServerLoginSuccessPacket;

/**
 * Packet sent by server when client finished logging in
 * 
 * @author Defective4
 *
 */
public class ServerLoginSuccessPacket extends AbstractServerLoginSuccessPacket {

    /**
     * Contructs {@link ServerLoginSuccessPacket}
     * 
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerLoginSuccessPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        uuid = is.readString();
        username = is.readString();
    }

}
