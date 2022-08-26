package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.defekt.mc.chatclient.protocol.data.ChatMessages;
import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Sent by server when player's TAB list is updatet
 * 
 * @author Defective4
 *
 */
public class ServerPlayerListItemPacket extends Packet {

    /**
     * A container for player information on player list
     * 
     * @author Defective4
     *
     */
    public static class PlayerListItem {
        private final UUID uuid;
        private String playerName = null;
        private String textures = null;
        private String displayName = null;
        private int ping = -1;

        /**
         * Construct player list item
         * 
         * @param uuid        player's unique ID
         * @param playerName  player name
         * @param textures    encoded player textures
         * @param displayName player's display name
         * @param ping        player's ping
         */
        public PlayerListItem(final UUID uuid, final String playerName, final String textures, final String displayName,
                final int ping) {
            super();
            this.uuid = uuid;
            this.playerName = playerName;
            this.textures = textures;
            this.displayName = displayName;
            this.ping = ping;
        }

        /**
         * Get player's unique ID
         * 
         * @return player's UUID
         */
        public UUID getUuid() {
            return uuid;
        }

        /**
         * Get player's name
         * 
         * @return name
         */
        public String getPlayerName() {
            return playerName;
        }

        /**
         * Get player's textures
         * 
         * @return base64 textures
         */
        public String getTextures() {
            return textures;
        }

        /**
         * Get player's display name
         * 
         * @return display name
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * Get player's ping
         * 
         * @return ping
         */
        public int getPing() {
            return ping;
        }
    }

    /**
     * Player List action type
     * 
     * @author Defective4
     *
     */
    public enum Action {
        /**
         * A player is added
         */
        ADD_PLAYER(0),
        /**
         * Player's gamemode is updated
         */
        UPDATE_GAMEMODE(1),
        /**
         * Player's latency is updated
         */
        UPDATE_LATENCY(2),
        /**
         * Player's display name is updated
         */
        UPDATE_DISPLAY_NAME(3),
        /**
         * A player is removed
         */
        REMOVE_PLAYER(4);

        /**
         * Action number
         */
        protected final int number;

        private Action(final int num) {
            this.number = num;
        }

        /**
         * Get Action for action ID
         * 
         * @param num action ID
         * @return corresponding Action, or null if not found
         */
        public static Action getForNumber(final int num) {
            for (final Action action : values())
                if (action.number == num) return action;
            return null;
        }
    }

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
        return new ArrayList<ServerPlayerListItemPacket.PlayerListItem>(playersList);
    }

}
