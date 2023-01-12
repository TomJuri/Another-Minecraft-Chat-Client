package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Base class for all server keep alive packets
 * 
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public class BaseServerKeepAlivePacket extends Packet {

    /**
     * ID contained in the keep alive packet
     */
    protected long pid;

    /**
     * If the packet comes from a < 1.9 client
     */
    protected boolean legacy = false;

    /**
     * Default cosntructor
     * 
     * @param reg
     * @param data
     * @throws IOException
     */
    protected BaseServerKeepAlivePacket(PacketRegistry reg, byte[] data) throws IOException {
        super(reg, data);
    }

    public long getPid() {
        return pid;
    }

    public boolean isLegacy() {
        return legacy;
    }

}
