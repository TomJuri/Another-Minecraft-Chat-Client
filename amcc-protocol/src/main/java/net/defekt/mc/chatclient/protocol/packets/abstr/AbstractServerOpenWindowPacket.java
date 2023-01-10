package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class AbstractServerOpenWindowPacket extends Packet {

    protected int windowID;
    protected String windowType;
    protected int windowInt = -1;
    protected String windowTitle;
    protected int slots = -1;

    protected AbstractServerOpenWindowPacket(PacketRegistry reg, byte[] data) throws IOException {
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
