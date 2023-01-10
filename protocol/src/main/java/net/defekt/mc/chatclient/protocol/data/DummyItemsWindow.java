package net.defekt.mc.chatclient.protocol.data;

public class DummyItemsWindow implements ItemsWindow {

    private final String title;
    private final int slots;
    private final int id;

    public DummyItemsWindow(String title, int slots, int id) {
        super();
        this.title = title;
        this.slots = slots;
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public int getSlots() {
        return slots;
    }

    public int getId() {
        return id;
    }

}
