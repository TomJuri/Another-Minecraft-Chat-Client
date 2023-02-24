package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Base class for all clientbound resource pack send packets
 * 
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public class BaseServerResourcePackSendPacket extends Packet {
    protected String url;
    protected String hash;
    protected boolean forced;
    protected boolean hasPrompt;
    protected String prompt;

    /**
     * Default constructor
     * 
     * @param reg
     * @param data
     * @throws IOException
     */
    protected BaseServerResourcePackSendPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
    }

    public String getUrl() {
        return url;
    }

    public String getHash() {
        return hash;
    }

    public boolean isForced() {
        return forced;
    }

    public boolean isHasPrompt() {
        return hasPrompt;
    }

    public String getPrompt() {
        return prompt;
    }

}
