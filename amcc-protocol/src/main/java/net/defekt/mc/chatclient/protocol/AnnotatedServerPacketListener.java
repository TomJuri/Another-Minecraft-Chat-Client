package net.defekt.mc.chatclient.protocol;

import net.defekt.mc.chatclient.protocol.data.UserPreferences;
import net.defekt.mc.chatclient.protocol.event.ClientListener;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Timer;
import java.util.TimerTask;

class AnnotatedServerPacketListener implements InternalPacketListener {

    protected MinecraftClient cl;
    protected PacketRegistry registry;
    protected int protocol;
    protected UserPreferences up;
    protected Timer keepAliveTimer = new Timer("keepAliveTimer", true);
    protected long lastKeepAlivePacket = System.currentTimeMillis();

    protected AnnotatedServerPacketListener(final MinecraftClient client) {
        this.cl = client;
        this.protocol = cl.getProtocol();
        this.up = UserPreferences.load();
        this.registry = cl.getReg();
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

    @Override
    public void packetReceived(final Packet packet, final PacketRegistry registry) {
        for (final Method method : getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ServerPacketHandler.class)) {
                if (method.getParameterCount() >= 1) {
                    final Parameter param = method.getParameters()[0];
                    final Class<?> paramClass = param.getType();
                    Class<?> pClass = packet.getClass();
                    while (pClass != null && pClass != paramClass) {
                        pClass = pClass.getSuperclass();
                    }
                    if (pClass == paramClass) {
                        try {
                            method.invoke(this, packet);
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
