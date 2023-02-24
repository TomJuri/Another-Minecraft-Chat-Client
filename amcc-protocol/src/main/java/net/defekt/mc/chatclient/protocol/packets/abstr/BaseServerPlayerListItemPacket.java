package net.defekt.mc.chatclient.protocol.packets.abstr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Base class for all clientbound player info packets
 * 
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public class BaseServerPlayerListItemPacket extends Packet {

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

    /**
     * Player list action
     */
    protected Action action;

    /**
     * Total entries
     */
    protected int players;

    /**
     * List of player infos
     */
    protected List<PlayerListItem> playersList = new ArrayList<PlayerListItem>();

    /**
     * Default constructor
     * 
     * @param reg
     * @param data
     * @throws IOException
     */
    protected BaseServerPlayerListItemPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
    }

    public Action getAction() {
        return action;
    }

    public int getPlayers() {
        return players;
    }

    public List<PlayerListItem> getPlayersList() {
        return playersList;
    }

}
