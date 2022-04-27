package net.defekt.mc.chatclient.protocol;

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
    public void messageReceived(String message, Position position) {
    }

    @Override
    public void disconnected(String reason) {
    }

    @Override
    public void healthUpdate(float health, int food) {
    }

    @Override
    public void positionChanged(double x, double y, double z) {
    }

    @Override
    public void statisticsReceived(Map<String, Integer> values) {
    }

    @Override
    public void windowOpened(int id, ItemsWindow win, PacketRegistry reg) {
    }

    @Override
    public void timeUpdated(long time, long worldAge) {
    }

}
