package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerPlayerPositionAndLookPacket;

/**
 * Sent by server when client's position is updated in-game
 * 
 * @author Defective4
 *
 */
public class ServerPlayerPositionAndLookPacket extends BaseServerPlayerPositionAndLookPacket {

    /**
     * constructs {@link ServerPlayerPositionAndLookPacket}
     * 
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerPlayerPositionAndLookPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        x = is.readDouble();
        y = is.readDouble();
        z = is.readDouble();

        yaw = is.readFloat();
        pitch = is.readFloat();

        flags = is.readByte();

        teleportID = is.readVarInt();
    }

}
