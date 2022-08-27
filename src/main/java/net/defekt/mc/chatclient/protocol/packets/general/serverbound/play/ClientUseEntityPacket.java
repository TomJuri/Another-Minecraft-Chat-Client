package net.defekt.mc.chatclient.protocol.packets.general.serverbound.play;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketFactory;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

public class ClientUseEntityPacket extends Packet {

    public enum UseType {
        INTERACT(0), ATTACK(1);

        private final int type;

        private UseType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public ClientUseEntityPacket(PacketRegistry reg, Integer entityID, UseType type, Boolean sneaking) {
        super(reg);
        putVarInt(entityID);
        putVarInt(type.getType());
        int protocol = PacketFactory.getProtocolFor(reg);
        if (protocol > 47) {
            if (type == UseType.INTERACT) putVarInt(0);
            if (protocol > 735) putBoolean(sneaking);
        }
    }

}
