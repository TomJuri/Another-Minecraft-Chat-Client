package net.defekt.mc.chatclient.protocol.event;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.packets.Packet;

/**
 * An implementation of the {@link MinecraftPacketListener}.<br>
 * It allows to create new methods that act as handlers<br>
 * instead of implementing only 4 predefined methods o
 * {@link MinecraftPacketListener}.<br>
 * New methods should be marked with {@link PacketHandler}
 * 
 * @author Defective4
 *
 */
public abstract class AnnotatedMinecraftPacketListener implements MinecraftPacketListener {

    @Override
    public Packet packetReceiving(Packet inPacket, MinecraftClient client) {
        return propagate(inPacket, client, true);
    }

    @Override
    public Packet packetSending(Packet outPacket, MinecraftClient client) {
        return propagate(outPacket, client, true);
    }

    @Override
    public void packetReceived(Packet inPacket, MinecraftClient client) {
        propagate(inPacket, client, false);
    }

    @Override
    public void packetSent(Packet outPacket, MinecraftClient client) {
        propagate(outPacket, client, false);
    }

    private Packet propagate(Packet packet, MinecraftClient client, boolean pre) {
        try {
            for (Method method : getClass().getDeclaredMethods()) {
                method.setAccessible(true);
                if (method.isAnnotationPresent(PacketHandler.class)) {
                    PacketHandler ann = method.getAnnotation(PacketHandler.class);
                    if (ann.preSend() != pre) break;
                    if (method.getParameterCount() >= 1) {
                        Parameter[] pms = method.getParameters();
                        Class<?> pmType = pms[0].getType();
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
        } catch (Exception e) {
        }
        return null;
    }

}
