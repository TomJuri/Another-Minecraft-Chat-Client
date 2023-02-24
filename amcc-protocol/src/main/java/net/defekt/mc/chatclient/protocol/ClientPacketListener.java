package net.defekt.mc.chatclient.protocol;

import java.util.Timer;
import java.util.TimerTask;

import net.defekt.mc.chatclient.protocol.data.UserPreferences;
import net.defekt.mc.chatclient.protocol.event.ClientListener;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * An implementation of {@link InternalPacketListener} responsible for handling
 * all packets received from server. It is added to every instance of
 * {@link MinecraftClient} after successfully connecting to server.
 * 
 * @see MinecraftClient
 * @see ClientListener
 * @author Defective4
 * @deprecated Use {@link MainPacketListener} instead
 *
 */
@Deprecated
class ClientPacketListener implements InternalPacketListener {

    private final int protocol;
    private final MinecraftClient cl;
    private final UserPreferences up;

    private final Timer keepAliveTimer = new Timer("keepAliveTimer", true);
    private final long lastKeepAlivePacket = System.currentTimeMillis();

    /**
     * Constructs packet listener bound to specified client
     * 
     * @param client A Minecraft client this listener is bound o
     * @deprecated
     */
    @Deprecated
    protected ClientPacketListener(final MinecraftClient client) {
        this.cl = client;
        this.protocol = cl.getProtocol();
        this.up = UserPreferences.load();
        keepAliveTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (!cl.isConnected()) {
                    keepAliveTimer.cancel();
                    return;
                }

                if (System.currentTimeMillis() - lastKeepAlivePacket > 60000) {
                    for (final ClientListener cls : cl.getClientListeners(true)) {
                        cls.disconnected("Timed Out", client);
                    }
                    cl.close();
                }
            }
        }, 0, 1000);
    }

    @Deprecated
    @Override
    public void packetReceived(final Packet packet, final PacketRegistry registry) {

    }

    public int getProtocol() {
        return protocol;
    }

    public MinecraftClient getCl() {
        return cl;
    }

    public UserPreferences getUp() {
        return up;
    }

    public Timer getKeepAliveTimer() {
        return keepAliveTimer;
    }

    public long getLastKeepAlivePacket() {
        return lastKeepAlivePacket;
    }

}
