package net.defekt.mc.chatclient.protocol.packets.v1_19_2.clientbound.play;

import java.io.IOException;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket.Position;

public class ServerPlayerChatMessagePacket extends Packet {

    private final String message;
    private final byte position;

    /**
     * Constructs {@link ServerPlayerChatMessagePacket}
     * 
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerPlayerChatMessagePacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        if (is.readBoolean()) {
            is.skip(is.readVarInt());
        }
        UUID uid = is.readUUID();
        is.skip(is.readVarInt());
        String msg = is.readString();
        JsonObject obj = new JsonObject();
        obj.add("translate", new JsonPrimitive("chat.type.text"));
        JsonArray ar = new JsonArray();
        JsonObject sender = new JsonObject();
        JsonObject msgJ = new JsonObject();

        sender.add("amcPlayer", new JsonPrimitive(uid.toString()));
        msgJ.add("text", new JsonPrimitive(msg));

        ar.add(sender);
        ar.add(msgJ);
        obj.add("with", ar);

        msg = obj.toString();
        if (is.readBoolean()) msg = is.readString();
        this.message = msg;
        position = 0;
    }

    /**
     * Get JSON message
     * 
     * @return raw JSON message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get message's position
     * 
     * @return message's position
     */
    public Position getPosition() {
        switch (position) {
            case 0: {
                return Position.CHAT;
            }
            case 1: {
                return Position.SYSTEM;
            }
            case 2: {
                return Position.HOTBAR;
            }
            default: {
                return Position.CHAT;
            }
        }
    }

}
