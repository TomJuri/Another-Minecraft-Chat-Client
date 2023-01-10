package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class AbstractServerResourcePackSendPacket extends Packet {
    protected String url;
    protected String hash;
    protected boolean forced;
    protected boolean hasPrompt;
    protected String prompt;

    protected AbstractServerResourcePackSendPacket(PacketRegistry reg, byte[] data) throws IOException {
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
