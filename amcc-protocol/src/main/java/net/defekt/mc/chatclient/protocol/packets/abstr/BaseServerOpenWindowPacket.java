package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Base class for all clientbound window open packets
 * 
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public class BaseServerOpenWindowPacket extends Packet {

    /**
     * ID of the window
     */
    protected int windowID;

    /**
     * Window type
     */
    protected String windowType;

    /**
     * Window type as an Integer (for older versions)
     */
    protected int windowInt = -1;

    /**
     * Window title (can be a chat component)
     */
    protected String windowTitle;

    /**
     * Total size of the window (only on newer versions)
     */
    protected int slots = -1;

    protected BaseServerOpenWindowPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
    }

    public int getWindowID() {
        return windowID;
    }

    public String getWindowType() {
        return windowType;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public int getSlots() {
        return slots;
    }

    public int getWindowInt() {
        return windowInt;
    }

}
