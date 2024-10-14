package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

import java.io.IOException;

/**
 * Sent by server when time is updated.
 *
 * @author Defective4
 */
public class ServerTimeUpdatePacket extends Packet {

    private final long worldAge;
    private final long time;

    /**
     * Constructs {@link ServerTimeUpdatePacket}
     *
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerTimeUpdatePacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        worldAge = is.readLong();
        time = is.readLong();
    }

    /**
     * @return get server world's age
     */
    public long getWorldAge() {
        return worldAge;
    }

    /**
     * @return get current time on server
     */
    public long getTime() {
        return time;
    }

}
