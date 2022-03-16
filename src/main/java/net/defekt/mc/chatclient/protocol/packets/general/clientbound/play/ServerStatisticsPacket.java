package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientStatusPacket;

/**
 * Sent by server as response to client status packet
 * 
 * @see ClientStatusPacket
 * @author Defective4
 *
 */
public class ServerStatisticsPacket extends Packet {

	private final Map<String, Integer> values = new HashMap<String, Integer>();

	/**
	 * constructs {@link ServerJoinGamePacket}
	 * 
	 * @param reg  packet registry used to construct this packet
	 * @param data packet's data
	 * @throws IOException never thrown
	 */
	public ServerStatisticsPacket(final PacketRegistry reg, final byte[] data) throws IOException {
		super(reg, data);
		final VarInputStream is = getInputStream();

		final int count = is.readVarInt();
		for (int x = 0; x < count; x++) {
			final String key = is.readString();
			final int value = is.readVarInt();
			values.put(key, value);
		}
	}

	/**
	 * Get received values
	 * 
	 * @return map of received values
	 */
	public Map<String, Integer> getValues() {
		return values;
	}

}
