package net.defekt.mc.chatclient.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.defekt.mc.chatclient.protocol.data.ChatMessages;
import net.defekt.mc.chatclient.protocol.data.ModInfo;
import net.defekt.mc.chatclient.protocol.data.StatusInfo;
import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.packets.HandshakePacket;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketFactory;

/**
 * This class contains methods for getting status of a Minecraft server and
 * discovering servers on LAN netowrk
 * 
 * @see LANListener
 * @see StatusInfo
 * @author Defective4
 *
 */
public class MinecraftStat {
	/**
	 * Perform a Server List Ping on specified server to get its status
	 * 
	 * @param host hostname of target server
	 * @param port port of target server
	 * @return An object containing data returned by server
	 * @throws IOException thrown when there was an error pinging target server
	 */
	public static StatusInfo serverListPing(final String host, final int port) throws IOException {
		try (Socket soc = new Socket()) {
			soc.connect(new InetSocketAddress(host, port));

			final OutputStream os = soc.getOutputStream();
			final VarInputStream is = new VarInputStream(soc.getInputStream());

			final Packet handshake = new HandshakePacket(PacketFactory.constructPacketRegistry(47), -1, host, port, 1);
			os.write(handshake.getData(false));
			os.write(0x01);
			os.write(0x00);

			final int len = is.readVarInt();
			if (len <= 0)
				throw new IOException("Invalid packet length received: " + Integer.toString(len));

			final int id = is.readVarInt();
			if (id != 0x00)
				throw new IOException("Invalid packet ID received: 0x" + Integer.toHexString(id));

			final String json = is.readString();
			soc.close();

			final JsonObject obj = new JsonParser().parse(json).getAsJsonObject();

			final int online = obj.get("players").getAsJsonObject().get("online").getAsInt();
			final int max = obj.get("players").getAsJsonObject().get("max").getAsInt();
			final String version = obj.get("version").getAsJsonObject().get("name").getAsString();
			final int protocol = obj.get("version").getAsJsonObject().get("protocol").getAsInt();

			String description;
			try {
				description = ChatMessages.parse(obj.get("description").toString());
			} catch (final Exception e) {
				description = obj.get("description").toString();
			}

			final String icon = obj.has("favicon") ? obj.get("favicon").getAsString() : null;

			String modType = null;
			final List<ModInfo> modList = new ArrayList<ModInfo>();

			if (obj.has("modinfo")) {
				final JsonObject modinfo = (JsonObject) obj.get("modinfo");
				modType = modinfo.get("type").getAsString();
				final JsonArray mods = modinfo.get("modList").getAsJsonArray();
				for (final JsonElement modElement : mods) {
					final JsonObject modObject = modElement.getAsJsonObject();
					modList.add(
							new ModInfo(modObject.get("modid").getAsString(), modObject.get("version").getAsString()));
				}
			}

			return new StatusInfo(description, online, max, version, protocol, icon, modType, modList);

		}
	}

	// TODO Documentation, query and RCON

	/**
	 * @param host hostname of a target server
	 * @param port port of the server
	 * @return An object containing data returned by server
	 * @throws IOException thrown when there was an error pinging target server
	 */
	public static StatusInfo legacyServerListPing(final String host, final int port) throws IOException {
		try (Socket soc = new Socket(host, port)) {
			final OutputStream os = soc.getOutputStream();
			final InputStream is = soc.getInputStream();
			os.write(0xFE);

			final byte[] buffer = new byte[1024];
			final byte[] stringBytes = Arrays.copyOf(buffer, is.read(buffer));
			soc.close();
			String data = new String(stringBytes, "UTF-16LE");
			data = data.substring(1, data.length() - 1);

			final int paraCount = data.length() - data.replace("\u00a7", "").length();
			String playersString = data;
			for (int x = 0; x < paraCount - 1; x++) {
				playersString = playersString.substring(playersString.indexOf("\u00a7") + 1);
			}

			final String motd = data.substring(0, data.lastIndexOf(playersString) - 1);

			int online = 0;
			int max = 0;

			final String[] players = playersString.split("\u00a7");
			online = Integer.parseInt(players[0]);
			max = Integer.parseInt(players[1]);
			return new StatusInfo(motd, online, max, "\u00a7cLegacy", -1, null, null, new ArrayList<ModInfo>());
		}
	}

	/**
	 * Starts listening for LAN servers
	 * 
	 * @param listener listener used to handle discovered servers
	 */
	public static void listenOnLAN(final LANListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try (MulticastSocket soc = new MulticastSocket(4445)) {
					final String lanIP = "224.0.2.60";
					soc.joinGroup(InetAddress.getByName(lanIP));
					final byte[] recv = new byte[1024];
					while (true) {
						try {
							final DatagramPacket packet = new DatagramPacket(recv, recv.length);
							soc.receive(packet);
							final String msg = new String(recv).trim();
							final String motd = msg.substring(6, msg.lastIndexOf("[/MOTD]"));
							int port = 25565;
							try {
								port = Integer
										.parseInt(msg.substring(msg.lastIndexOf("[AD]") + 4, msg.lastIndexOf("[/AD]")));
							} catch (final Exception e) {
								e.printStackTrace();
							}
							listener.serverDiscovered(packet.getAddress(), motd, port);

						} catch (final Exception e) {
							e.printStackTrace();
						}
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
