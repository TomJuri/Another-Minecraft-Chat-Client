package net.defekt.mc.chatclient.protocol.data;

public interface ItemsWindow {
    public void finishTransaction(int windowID);
    public void cancelTransaction(int windowID);
    public void putItem(int slot, ItemStack item);
    public void closeWindow();
    public void closeWindow(boolean silent);
    public int getSize();
}
