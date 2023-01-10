package net.defekt.mc.chatclient.protocol.data;

import java.io.Serializable;
import java.util.UUID;

/**
 * A container used to store information about a player
 * 
 * @see ServerPlayerListItemPacket
 * @author Defective4
 *
 */
public class PlayerInfo implements Serializable {
    private static final long serialVersionUID = -1040109725883178962L;

    private final String name;
    private final String displayName;
    private String texture;
    private final int ping;
    private final UUID uuid;

    /**
     * Create Player Info object
     * 
     * @param name        player's real name
     * @param texture     player's skin url, may be null
     * @param displayName player's display name
     * @param ping        player ping
     * @param uuid        player's UUID
     */
    public PlayerInfo(final String name, final String texture, final String displayName, final int ping,
            final UUID uuid) {
        this.displayName = displayName;
        this.name = name;
        this.texture = texture;
        this.ping = ping;
        this.uuid = uuid;
    }

    /**
     * Get player's real name
     * 
     * @return player's real name
     */
    public String getName() {
        return name;
    }

    /**
     * Get player's display name
     * 
     * @return player's display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get player's skin texture URL
     * 
     * @return player's skin url, "default" if none
     */
    public String getTexture() {
        return texture != null && !texture.isEmpty() ? texture : "default";
    }

    /**
     * Get player's ping
     * 
     * @return player's ping
     */
    public int getPing() {
        return ping;
    }

    /**
     * Get player's UUID
     * 
     * @return player's UUID
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Set player's texture
     * 
     * @param texture player's texture
     */
    public void setTexture(final String texture) {
        this.texture = texture;
    }
}
