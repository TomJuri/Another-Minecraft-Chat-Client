package net.defekt.mc.chatclient.protocol.packets.general.clientbound.login;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Sent by server during login process to request player authentication. AMCC
 * does not support this at the moment
 *
 * @author Defective4
 */
public class ServerLoginEncryptionPacket extends Packet {

    private final String serverID;
    private final byte[] publicKey;
    private final byte[] verifyToken;

    /**
     * Contructs {@link ServerLoginEncryptionPacket}
     *
     * @param reg  packet registry used to construct this packet
     * @param data packet's data
     * @throws IOException never thrown
     */
    public ServerLoginEncryptionPacket(final PacketRegistry reg, final byte[] data) throws IOException {
        super(reg, data);
        final VarInputStream is = getInputStream();
        serverID = is.readString();
        publicKey = new byte[is.readVarInt()];
        is.read(publicKey);
        verifyToken = new byte[is.readVarInt()];
        is.read(verifyToken);
    }

    /**
     * @return server's ID. Empty after Minecraft 1.7
     */
    public String getServerID() {
        return serverID;
    }

    /**
     * @return server's public key.
     * @throws InvalidKeySpecException  if the key is somehow invalid
     * @throws NoSuchAlgorithmException never thrown
     */
    public PublicKey getPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        final KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(new X509EncodedKeySpec(publicKey));
    }

    /**
     * @return verification token sent by server
     */
    public byte[] getVerifyToken() {
        return verifyToken;
    }

}
