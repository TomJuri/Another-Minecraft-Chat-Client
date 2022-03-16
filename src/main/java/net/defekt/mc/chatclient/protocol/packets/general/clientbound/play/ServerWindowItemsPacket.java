package net.defekt.mc.chatclient.protocol.packets.general.clientbound.play;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.defekt.mc.chatclient.protocol.data.ItemStack;
import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketFactory;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;

/**
 * Send by server when window items are set
 * 
 * @author Defective4
 *
 */
public class ServerWindowItemsPacket extends Packet {

	private final int windowID;
	private final List<ItemStack> items = new ArrayList<ItemStack>();

	/**
	 * Constructs {@link ServerWindowItemsPacket}
	 * 
	 * @param reg  packet registry used to construct this packet
	 * @param data packet's data
	 * @throws IOException never thrown
	 */
	public ServerWindowItemsPacket(final PacketRegistry reg, final byte[] data) throws IOException {
		super(reg, data);
		final VarInputStream is = getInputStream();
		windowID = is.readUnsignedByte();
		final short count = is.readShort();
		for (int x = 0; x < count; x++) {
			items.add(is.readSlotData(PacketFactory.getProtocolFor(reg)));
		}
	}

	/**
	 * Get window's ID
	 * 
	 * @return window ID
	 */
	public int getWindowID() {
		return windowID;
	}

	/**
	 * Get items from this packet
	 * 
	 * @return items list
	 */
	public List<ItemStack> getItems() {
		return new ArrayList<ItemStack>(items);
	}

}
