package net.defekt.mc.chatclient.protocol.packets.general.clientbound.login;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Sent by server during login process to request player authentication. AMCC
 * does not support this at the moment
 * 
 * @author Defective4
 *
 */
public class ServerLoginEncryptionPacket extends Packet {

	/**
	 * Contructs {@link ServerLoginEncryptionPacket}
	 * 
	 * @param reg  packet registry used to construct this packet
	 * @param data packet's data
	 * @throws IOException never thrown
	 */
	public ServerLoginEncryptionPacket(final PacketRegistry reg, final byte[] data) throws IOException {
		super(reg, data);

	}

}
