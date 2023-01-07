package net.defekt.mc.chatclient.protocol.packets.registry;

import java.util.HashMap;
import java.util.Map;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.alt.clientbound.login.ServerLoginSuccessPacket;
import net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerResourcePackSendPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginEncryptionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginResponsePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginSetCompressionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerCloseWindowPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerDestroyEntitiesPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerDisconnectPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerEntityRelativeMovePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerJoinGamePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerKeepAlivePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPlayerListItemPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPlayerPositionAndLookPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPluginMessagePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerSetSlotPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerSpawnEntityPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerSpawnPlayerPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerTimeUpdatePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerUpdateHealthPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerWindowItemsPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientAnimationPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientCloseWindowPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientEntityActionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientHeldItemChangePacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientKeepAlivePacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientPlayerDiggingPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientPlayerPositionAndLookPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientPlayerPositionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientPluginMessagePacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientResourcePackStatusPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientStatusPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientUseEntityPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientUseItemPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientWindowClickPacket;
import net.defekt.mc.chatclient.protocol.packets.v1_19.serverbound.login.ClientLoginRequestPacket;
import net.defekt.mc.chatclient.protocol.packets.v1_19.serverbound.play.ClientChatCommandPacket;
import net.defekt.mc.chatclient.protocol.packets.v1_19.serverbound.play.ClientChatMessagePacket;

/**
 * A packet registry implementation for protocol 755
 * 
 * @author Defective4
 */
public class PacketRegistryV759 extends PacketRegistry {

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
                put(0x11, ClientKeepAlivePacket.class);
                put(0x03, ClientChatCommandPacket.class);
                put(0x04, ClientChatMessagePacket.class);
                put(0x23, ClientResourcePackStatusPacket.class);
                put(0x0C, ClientPluginMessagePacket.class);
                put(0x06, ClientStatusPacket.class);
                put(0x1D, ClientEntityActionPacket.class);
                put(0x13, ClientPlayerPositionPacket.class);
                put(0x14, ClientPlayerPositionAndLookPacket.class);
                put(0x0A, ClientWindowClickPacket.class);
                put(0x0B, ClientCloseWindowPacket.class);
                put(0x27, ClientHeldItemChangePacket.class);
                put(0x1C, ClientPlayerDiggingPacket.class);
                put(0x31, ClientUseItemPacket.class);
//				put(0x07, ClientConfirmTransactionPacket.class); <- No window confirmation packet in 1.17 ???
                put(0x0F, ClientUseEntityPacket.class);
                put(0x2E, ClientAnimationPacket.class);
            }
        };
    }

    @Override
    protected Map<Integer, Class<? extends Packet>> initInPackets() {
        return new HashMap<Integer, Class<? extends Packet>>() {
            private static final long serialVersionUID = 1L;
            {
                put(0x1E, ServerKeepAlivePacket.class);
                put(0x5F, ServerChatMessagePacket.class);
                put(0x36, ServerPlayerPositionAndLookPacket.class);
                put(0x17, ServerDisconnectPacket.class);
                put(0x3A, ServerResourcePackSendPacket.class);
                put(0x15, ServerPluginMessagePacket.class);
                put(0x52, ServerUpdateHealthPacket.class);
                put(0x23, ServerJoinGamePacket.class);
                put(0x34, ServerPlayerListItemPacket.class);
                put(0x10, ServerCloseWindowPacket.class);
//                put(0x2E, ServerOpenWindowPacket.class);
                put(0x11, ServerWindowItemsPacket.class);
                put(0x13, ServerSetSlotPacket.class);
//				put(0x11, ServerConfirmTransactionPacket.class); <- Same as in serverbound version?
                put(0x59, ServerTimeUpdatePacket.class);

                put(0x00, ServerSpawnEntityPacket.class);
                put(0x02, ServerSpawnPlayerPacket.class);
                put(0x38, ServerDestroyEntitiesPacket.class);
                put(0x26, ServerEntityRelativeMovePacket.class);
//                put(0x2A, ServerEntityRelativeMovePacket.class);
//                put(0x26, ServerEntityTeleportPacket.class);
            }
        };
    }

}
