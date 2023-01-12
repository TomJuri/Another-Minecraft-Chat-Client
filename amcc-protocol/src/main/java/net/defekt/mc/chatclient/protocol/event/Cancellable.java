package net.defekt.mc.chatclient.protocol.event;

/**
 * Simple interface for objects, of which execution can be cancelled.
 * 
 * @author Defective4
 *
 */
public interface Cancellable {

    /**
     * Get cancelled state
     * 
     * @return true if cancelled
     */
    public boolean isCancelled();

    /**
     * Set cancelled state
     * 
     * @param canceled
     */
    public void setCancelled(boolean canceled);

}
