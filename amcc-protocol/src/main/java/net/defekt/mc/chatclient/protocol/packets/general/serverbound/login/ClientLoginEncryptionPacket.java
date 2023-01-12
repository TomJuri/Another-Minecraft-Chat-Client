package net.defekt.mc.chatclient.protocol.packets.general.serverbound.login;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginEncryptionPacket;

/**
 * Sent by client as a response to {@link ServerLoginEncryptionPacket}
 * 
 * @author Defective4
 *
 */
public class ClientLoginEncryptionPacket extends Packet {

    /**
     * Constructs new {@link ClientLoginEncryptionPacket}
     * 
     * @param reg             packet registry used to contruct this packet
     * @param encryptedSecret client's secret encrypted with server's public key
     * @param encryptedToken  server verification token encrypted with server's
     *                        public key
     */
    public ClientLoginEncryptionPacket(final PacketRegistry reg, final byte[] encryptedSecret,
            final byte[] encryptedToken) {
        super(reg);
        id = 0x01;
        putVarInt(encryptedSecret.length);
        putBytes(encryptedSecret);
        putVarInt(encryptedToken.length);
        putBytes(encryptedToken);
    }

}
