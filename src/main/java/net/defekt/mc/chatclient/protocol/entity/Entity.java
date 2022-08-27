package net.defekt.mc.chatclient.protocol.entity;

import java.util.UUID;

public class Entity {
    private final UUID uid;
    private final int type;
    private double x;
    private double y;
    private double z;

    public Entity(final UUID uid, final int type, final double x, final double y, final double z) {
        super();
        this.uid = uid;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public UUID getUid() {
        return uid;
    }

    public int getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public void setZ(final double z) {
        this.z = z;
    }

}
