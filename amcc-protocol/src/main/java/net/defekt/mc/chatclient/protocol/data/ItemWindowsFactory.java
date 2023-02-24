package net.defekt.mc.chatclient.protocol.data;

import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Factory class for creating new item windows
 * 
 * @author Defective4
 *
 */
public class ItemWindowsFactory {
    private Class<? extends ItemsWindow> itemWindowsClass = DummyItemsWindow.class;

    /**
     * Creates new items window
     * 
     * @param title
     * @param size
     * @param windowID
     * @param client
     * @param registry
     * @return items window
     */
    public ItemsWindow createWindow(final String title, final int size, final int windowID,
            final MinecraftClient client, final PacketRegistry registry) {
        try {
            final ItemsWindow window = (ItemsWindow) itemWindowsClass.getConstructors()[0].newInstance(title, size,
                    windowID, client, registry);

            return window;
        } catch (final Exception e) {
            e.printStackTrace();
            return new DummyItemsWindow(title, size, windowID, client, registry);
        }
    }

    @SuppressWarnings("javadoc")
    public Class<? extends ItemsWindow> getItemWindowsClass() {
        return itemWindowsClass;
    }

    @SuppressWarnings("javadoc")
    public void setItemWindowsClass(final Class<? extends ItemsWindow> itemWindowsClass) {
        this.itemWindowsClass = itemWindowsClass;
    }
}
