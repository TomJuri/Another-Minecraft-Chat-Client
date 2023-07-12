package net.defekt.mc.chatclient.protocol.entity;

import java.util.UUID;

/**
 * This class is used to store information about an entity sent to client by
 * server
 *
 * @author Defective4
 */
public class Entity {
    private final UUID uid;
    private final int type;
    private double x;
    private double y;
    private double z;

    /**
     * Default constructor
     *
     * @param uid  entity's unique ID
     * @param type entity type
     * @param x    entity's X coordinate
     * @param y    entity's Y coordinate
     * @param z    entity's Z coordinate
     */
    public Entity(final UUID uid, final int type, final double x, final double y, final double z) {
        super();
        this.uid = uid;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Get entity's UID
     *
     * @return entity UID
     */
    public UUID getUid() {
        return uid;
    }

    /**
     * Get entity type
     *
     * @return entity type
     */
    public int getType() {
        return type;
    }

    /**
     * Get entity's X Coordinate
     *
     * @return X coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Set entity's X position
     *
     * @param x new X position
     */
    public void setX(final double x) {
        this.x = x;
    }

    /**
     * Get entity's Y Coordinate
     *
     * @return Y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Set entity's Y position
     *
     * @param y new Y position
     */
    public void setY(final double y) {
        this.y = y;
    }

    /**
     * Get entity's Z Coordinate
     *
     * @return Z coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * Set entity's Z position
     *
     * @param z new Z position
     */
    public void setZ(final double z) {
        this.z = z;
    }

}
