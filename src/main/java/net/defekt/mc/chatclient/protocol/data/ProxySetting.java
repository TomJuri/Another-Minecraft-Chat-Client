package net.defekt.mc.chatclient.protocol.data;

import java.io.Serializable;

public class ProxySetting implements Serializable {
    private final String name;
    private final String host;
    private final int port;

    public ProxySetting(String name, String host, int port) {
        super();
        this.name = name;
        this.host = host;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return getName();
    }
}
