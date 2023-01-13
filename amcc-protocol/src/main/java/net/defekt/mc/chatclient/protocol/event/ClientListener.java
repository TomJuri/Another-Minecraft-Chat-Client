package net.defekt.mc.chatclient.protocol.event;

import java.io.IOException;
import java.util.Map;

import net.defekt.mc.chatclient.protocol.MainPacketListener;
import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.data.ItemsWindow;
import net.defekt.mc.chatclient.protocol.entity.Entity;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry.State;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket.Position;

/**
 * A listener interface for receiving client related events, like chat messages
 * or health updates
 * 
 * @see MainPacketListener
 * @author Defective4
 */
public interface ClientListener {

    /**
     * Invoked when client's game state gets changed
     * 
     * @param oldState state before change
     * @param newState state after change
     * @param client
     */
    public void gameStateChanged(State oldState, State newState, MinecraftClient client);

    /**
     * Invoked when a chat message was received.
     * 
     * @param message  chat message in parsed text form
     * @param position position of this chat message
     * @param client
     * 
     * @return whether or not the message should be canceled and NOT displayed to in
     *         the clien
     */
    public boolean messageReceived(String message, Position position, MinecraftClient client);

    /**
     * Invoked when client got disconnected from server.
     * 
     * @param reason reason of disconnecting
     * @param client
     */
    public void disconnected(String reason, MinecraftClient client);

    /**
     * Invoked when client receives a health update.
     * 
     * @param health client's new health. Value equal or less than zero means that
     *               client is dead
     * @param food   client's new hunger
     * @param client
     */
    public void healthUpdate(float health, int food, MinecraftClient client);

    /**
     * Invoked when client's position on server is updated.
     * 
     * @param x      new X position
     * @param y      new Y position
     * @param z      new Z position
     * @param client
     */
    public void positionChanged(double x, double y, double z, MinecraftClient client);

    /**
     * Invoked when client receives statistics after ClientStatus packet
     * 
     * @param values map of received values
     * @param client
     */
    public void statisticsReceived(Map<String, Integer> values, MinecraftClient client);

    /**
     * Invoked when server shows a window to client (a enderchest, interactive GUI,
     * etc.)
     * 
     * @param id     opened window's ID
     * @param win    opened window
     * @param reg    client's packet registry
     * @param client
     */
    public void windowOpened(int id, ItemsWindow win, PacketRegistry reg, MinecraftClient client);

    /**
     * Invoked when time on server is updated
     * 
     * @param time     server's time
     * @param worldAge current world's age
     * @param client
     */
    public void timeUpdated(long time, long worldAge, MinecraftClient client);

    /**
     * Called when client changes tracked target
     * 
     * @param id     ID of the new tracked entity
     * @param client
     */
    public void changedTrackedEntity(int id, MinecraftClient client);

    /**
     * Called when an entity moves within client's range
     * 
     * @param entity moved entity
     * @param id     ID of the moved entity
     * @param client
     */
    public void entityMoved(Entity entity, int id, MinecraftClient client);

    /**
     * Called every in-game tick (approximately)
     * 
     * @param client
     * 
     * @throws IOException
     */
    public void tick(MinecraftClient client) throws IOException;
}
