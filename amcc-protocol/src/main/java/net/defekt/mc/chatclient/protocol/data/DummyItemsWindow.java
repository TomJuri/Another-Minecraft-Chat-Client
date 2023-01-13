package net.defekt.mc.chatclient.protocol.data;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * An implementation of {@link ItemsWindow} acting as a void for items.<br>
 * It does not perform any item handling.
 * 
 * @author Defective4
 *
 */
public class DummyItemsWindow extends ItemsWindow {

    private final int slots;

    /**
     * Default constructor
     * 
     * @param title
     * @param size
     * @param windowID
     * @param client
     * @param registry
     */
    public DummyItemsWindow(String title, int size, int windowID, MinecraftClient client, PacketRegistry registry) {
        super(title, size, windowID, client, registry);
        this.slots = size;
    }

    @Override
    public void finishTransaction(short windowID) {
    }

    @Override
    public void cancelTransaction(short windowID) {
    }

    @Override
    public void putItem(int slot, ItemStack item) {
    }

    @Override
    public void closeWindow() {
    }

    @Override
    public void closeWindow(boolean silent) {
    }

    @Override
    public int getSize() {
        return slots;
    }

}
