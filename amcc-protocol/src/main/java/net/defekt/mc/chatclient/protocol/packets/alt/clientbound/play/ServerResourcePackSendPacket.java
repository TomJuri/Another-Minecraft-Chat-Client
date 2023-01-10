package net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.AbstractServerResourcePackSendPacket;

/**
 * Newer version of
 * {@link net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerResourcePackSendPacket}
 * <br>
 * Used in Minecraft 1.18
 * 
 * @author Defective4
 *
 */
public class ServerResourcePackSendPacket extends AbstractServerResourcePackSendPacket {

    /**
     * constructs {@link ServerResourcePackSendPacket}
     * 
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerResourcePackSendPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        url = is.readString();
        hash = is.readString();
        forced = is.readBoolean();
        hasPrompt = is.readBoolean();
        prompt = hasPrompt ? is.readString() : "";
    }
}
