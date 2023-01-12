package net.defekt.mc.chatclient.protocol;

public class ProtocolEntry {
    public final String name;
    public final int protocol;

    public ProtocolEntry(int protocol, String name) {
        this.name = name;
        this.protocol = protocol;
    }

    public String getName() {
        return name;
    }

    public int getProtocol() {
        return protocol;
    }
}
