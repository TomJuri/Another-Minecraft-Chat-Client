package net.defekt.mc.chatclient.protocol.packets.alt.clientbound.login;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerLoginSuccessPacket;

import java.io.IOException;

/**
 * Packet sent by server when client finished logging in
 *
 * @author Defective4
 */
public class ServerLoginSuccessPacket extends BaseServerLoginSuccessPacket {

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
        uuid = is.readUUID().toString();
        username = is.readString();
    }

}
