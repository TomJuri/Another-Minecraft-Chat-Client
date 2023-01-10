package net.defekt.mc.chatclient.protocol.event;

public interface Cancelable {
    public boolean isCanceled();

    public void setCanceled(boolean canceled);
    
    
}
