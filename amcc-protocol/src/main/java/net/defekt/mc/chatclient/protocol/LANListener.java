package net.defekt.mc.chatclient.protocol;

import java.net.InetAddress;

/**
 * An interface used to listen for Minecraft servers over LAN
 *
 * @author Defective4
 * @see MinecraftStat
 */
@FunctionalInterface
public interface LANListener {
    /**
     * Invoked when a server was discovered
     *
     * @param sender address of discovered server
     * @param motd   Message Of The Day sent by server
     * @param port   port of the server
     */
    public void serverDiscovered(InetAddress sender, String motd, int port);
}
