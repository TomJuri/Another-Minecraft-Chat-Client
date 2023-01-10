/**
 * 
 */
package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.AbstractServerOpenWindowPacket;

/**
 * Sent by server when a window is opened
 * 
 * @author Defective4
 */
public class ServerOpenWindowPacket extends AbstractServerOpenWindowPacket {

    /**
     * Constructs {@link ServerOpenWindowPacket}
     * 
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerOpenWindowPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        windowID = is.readUnsignedByte();
        windowType = is.readString();
        windowTitle = is.readString();
        slots = is.readUnsignedByte();
    }

}
