package net.defekt.mc.chatclient.protocol.packets.general.clientbound.login;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * An universal login response packet used by server to send a text message to
 * client
 * 
 * @author Defective4
 *
 */
public class ServerLoginResponsePacket extends Packet {

	private final String response;

	/**
	 * Contructs {@link ServerLoginResponsePacket}
	 * 
	 * @param reg  packet registry used to construct this packet
	 * @param data packet's data
	 * @throws IOException never thrown
	 */
	public ServerLoginResponsePacket(final PacketRegistry reg, final byte[] data) throws IOException {
		super(reg, data);
		this.response = getInputStream().readString();
	}

	/**
	 * Get message sent by server
	 * 
	 * @return server message
	 */
	public String getResponse() {
		return response;
	}

}
