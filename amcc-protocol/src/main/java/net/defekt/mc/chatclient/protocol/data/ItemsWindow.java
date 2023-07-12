package net.defekt.mc.chatclient.protocol.data;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Base class for all item windows.
 *
 * @author Defective4
 */
public abstract class ItemsWindow {

    /**
     * Default constructor
     *
     * @param title
     * @param size
     * @param windowID
     * @param client
     * @param registry
     */
    public ItemsWindow(final String title, final int size, final int windowID, final MinecraftClient client, final PacketRegistry registry) {

    }

    /**
     * Mark the transaction as finished and flush any pending changes.
     *
     * @param windowID
     */
    public abstract void finishTransaction(short windowID);

    /**
     * Cancel the transaction and discard any changes.
     *
     * @param windowID
     */
    public abstract void cancelTransaction(short windowID);

    /**
     * Put a new item in specified slot
     *
     * @param slot
     * @param item
     */
    public abstract void putItem(int slot, ItemStack item);

    /**
     * Completely close the window
     */
    public abstract void closeWindow();

    /**
     * @param silent
     */
    public abstract void closeWindow(boolean silent);

    /**
     * Get inventory size in slots.
     *
     * @return inventory size
     */
    public abstract int getSize();
}
