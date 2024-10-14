package net.defekt.mc.chatclient.protocol.io;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.api.Tag;
import dev.dewy.nbt.tags.collection.CompoundTag;
import net.defekt.mc.chatclient.protocol.data.ItemStack;
import net.defekt.mc.chatclient.protocol.packets.Packet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * An extension of {@link DataOutputStream} with methods to write out Minecraft'
 * data types
 *
 * @author Defective4
 * @see VarInputStream
 * @see Packet
 */
public class VarOutputStream extends DataOutputStream {

    private final Nbt nbt = new Nbt();

    /**
     * Wrap output stream in {@link VarOutputStream}
     *
     * @param out output stream to wrap
     */
    public VarOutputStream(final OutputStream out) {
        super(out);
    }

    /**
     * Checks how many bytes will this int take after creating VarInt from it
     *
     * @param v VarInt value
     * @return size of VarInt
     */
    public static int checkVarIntSize(final int v) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final VarOutputStream vos = new VarOutputStream(bos);
        try {
            vos.writeVarInt(v);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
        return bos.size();
    }

    /**
     * Write a string to stream<br>
     * This method first writes VarInt equal string's length, then it writes string
     * as byte array
     *
     * @param v string to write
     * @throws IOException thrown when there was an error writting to stream
     */
    public void writeString(final String v) throws IOException {
        final byte[] sBytes = v.getBytes(StandardCharsets.UTF_8);
        writeVarInt(sBytes.length);
        write(sBytes);
    }

    /**
     * Writes item data to stream
     *
     * @param is       item stack to write
     * @param protocol protocol determining slot data used
     * @throws IOException thrown when there was an error writing to stream
     */
    public void writeSlotData(final ItemStack is, final int protocol) throws IOException {
        if (protocol >= 477) {
            writeBoolean(is.getId() != 0);
            writeVarInt(is.getId());
        } else {
            writeShort(is.getId());
            if (is.getId() == -1) return;
        }
        writeByte(is.getCount());
        if (protocol < 393) {
            writeShort(is.getDamage());
        }
        if (is.getNbt() == null) {
            writeByte(0);
        } else {
            final Tag tg = is.getNbt();
            if (tg instanceof CompoundTag) nbt.toStream((CompoundTag) tg, this);
        }
    }

    /**
     * Writes VarInt to stream.<br>
     * Snippet from
     * <a href="https://wiki.vg/Protocol#VarInt_and_VarLong">wiki.vg</a>
     *
     * @param value VarInt value
     * @throws IOException thrown when there was an error writing to stream
     */
    public void writeVarInt(int value) throws IOException {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            writeByte(temp);
        } while (value != 0);
    }

}
