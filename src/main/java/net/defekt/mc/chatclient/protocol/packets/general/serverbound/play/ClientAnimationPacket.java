package net.defekt.mc.chatclient.protocol.packets.general.serverbound.play;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketFactory;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

@SuppressWarnings("javadoc")
public class ClientAnimationPacket extends Packet {

    public ClientAnimationPacket(final PacketRegistry reg) {
        super(reg);
        if (PacketFactory.getProtocolFor(reg) > 47) {
            putVarInt(0);
        }
    }

}
