package net.defekt.mc.chatclient.protocol.event;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.data.ItemsWindow;
import net.defekt.mc.chatclient.protocol.entity.Entity;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry.State;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket.Position;

import java.io.IOException;
import java.util.Map;

/**
 * An adapter class for {@link ClientListener}
 *
 * @author Defective4
 */
public abstract class ClientAdapter implements ClientListener {

    @Override
    public boolean messageReceived(final String message, final Position position, final MinecraftClient client) {
        return false;
    }

    @Override
    public void disconnected(final String reason, final MinecraftClient client) {
    }

    @Override
    public void healthUpdate(final float health, final int food, final MinecraftClient client) {
    }

    @Override
    public void positionChanged(final double x, final double y, final double z, final MinecraftClient client) {
    }

    @Override
    public void statisticsReceived(final Map<String, Integer> values, final MinecraftClient client) {
    }

    @Override
    public void windowOpened(final int id, final ItemsWindow win, final PacketRegistry reg, final MinecraftClient client) {
    }

    @Override
    public void timeUpdated(final long time, final long worldAge, final MinecraftClient client) {
    }

    @Override
    public void changedTrackedEntity(final int id, final MinecraftClient client) {
    }

    @Override
    public void entityMoved(final Entity entity, final int id, final MinecraftClient client) {
    }

    @Override
    public void tick(final MinecraftClient client) throws IOException {
    }

    @Override
    public void gameStateChanged(final State oldState, final State newState, final MinecraftClient client) {
    }

}
