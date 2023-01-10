package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerResourcePackSendPacket;

/**
 * Sent by server to update resource pack used by client
 * 
 * @author Defective4
 *
 */
public class ServerResourcePackSendPacket extends BaseServerResourcePackSendPacket {

    /**
     * constructs {@link ServerResourcePackSendPacket}
     * 
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerResourcePackSendPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream in = getInputStream();
        url = in.readString();
        hash = in.readString();
        forced = false;
        hasPrompt = false;
        prompt = "";
    }
}
