package net.defekt.mc.chatclient.protocol.event;

import java.io.IOException;
import java.util.Map;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.data.ItemsWindow;
import net.defekt.mc.chatclient.protocol.entity.Entity;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket.Position;

/**
 * An adapter class for {@link ClientListener}
 * 
 * @author Defective4
 *
 */
public abstract class ClientAdapter implements ClientListener {

    @Override
    public boolean messageReceived(String message, Position position, MinecraftClient client) {
        return false;
    }

    @Override
    public void disconnected(String reason, MinecraftClient client) {
    }

    @Override
    public void healthUpdate(float health, int food, MinecraftClient client) {
    }

    @Override
    public void positionChanged(double x, double y, double z, MinecraftClient client) {
    }

    @Override
    public void statisticsReceived(Map<String, Integer> values, MinecraftClient client) {
    }

    @Override
    public void windowOpened(int id, ItemsWindow win, PacketRegistry reg, MinecraftClient client) {
    }

    @Override
    public void timeUpdated(long time, long worldAge, MinecraftClient client) {
    }

    @Override
    public void changedTrackedEntity(int id, MinecraftClient client) {
    }

    @Override
    public void entityMoved(Entity entity, int id, MinecraftClient client) {
    }

    @Override
    public void tick(MinecraftClient client) throws IOException {
    }

}
