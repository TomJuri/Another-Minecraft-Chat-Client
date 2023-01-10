package net.defekt.mc.chatclient.protocol.packets.v1_19.clientbound.play;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.defekt.mc.chatclient.protocol.data.ChatMessages;
import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPlayerListItemPacket.Action;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPlayerListItemPacket.PlayerListItem;

/**
 * Sent by server when player's TAB list is updatet
 * 
 * @author Defective4
 *
 */
public class ServerPlayerListItemPacket extends Packet {
    /**
     * Player List action type
     * 
     * @author Defective4
     *
     */

    private final Action action;
    private final int players;

    private final List<PlayerListItem> playersList = new ArrayList<PlayerListItem>();

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

    /**
     * Get Player List action
     * 
     * @return player list action
     */
    public Action getAction() {
        return action;
    }

    /**
     * Get player list max players count
     * 
     * @return max players
     */
    public int getPlayers() {
        return players;
    }

    /**
     * Get UUID of player involved in this packet
     * 
     * @deprecated as multiple players can now be stored in this packet, this method
     *             will only return value of first player stored in packet.
     * @return player's UUID
     */
    @Deprecated
    public UUID getUUID() {
        return playersList.get(0).getUuid();
    }

    /**
     * Get name of player
     * 
     * @return player's name
     */
    @Deprecated
    public String getPlayerName() {
        return playersList.get(0).getPlayerName();
    }

    /**
     * Get player's skin data
     * 
     * @deprecated as multiple players can now be stored in this packet, this method
     *             will only return value of first player stored in packet.
     * @return player's skin data, or null if none
     */
    @Deprecated
    public String getTextures() {
        return playersList.get(0).getTextures();
    }

    /**
     * Get player's display name
     * 
     * @deprecated as multiple players can now be stored in this packet, this method
     *             will only return value of first player stored in packet.
     * @return player's display name, or null if none
     */
    @Deprecated
    public String getDisplayName() {
        return playersList.get(0).getDisplayName();
    }

    /**
     * Get player's latency
     * 
     * @deprecated as multiple players can now be stored in this packet, this method
     *             will only return value of first player stored in packet.
     * @return player's latency, or -1 if unknown
     */
    @Deprecated
    public int getPing() {
        return playersList.get(0).getPing();
    }

    /**
     * Get list of players stored in this packet
     * 
     * @return player list
     */
    public List<PlayerListItem> getPlayersList() {
        return new ArrayList<PlayerListItem>(playersList);
    }

}
