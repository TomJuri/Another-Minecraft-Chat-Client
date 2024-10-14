package net.defekt.mc.chatclient.protocol.packets.registry;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.alt.clientbound.login.ServerLoginSuccessPacket;
import net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerResourcePackSendPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginEncryptionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginResponsePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginSetCompressionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.*;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.*;
import net.defekt.mc.chatclient.protocol.packets.v1_19.clientbound.play.ServerPlayerListItemPacket;
import net.defekt.mc.chatclient.protocol.packets.v1_19_2.clientbound.play.ServerPlayerChatMessagePacket;
import net.defekt.mc.chatclient.protocol.packets.v1_19_2.serverbound.login.ClientLoginRequestPacket;
import net.defekt.mc.chatclient.protocol.packets.v1_19_2.serverbound.play.ClientChatCommandPacket;
import net.defekt.mc.chatclient.protocol.packets.v1_19_2.serverbound.play.ClientChatMessagePacket;

import java.util.HashMap;
import java.util.Map;

/**
 * A packet registry implementation for protocol 755
 *
 * @author Defective4
 */
public class PacketRegistryV760 extends PacketRegistry {

    @Override
    public Map<Integer, Class<? extends Packet>> initLoginPackets() {
        return new HashMap<Integer, Class<? extends Packet>>() {
            private static final long serialVersionUID = 1L;

            {
                put(0x99, ClientLoginRequestPacket.class);
                put(0x00, ServerLoginResponsePacket.class);
                put(0x01, ServerLoginEncryptionPacket.class);
                put(0x02, ServerLoginSuccessPacket.class);
                put(0x03, ServerLoginSetCompressionPacket.class);
            }
        };
    }

    @Override
    protected Map<Integer, Class<? extends Packet>> initOutPackets() {
        return new HashMap<Integer, Class<? extends Packet>>() {
            private static final long serialVersionUID = 1L;

            {
                put(0x12, ClientKeepAlivePacket.class);
                put(0x04, ClientChatCommandPacket.class);
                put(0x05, ClientChatMessagePacket.class);
                put(0x24, ClientResourcePackStatusPacket.class);
                put(0x0D, ClientPluginMessagePacket.class);
                put(0x07, ClientStatusPacket.class);
                put(0x1E, ClientEntityActionPacket.class);
                put(0x14, ClientPlayerPositionPacket.class);
                put(0x15, ClientPlayerPositionAndLookPacket.class);
                put(0x0B, ClientWindowClickPacket.class);
                put(0x0C, ClientCloseWindowPacket.class);
                put(0x28, ClientHeldItemChangePacket.class);
                put(0x1D, ClientPlayerDiggingPacket.class);
                put(0x32, ClientUseItemPacket.class);
                //				put(0x07, ClientConfirmTransactionPacket.class); <- No window confirmation packet in 1.17 ???
                put(0x10, ClientUseEntityPacket.class);
                put(0x2F, ClientAnimationPacket.class);
            }
        };
    }

    @Override
    protected Map<Integer, Class<? extends Packet>> initInPackets() {
        return new HashMap<Integer, Class<? extends Packet>>() {
            private static final long serialVersionUID = 1L;

            {
                put(0x20, ServerKeepAlivePacket.class);
                put(0x62, ServerChatMessagePacket.class);
                put(0x33, ServerPlayerChatMessagePacket.class);
                put(0x39, ServerPlayerPositionAndLookPacket.class);
                put(0x19, ServerDisconnectPacket.class);
                put(0x3D, ServerResourcePackSendPacket.class);
                put(0x16, ServerPluginMessagePacket.class);
                put(0x55, ServerUpdateHealthPacket.class);
                put(0x25, ServerJoinGamePacket.class);
                put(0x37, ServerPlayerListItemPacket.class);
                put(0x10, ServerCloseWindowPacket.class);
                //                put(0x2E, ServerOpenWindowPacket.class);
                put(0x11, ServerWindowItemsPacket.class);
                put(0x13, ServerSetSlotPacket.class);
                //				put(0x11, ServerConfirmTransactionPacket.class); <- Same as in serverbound version?
                put(0x5C, ServerTimeUpdatePacket.class);

                put(0x00, ServerSpawnEntityPacket.class);
                put(0x02, ServerSpawnPlayerPacket.class);
                put(0x3B, ServerDestroyEntitiesPacket.class);
                put(0x28, ServerEntityRelativeMovePacket.class);
                //                put(0x2A, ServerEntityRelativeMovePacket.class);
                //                put(0x26, ServerEntityTeleportPacket.class);
            }
        };
    }

}
