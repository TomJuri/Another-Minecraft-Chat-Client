package net.defekt.mc.chatclient.protocol;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Timer;
import java.util.TimerTask;

import net.defekt.mc.chatclient.protocol.data.UserPreferences;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class AnnotatedPacketListener implements InternalPacketListener {

    protected MinecraftClient cl;
    protected PacketRegistry registry;
    protected int protocol;
    protected UserPreferences up;
    protected Timer keepAliveTimer = new Timer("keepAliveTimer", true);
    protected long lastKeepAlivePacket = System.currentTimeMillis();

    protected AnnotatedPacketListener(final MinecraftClient client) {
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
                    for (final ClientListener cls : cl.getClientListeners()) {
                        cls.disconnected("Timed Out");
                    }
                    cl.close();
                }
            }
        }, 0, 1000);
    }

    @Override
    public void packetReceived(Packet packet, PacketRegistry registry) {
        for (Method method : getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PacketHandler.class)) {
                if (method.getParameterCount() >= 1) {
                    Parameter param = method.getParameters()[0];
                    Class<?> paramClass = param.getType();
                    Class<?> pClass = packet.getClass();
                    while (pClass != null && pClass != paramClass) {
                        pClass = pClass.getSuperclass();
                    }
                    if (pClass == paramClass) {
                        try {
                            method.invoke(this, packet);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
