package net.defekt.mc.chatclient.protocol.entity;

import java.util.UUID;

/**
 * This class is an extension of {@link Entity}.<br>
 * It's used to store information about players.
 * 
 * @author Defective4
 *
 */
public class Player extends Entity {
    /**
     * Default constructor
     * 
     * @param uid player's UID
     * @param x   player's X
     * @param y   player's Y
     * @param z   player's Z
     */
    public Player(final UUID uid, final double x, final double y, final double z) {
        super(uid, -1, x, y, z);
    }
}
