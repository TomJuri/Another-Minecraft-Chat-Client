package net.defekt.mc.chatclient.protocol.data;

/**
 * Base interface for all item windows.
 * 
 * @author Defective4
 *
 */
public interface ItemsWindow {
    /**
     * Mark the transaction as finished and flush any pending changes.
     * 
     * @param windowID
     */
    public void finishTransaction(int windowID);

    /**
     * Cancel the transaction and discard any changes.
     * 
     * @param windowID
     */
    public void cancelTransaction(int windowID);

    /**
     * Put a new item in specified slot
     * 
     * @param slot
     * @param item
     */
    public void putItem(int slot, ItemStack item);

    /**
     * Completely close the window
     */
    public void closeWindow();

    /**
     * 
     * @param silent
     */
    public void closeWindow(boolean silent);

    /**
     * Get inventory size in slots.
     * 
     * @return inventory size
     */
    public int getSize();
}
