package net.defekt.mc.chatclient.protocol.data;

import java.io.Serializable;

/**
 * An object used to store SOCKS proxy host, port and name
 *
 * @author Defective4
 */
public class ProxySetting implements Serializable {
    private final String name;
    private final String host;
    private final int port;

    /**
     * Default constructor
     *
     * @param name setting's display name
     * @param host proxy hostname
     * @param port proxy port
     */
    public ProxySetting(final String name, final String host, final int port) {
        super();
        this.name = name;
        this.host = host;
        this.port = port;
    }

    /**
     * Get proxy's display name
     *
     * @return proxy display name
     */
    public String getName() {
        return name;
    }

    /**
     * Get proxy's hostname
     *
     * @return proxy hostname
     */
    public String getHost() {
        return host;
    }

    /**
     * Get proxy's port
     *
     * @return proxy port
     */
    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return getName();
    }
}
