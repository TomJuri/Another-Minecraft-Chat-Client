package net.defekt.mc.chatclient.protocol;

/**
 * Class containing user defined protocol number and name
 *
 * @author Defective4
 */
@SuppressWarnings("javadoc")
public class ProtocolEntry {
    public final String name;
    public final int protocol;

    /**
     * Creates new protocol entry
     *
     * @param protocol
     * @param name
     */
    public ProtocolEntry(final int protocol, final String name) {
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
