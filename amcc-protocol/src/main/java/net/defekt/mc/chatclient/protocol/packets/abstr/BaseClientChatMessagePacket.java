package net.defekt.mc.chatclient.protocol.packets.abstr;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Base class for all serverbound chat messages
 * 
 * @author Defective4
 *
 */
public class BaseClientChatMessagePacket extends Packet {

    private final String message;
    private final boolean command;

    /**
     * Default constructor
     * 
     * @param reg
     * @param message
     * @param command
     */
    protected BaseClientChatMessagePacket(PacketRegistry reg, String message, boolean command) {
        super(reg);
        this.message = message;
        this.command = command;
    }

    /**
     * Get chat message contained in this packet
     * 
     * @return contained chat message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Only applies to versions 1.19+<br>
     * On older versions always returns false.
     * 
     * @return true if the message should be interpreted as a command
     */
    public boolean isCommand() {
        return command;
    }

}
