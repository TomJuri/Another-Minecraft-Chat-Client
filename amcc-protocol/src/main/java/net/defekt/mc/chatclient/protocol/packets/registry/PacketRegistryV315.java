package net.defekt.mc.chatclient.protocol.packets.registry;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerKeepAlivePacket;
import net.defekt.mc.chatclient.protocol.packets.alt.serverbound.play.ClientKeepAlivePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginEncryptionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginResponsePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginSetCompressionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginSuccessPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.*;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.login.ClientLoginRequestPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A packet registry implementation for protocol 315
 *
 * @author Defective4
 */
public class PacketRegistryV315 extends PacketRegistry {

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
                put(0x0B, ClientKeepAlivePacket.class);
                put(0x02, ClientChatMessagePacket.class);
                put(0x16, ClientResourcePackStatusPacket.class);
                put(0x09, ClientPluginMessagePacket.class);
                put(0x03, ClientStatusPacket.class);
                put(0x14, ClientEntityActionPacket.class);
                put(0x0C, ClientPlayerPositionPacket.class);
                put(0x0D, ClientPlayerPositionAndLookPacket.class);
                put(0x07, ClientWindowClickPacket.class);
                put(0x08, ClientCloseWindowPacket.class);
                put(0x17, ClientHeldItemChangePacket.class);
                put(0x13, ClientPlayerDiggingPacket.class);
                put(0x1D, ClientUseItemPacket.class);
                put(0x05, ClientConfirmTransactionPacket.class);
                put(0x0A, ClientUseEntityPacket.class);
                put(0x1A, ClientAnimationPacket.class);
            }
        };
    }

    @Override
    protected Map<Integer, Class<? extends Packet>> initInPackets() {
        return new HashMap<Integer, Class<? extends Packet>>() {
            private static final long serialVersionUID = 1L;

            {
                put(0x1F, ServerKeepAlivePacket.class);
                put(0x0F, ServerChatMessagePacket.class);
                put(0x2E, ServerPlayerPositionAndLookPacket.class);
                put(0x1A, ServerDisconnectPacket.class);
                put(0x32, ServerResourcePackSendPacket.class);
                put(0x18, ServerPluginMessagePacket.class);
                put(0x3E, ServerUpdateHealthPacket.class);
                put(0x23, ServerJoinGamePacket.class);
                put(0x2D, ServerPlayerListItemPacket.class);
                put(0x07, ServerStatisticsPacket.class);
                put(0x12, ServerCloseWindowPacket.class);
                put(0x13, ServerOpenWindowPacket.class);
                put(0x14, ServerWindowItemsPacket.class);
                put(0x16, ServerSetSlotPacket.class);
                put(0x11, ServerConfirmTransactionPacket.class);
                put(0x44, ServerTimeUpdatePacket.class);

                put(0x03, ServerSpawnEntityPacket.class);
                put(0x05, ServerSpawnPlayerPacket.class);
                put(0x30, ServerDestroyEntitiesPacket.class);
                put(0x25, ServerEntityRelativeMovePacket.class);
                put(0x26, ServerEntityRelativeMovePacket.class);
                put(0x49, ServerEntityTeleportPacket.class);
            }
        };
    }

}
