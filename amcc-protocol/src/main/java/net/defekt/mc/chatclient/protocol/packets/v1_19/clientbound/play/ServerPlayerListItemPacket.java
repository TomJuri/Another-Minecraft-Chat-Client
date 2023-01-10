package net.defekt.mc.chatclient.protocol.packets.v1_19.clientbound.play;

import java.io.IOException;
import java.util.UUID;

import net.defekt.mc.chatclient.protocol.data.ChatMessages;
import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.abstr.AbstractServerPlayerListItemPacket;

/**
 * Sent by server when player's TAB list is updatet
 * 
 * @author Defective4
 *
 */
public class ServerPlayerListItemPacket extends AbstractServerPlayerListItemPacket {

    /**
     * constructs {@link ServerPlayerListItemPacket}
     * 
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerPlayerListItemPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();

        action = Action.getForNumber(is.readVarInt());
        players = is.readVarInt();

        for (int i = 0; i < players; i++) {

            final UUID uid = is.readUUID();
            String playerName = "";
            String textures = "";
            String displayName = "";
            int ping = -1;
            switch (action) {
                case ADD_PLAYER: {
                    playerName = is.readString();
                    final int propertiesNum = is.readVarInt();
                    textures = null;
                    displayName = null;

                    for (int x = 0; x < propertiesNum; x++) {
                        final String pName = is.readString();
                        final String value = is.readString();
                        final boolean isSigned = is.readBoolean();
                        if (isSigned) {
                            is.readString();
                        }

                        if (pName.equals("textures")) {
                            textures = value;
                        }
                    }

                    is.readVarInt();
                    ping = is.readVarInt();
                    if (is.readBoolean()) {
                        displayName = is.readString();
                    }

                    boolean hasSigData = is.readBoolean();
                    if (hasSigData) {
                        is.readLong();
                        is.skip(is.readVarInt());
                        is.skip(is.readVarInt());
                    }

                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    if (is.readBoolean()) {
                        displayName = is.readString();
                    }
                    break;
                }
                case UPDATE_LATENCY: {
                    ping = is.readVarInt();
                    break;
                }
                default: {
                    break;
                }
            }

            if (displayName != null) {
                displayName = ChatMessages.parse(displayName);
            }
            playersList.add(new PlayerListItem(uid, playerName, textures, displayName, ping));
        }
    }

}
