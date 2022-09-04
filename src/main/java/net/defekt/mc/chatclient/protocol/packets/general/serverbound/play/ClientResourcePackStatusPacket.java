package net.defekt.mc.chatclient.protocol.packets.general.serverbound.play;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.ui.Messages;

/**
 * Sent by client as response to server's resource pack
 * 
 * @author Defective4
 *
 */
public class ClientResourcePackStatusPacket extends Packet {

    /**
     * Resource pack status
     * 
     * @author Defective4
     *
     */
    public enum Status {
        /**
         * Incoming resource pack will be declined
         */
        DECLINED(1, "Main.rsBehaviorDecline"),
        /**
         * Incoming resource pack will be accepted
         */
        ACCEPTED(3, "Main.rsBehaviorAccept"),
        /**
         * Incoming resource pack will be loaded
         */
        LOADED(0, "Main.rsBehaviorAcceptLoad"),
        /**
         * Incoming resource pack will fail to load
         */
        FAILED(2, "Main.rsBehaviorFail");

        /**
         * Status number
         */
        public final int num;

        /**
         * Translation key
         */
        public final String key;

        private Status(final int num, final String key) {
            this.num = num;
            this.key = key;
        }

        @Override
        public String toString() {
            return Messages.getString(key);
        }
    }

    /**
     * Constructs new {@link ClientResourcePackStatusPacket}
     * 
     * @param reg    packet registry used to construct this packet
     * @param status resource pack status
     */
    public ClientResourcePackStatusPacket(final PacketRegistry reg, final Status status) {
        super(reg);
        putVarInt(status.num);
    }

}
