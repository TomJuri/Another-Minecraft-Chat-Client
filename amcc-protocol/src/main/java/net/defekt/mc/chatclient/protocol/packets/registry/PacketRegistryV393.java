package net.defekt.mc.chatclient.protocol.packets.registry;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
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
 * A packet registry implementation for protocol 393
 *
 * @author Defective4
 */
public class PacketRegistryV393 extends PacketRegistry {

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
                put(0x0E, ClientKeepAlivePacket.class);
                put(0x02, ClientChatMessagePacket.class);
                put(0x1D, ClientResourcePackStatusPacket.class);
                put(0x0A, ClientPluginMessagePacket.class);
                put(0x03, ClientStatusPacket.class);
                put(0x19, ClientEntityActionPacket.class);
                put(0x10, ClientPlayerPositionPacket.class);
                put(0x11, ClientPlayerPositionAndLookPacket.class);
                put(0x08, ClientWindowClickPacket.class);
                put(0x09, ClientCloseWindowPacket.class);
                put(0x21, ClientHeldItemChangePacket.class);
                put(0x18, ClientPlayerDiggingPacket.class);
                put(0x2A, ClientUseItemPacket.class);
                put(0x06, ClientConfirmTransactionPacket.class);
                put(0x0D, ClientUseEntityPacket.class);
                put(0x27, ClientAnimationPacket.class);
            }
        };
    }

    @Override
    protected Map<Integer, Class<? extends Packet>> initInPackets() {
        return new HashMap<Integer, Class<? extends Packet>>() {
            private static final long serialVersionUID = 1L;

            {
                put(0x21, ServerKeepAlivePacket.class);
                put(0x0E, ServerChatMessagePacket.class);
                put(0x32, ServerPlayerPositionAndLookPacket.class);
                put(0x1B, ServerDisconnectPacket.class);
                put(0x37, ServerResourcePackSendPacket.class);
                put(0x19, ServerPluginMessagePacket.class);
                put(0x44, ServerUpdateHealthPacket.class);
                put(0x25, ServerJoinGamePacket.class);
                put(0x30, ServerPlayerListItemPacket.class);
                put(0x13, ServerCloseWindowPacket.class);
                put(0x14, ServerOpenWindowPacket.class);
                put(0x15, ServerWindowItemsPacket.class);
                put(0x17, ServerSetSlotPacket.class);
                put(0x12, ServerConfirmTransactionPacket.class);
                put(0x4A, ServerTimeUpdatePacket.class);

                put(0x03, ServerSpawnEntityPacket.class);
                put(0x05, ServerSpawnPlayerPacket.class);
                put(0x35, ServerDestroyEntitiesPacket.class);
                put(0x28, ServerEntityRelativeMovePacket.class);
                put(0x29, ServerEntityRelativeMovePacket.class);
                put(0x50, ServerEntityTeleportPacket.class);
            }
        };
    }

}
