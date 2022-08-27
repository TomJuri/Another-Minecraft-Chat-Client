package net.defekt.mc.chatclient.protocol.entity;

import java.util.UUID;

public class Player extends Entity {
    public Player(final UUID uid, final double x, final double y, final double z) {
        super(uid, -1, x, y, z);
    }
}
