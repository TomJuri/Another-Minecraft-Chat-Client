package net.defekt.mc.chatclient.protocol.data;

/**
 * An implementation of {@link ItemsWindow} acting as a void for items.<br>
 * It does not perform any item handling.
 * 
 * @author Defective4
 *
 */
public class DummyItemsWindow implements ItemsWindow {

    private final int slots;

    /**
     * Default constructor
     * 
     * @param title
     * @param slots
     * @param id
     */
    public DummyItemsWindow(String title, int slots, int id) {
        super();
        this.slots = slots;
    }

    @Override
    public void finishTransaction(int windowID) {
    }

    @Override
    public void cancelTransaction(int windowID) {
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
