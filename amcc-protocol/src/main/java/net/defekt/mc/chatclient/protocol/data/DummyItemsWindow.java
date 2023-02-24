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
    public DummyItemsWindow(final String title, final int size, final int windowID, final MinecraftClient client,
            final PacketRegistry registry) {
        super(title, size, windowID, client, registry);
        this.slots = size;
    }

    @Override
    public void finishTransaction(final short windowID) {
    }

    @Override
    public void cancelTransaction(final short windowID) {
    }

    @Override
    public void putItem(final int slot, final ItemStack item) {
    }

    @Override
    public void closeWindow() {
    }

    @Override
    public void closeWindow(final boolean silent) {
    }

    @Override
    public int getSize() {
        return slots;
    }

}
