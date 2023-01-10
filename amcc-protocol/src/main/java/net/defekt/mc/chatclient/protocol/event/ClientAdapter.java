package net.defekt.mc.chatclient.protocol.event;

import java.util.Map;

import net.defekt.mc.chatclient.protocol.data.ItemsWindow;
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
    public void messageReceived(final String message, final Position position) {
    }

    @Override
    public void disconnected(final String reason) {
    }

    @Override
    public void healthUpdate(final float health, final int food) {
    }

    @Override
    public void positionChanged(final double x, final double y, final double z) {
    }

    @Override
    public void statisticsReceived(final Map<String, Integer> values) {
    }

    @Override
    public void windowOpened(final int id, final ItemsWindow win, final PacketRegistry reg) {
    }

    @Override
    public void timeUpdated(final long time, final long worldAge) {
    }

}
