package net.defekt.mc.chatclient.protocol.event;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.packets.Packet;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * An implementation of the {@link MinecraftPacketListener}.<br>
 * It allows to create new methods that act as handlers<br>
 * instead of implementing only 4 predefined methods o
 * {@link MinecraftPacketListener}.<br>
 * New methods should be marked with {@link PacketHandler}
 *
 * @author Defective4
 */
public abstract class AnnotatedMinecraftPacketListener implements MinecraftPacketListener {

    @Override
    public Packet packetReceiving(final Packet inPacket, final MinecraftClient client) {
        return propagate(inPacket, client, true);
    }

    @Override
    public Packet packetSending(final Packet outPacket, final MinecraftClient client) {
        return propagate(outPacket, client, true);
    }

    @Override
    public void packetReceived(final Packet inPacket, final MinecraftClient client) {
        propagate(inPacket, client, false);
    }

    @Override
    public void packetSent(final Packet outPacket, final MinecraftClient client) {
        propagate(outPacket, client, false);
    }

    private Packet propagate(final Packet packet, final MinecraftClient client, final boolean pre) {
        try {
            for (final Method method : getClass().getDeclaredMethods()) {
                method.setAccessible(true);
                if (method.isAnnotationPresent(PacketHandler.class)) {
                    final PacketHandler ann = method.getAnnotation(PacketHandler.class);
                    if (ann.preSend() != pre) break;
                    if (method.getParameterCount() >= 1) {
                        final Parameter[] pms = method.getParameters();
                        final Class<?> pmType = pms[0].getType();
                        Class<?> pmL = packet.getClass();
                        while (pmL != null && pmL != pmType) {
                            pmL = pmL.getSuperclass();
                        }

                        if (pmL == pmType) {

                            Object result;
                            if (pms.length >= 2 && pms[1].getType() == MinecraftClient.class) {
                                result = method.invoke(this, packet, client);
                            } else {
                                result = method.invoke(this, packet);
                            }
                            method.setAccessible(false);
                            if (result != null && result instanceof Packet) {
                                return (Packet) result;
                            } else {
                                return null;
                            }
                        }
                    }
                }
                method.setAccessible(false);
            }
        } catch (final Exception e) {
        }
        return null;
    }

}
