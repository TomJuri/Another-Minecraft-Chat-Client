package net.defekt.mc.chatclient.protocol.packets.registry;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.alt.clientbound.login.ServerLoginSuccessPacket;
import net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerOpenWindowPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginEncryptionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginResponsePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginSetCompressionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.*;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.login.ClientLoginRequestPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A packet registry implementation for protocol 751
 *
 * @author Defective4
 */
public class PacketRegistryV753 extends PacketRegistry {

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
                put(0x10, ClientKeepAlivePacket.class);
                put(0x03, ClientChatMessagePacket.class);
                put(0x21, ClientResourcePackStatusPacket.class);
                put(0x0B, ClientPluginMessagePacket.class);
                put(0x04, ClientStatusPacket.class);
                put(0x1C, ClientEntityActionPacket.class);
                put(0x12, ClientPlayerPositionPacket.class);
                put(0x13, ClientPlayerPositionAndLookPacket.class);
                put(0x09, ClientWindowClickPacket.class);
                put(0x0A, ClientCloseWindowPacket.class);
                put(0x25, ClientHeldItemChangePacket.class);
                put(0x1B, ClientPlayerDiggingPacket.class);
                put(0x2F, ClientUseItemPacket.class);
                put(0x07, ClientConfirmTransactionPacket.class);
                put(0x0E, ClientUseEntityPacket.class);
                put(0x2C, ClientAnimationPacket.class);
            }
        };
    }

    @Override
    protected Map<Integer, Class<? extends Packet>> initInPackets() {
        return new HashMap<Integer, Class<? extends Packet>>() {
            private static final long serialVersionUID = 1L;

            {
                put(0x1F, ServerKeepAlivePacket.class);
                put(0x0E, ServerChatMessagePacket.class);
                put(0x34, ServerPlayerPositionAndLookPacket.class);
                put(0x19, ServerDisconnectPacket.class);
                put(0x38, ServerResourcePackSendPacket.class);
                put(0x17, ServerPluginMessagePacket.class);
                put(0x49, ServerUpdateHealthPacket.class);
                put(0x24, ServerJoinGamePacket.class);
                put(0x32, ServerPlayerListItemPacket.class);
                put(0x12, ServerCloseWindowPacket.class);
                put(0x2D, ServerOpenWindowPacket.class);
                put(0x13, ServerWindowItemsPacket.class);
                put(0x15, ServerSetSlotPacket.class);
                put(0x11, ServerConfirmTransactionPacket.class);
                put(0x4E, ServerTimeUpdatePacket.class);

                put(0x02, ServerSpawnEntityPacket.class);
                put(0x04, ServerSpawnPlayerPacket.class);
                put(0x36, ServerDestroyEntitiesPacket.class);
                put(0x27, ServerEntityRelativeMovePacket.class);
                put(0x28, ServerEntityRelativeMovePacket.class);
                put(0x56, ServerEntityTeleportPacket.class);
            }
        };
    }

}
