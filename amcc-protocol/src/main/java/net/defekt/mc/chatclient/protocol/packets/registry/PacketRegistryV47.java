package net.defekt.mc.chatclient.protocol.packets.registry;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerEntityRelativeMovePacket;
import net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerEntityTeleportPacket;
import net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerKeepAlivePacket;
import net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerPlayerPositionAndLookPacket;
import net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerSpawnEntityPacket;
import net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerSpawnPlayerPacket;
import net.defekt.mc.chatclient.protocol.packets.alt.serverbound.play.ClientKeepAlivePacket;
import net.defekt.mc.chatclient.protocol.packets.alt.serverbound.play.ClientResourcePackStatusPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginEncryptionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginResponsePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginSetCompressionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginSuccessPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerOpenWindowPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerResourcePackSendPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.*;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.login.ClientLoginRequestPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A packet registry implementation for protocol 47
 *
 * @author Defective4
 */
public class PacketRegistryV47 extends PacketRegistry {

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
                put(0x00, ClientKeepAlivePacket.class);
                put(0x01, ClientChatMessagePacket.class);
                put(0x19, ClientResourcePackStatusPacket.class);
                put(0x17, ClientPluginMessagePacket.class);
                put(0x16, ClientStatusPacket.class);
                put(0x0B, ClientEntityActionPacket.class);
                put(0x04, ClientPlayerPositionPacket.class);
                put(0x06, ClientPlayerPositionAndLookPacket.class);
                put(0x0E, ClientWindowClickPacket.class);
                put(0x0D, ClientCloseWindowPacket.class);
                put(0x09, ClientHeldItemChangePacket.class);
                put(0x0F, ClientConfirmTransactionPacket.class);
                put(0x02, ClientUseEntityPacket.class);
                put(0x0A, ClientAnimationPacket.class);
            }
        };
    }

    @Override
    protected Map<Integer, Class<? extends Packet>> initInPackets() {
        return new HashMap<Integer, Class<? extends Packet>>() {
            private static final long serialVersionUID = 1L;

            {
                put(0x00, ServerKeepAlivePacket.class);
                put(0x02, ServerChatMessagePacket.class);
                put(0x08, ServerPlayerPositionAndLookPacket.class);
                put(0x40, ServerDisconnectPacket.class);
                put(0x48, ServerResourcePackSendPacket.class);
                put(0x3F, ServerPluginMessagePacket.class);
                put(0x06, ServerUpdateHealthPacket.class);
                put(0x01, ServerJoinGamePacket.class);
                put(0x38, ServerPlayerListItemPacket.class);
                put(0x37, ServerStatisticsPacket.class);
                put(0x2E, ServerCloseWindowPacket.class);
                put(0x2D, ServerOpenWindowPacket.class);
                put(0x30, ServerWindowItemsPacket.class);
                put(0x2F, ServerSetSlotPacket.class);
                put(0x23, ServerConfirmTransactionPacket.class);
                put(0x03, ServerTimeUpdatePacket.class);

                put(0x0F, ServerSpawnEntityPacket.class);
                put(0x0C, ServerSpawnPlayerPacket.class);
                put(0x13, ServerDestroyEntitiesPacket.class);
                put(0x15, ServerEntityRelativeMovePacket.class);
                put(0x17, ServerEntityRelativeMovePacket.class);
                put(0x18, ServerEntityTeleportPacket.class);
            }
        };
    }

}
