package net.defekt.mc.chatclient.protocol.packets.general.serverbound.play;

import java.io.IOException;

import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * A client's plugin message packet
 * 
 * @author Defective4
 *
 */
public class ClientPluginMessagePacket extends Packet {

	/**
	 * Constructs new {@link ClientPluginMessagePacket}
	 * 
	 * @param reg     packet registry used to construct this packet
	 * @param channel plugin channel
	 * @param data    plugin message data
	 * @throws IOException never thrown
	 */
	public ClientPluginMessagePacket(final PacketRegistry reg, final String channel, final byte[] data)
			throws IOException {
		super(reg);
		putString(channel);
		putBytes(data);
	}

}
