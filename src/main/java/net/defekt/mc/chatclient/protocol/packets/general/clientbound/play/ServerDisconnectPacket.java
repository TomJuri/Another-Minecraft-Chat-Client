package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Sent by server when client is disconnected from server (Timed Out, kicked,
 * etc.)
 * 
 * @author Defective4
 *
 */
public class ServerDisconnectPacket extends Packet {

	private final String reason;

	/**
	 * Constructs {@link ServerDisconnectPacket}
	 * 
	 * @param reg  packet registry used to construct this packet
	 * @param data packet's data
	 * @throws IOException never thrown
	 */
	public ServerDisconnectPacket(final PacketRegistry reg, final byte[] data) throws IOException {
		super(reg, data);
		reason = getInputStream().readString();
	}

	/**
	 * Get raw JSON reason message
	 * 
	 * @return raw JSON reason
	 */
	public String getReason() {
		return reason;
	}

}
