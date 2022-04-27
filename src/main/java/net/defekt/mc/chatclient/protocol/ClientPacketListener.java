package net.defekt.mc.chatclient.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.defekt.mc.chatclient.protocol.MojangAPI.RequestResponse;
import net.defekt.mc.chatclient.protocol.data.ChatMessages;
import net.defekt.mc.chatclient.protocol.data.Hosts;
import net.defekt.mc.chatclient.protocol.data.ItemStack;
import net.defekt.mc.chatclient.protocol.data.ItemsWindow;
import net.defekt.mc.chatclient.protocol.data.ModInfo;
import net.defekt.mc.chatclient.protocol.data.PlayerInfo;
import net.defekt.mc.chatclient.protocol.data.StatusInfo;
import net.defekt.mc.chatclient.protocol.io.IOUtils;
import net.defekt.mc.chatclient.protocol.io.VarOutputStream;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketFactory;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry.State;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginEncryptionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginResponsePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginSetCompressionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginSuccessPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket.Position;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerCloseWindowPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerConfirmTransactionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerDisconnectPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerJoinGamePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerKeepAlivePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerOpenWindowPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPlayerListItemPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPlayerListItemPacket.Action;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPlayerListItemPacket.PlayerListItem;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPlayerPositionAndLookPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPluginMessagePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerResourcePackSendPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerSetSlotPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerStatisticsPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerTimeUpdatePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerUpdateHealthPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerWindowItemsPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.login.ClientLoginEncryptionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientPluginMessagePacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientTeleportConfirmPacket;
import net.defekt.mc.chatclient.ui.Main;
import net.defekt.mc.chatclient.ui.Messages;
import net.defekt.mc.chatclient.ui.UserPreferences;

/**
 * An implementation of {@link InternalPacketListener} responsible for handling
 * all packets received from server. It is added to every instance of
 * {@link MinecraftClient} after successfully connecting to server.
 * 
 * @see MinecraftClient
 * @see ClientListener
 * @author Defective4
 *
 */
public class ClientPacketListener implements InternalPacketListener {

    private final int protocol;
    private final MinecraftClient cl;
    private final UserPreferences up = Main.up;

    private final Timer keepAliveTimer = new Timer("keepAliveTimer", true);
    private long lastKeepAlivePacket = System.currentTimeMillis();

    /**
     * Constructs packet listener bound to specified client
     * 
     * @param client A Minecraft client this listener is bound o
     */
    protected ClientPacketListener(final MinecraftClient client) {
        this.cl = client;
        this.protocol = cl.getProtocol();
        keepAliveTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (!cl.isConnected()) {
                    keepAliveTimer.cancel();
                    return;
                }

                if (System.currentTimeMillis() - lastKeepAlivePacket > 60000) {
                    for (final ClientListener cls : cl.getClientListeners()) {
                        cls.disconnected("Timed Out");
                    }
                    cl.close();
                }
            }
        }, 0, 1000);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void packetReceived(final Packet packet, final PacketRegistry registry) {
        try {
            if (packet instanceof ServerLoginSetCompressionPacket) {
                cl.setCompression(true);
            } else if (packet instanceof ServerLoginEncryptionPacket) {
                ServerLoginEncryptionPacket sPacket = (ServerLoginEncryptionPacket) packet;
                switch (cl.getAuthType()) {
                    default:
                    case Offline: {
                        for (final ClientListener ls : cl.getClientListeners()) {
                            ls.disconnected(Messages.getString("MinecraftClient.clientErrorDisconnectedNoAuth"));
                        }
                        cl.close();
                        break;
                    }
                    case Mojang: {
                        PublicKey publicKey = sPacket.getPublicKey();
                        byte[] verifyToken = sPacket.getVerifyToken();
                        String serverID = sPacket.getServerID();

                        byte[] clientSecret = new byte[16];
                        new SecureRandom().nextBytes(clientSecret);

                        Cipher rsa = Cipher.getInstance("RSA");
                        rsa.init(Cipher.ENCRYPT_MODE, publicKey);

                        byte[] encryptedSecret = rsa.doFinal(clientSecret);
                        byte[] encryptedToken = rsa.doFinal(verifyToken);

                        String sha = IOUtils.sha1(serverID.getBytes(), clientSecret, publicKey.getEncoded());

                        RequestResponse resp = MojangAPI.makeJSONRequest(
                                (cl.getAuthType() == AuthType.Mojang ? Hosts.MOJANG_SESSIONSERVER
                                        : Hosts.ALTENING_SESSIONSERVER) + "/session/minecraft/join",
                                new HashMap<String, JsonElement>() {
                                    {
                                        put("accessToken", new JsonPrimitive(cl.getAuthToken()));
                                        put("selectedProfile", new JsonPrimitive(cl.getAuthID()));
                                        put("serverId", new JsonPrimitive(sha));
                                    }
                                }); // TODO TheAltening
                        try {
                            JsonObject json = resp.getJson();
                            if (json.has("error")) {
                                String errMsg = json.has("errorMessage") ? json.get("errorMessage").getAsString()
                                        : json.get("error").getAsString();
                                for (final ClientListener ls : cl.getClientListeners()) {
                                    ls.disconnected(
                                            Messages.getString("MinecraftClient.clientErrorDisconnected") + errMsg);
                                }
                                cl.close();
                                break;
                            }
                        } catch (Exception ex) {
                        }

                        cl.sendPacket(new ClientLoginEncryptionPacket(registry, encryptedSecret, encryptedToken));
                        cl.enableEncryption(clientSecret);
                        break;
                    }
                }
            } else if (packet instanceof ServerLoginResponsePacket) {
                for (final ClientListener ls : cl.getClientListeners()) {
                    ls.disconnected(ChatMessages.parse(((ServerLoginResponsePacket) packet).getResponse()));
                }
                cl.close();
            } else if (packet instanceof ServerTimeUpdatePacket) {
                final ServerTimeUpdatePacket sti = (ServerTimeUpdatePacket) packet;
                for (final ClientListener cls : cl.getClientListeners()) {
                    cls.timeUpdated(sti.getTime(), sti.getWorldAge());
                }
            } else if (packet instanceof ServerConfirmTransactionPacket) {
                if (!up.isEnableInventoryHandling() || protocol >= 755)
                    return;

                final int windowID = (int) packet.accessPacketMethod("getWindowID");
                final short actionID = (short) packet.accessPacketMethod("getActionID");
                final boolean accepted = (boolean) packet.accessPacketMethod("isAccepted");

                final ItemsWindow win = windowID == 0 ? cl.getInventory()
                        : cl.getOpenWindows().containsKey(windowID) ? cl.getOpenWindows().get(windowID) : null;

                if (win != null)
                    if (accepted) {
                        win.finishTransaction(actionID);
                    } else {
                        win.cancelTransaction(actionID);
                    }

                if (!accepted) {
                    cl.sendPacket(PacketFactory.constructPacket(registry, "ClientConfirmTransactionPacket",
                            (byte) windowID, actionID, accepted));
                }

            } else if (packet instanceof ServerSetSlotPacket) {
                if (!up.isEnableInventoryHandling() || protocol >= 755)
                    return;

                final int windowID = (int) packet.accessPacketMethod("getWindowID");

                final short slot = (short) packet.accessPacketMethod("getSlot");
                final ItemStack item = (ItemStack) packet.accessPacketMethod("getItem");
                if (windowID == 0
                        || (cl.getOpenWindows().containsKey(windowID) && cl.getOpenWindows().get(windowID) != null)) {
                    final ItemsWindow iWin = windowID == 0 ? cl.getInventory() : cl.getOpenWindows().get(windowID);
                    iWin.putItem(slot, item);
                }
            } else if (packet instanceof ServerCloseWindowPacket) {
                if (!up.isEnableInventoryHandling() || protocol >= 755)
                    return;

                final int windowID = (int) packet.accessPacketMethod("getWindowID");
                if (cl.getOpenWindows().containsKey(windowID) && cl.getOpenWindows().get(windowID) != null) {
                    cl.getOpenWindows().get(windowID).closeWindow();
                } else if (windowID == 0) {
                    cl.getInventory().closeWindow();
                }
            } else if (packet instanceof ServerWindowItemsPacket) {
                if (!up.isEnableInventoryHandling() || protocol >= 755)
                    return;

                final int windowID = (int) packet.accessPacketMethod("getWindowID");
                final List<ItemStack> items = (List<ItemStack>) packet.accessPacketMethod("getItems");
                if (windowID == 0
                        || (cl.getOpenWindows().containsKey(windowID) && cl.getOpenWindows().get(windowID) != null)) {
                    final ItemsWindow iWin = windowID == 0 ? cl.getInventory() : cl.getOpenWindows().get(windowID);
                    for (int x = 0; x < items.size(); x++) {
                        iWin.putItem(x, items.get(x));
                    }
                }
            } else if (packet instanceof ServerOpenWindowPacket
                    || packet instanceof net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerOpenWindowPacket) {
                if (!up.isEnableInventoryHandling() || protocol >= 755)
                    return;

                final int windowID = (int) packet.accessPacketMethod("getWindowID");
                final String windowTitle = ChatMessages
                        .removeColors(ChatMessages.parse((String) packet.accessPacketMethod("getWindowTitle")));
                int slots = 0;
                if (packet instanceof ServerOpenWindowPacket) {
                    slots = ((int) packet.accessPacketMethod("getSlots"));
                } else {
                    final int windowType = (int) packet.accessPacketMethod("getWindowType");
                    if (windowID <= 5) {
                        slots = (windowType + 1) * 9;
                    } else
                        return;
                }

                final ItemsWindow win = new ItemsWindow(windowTitle, slots, windowID, cl, registry);
                cl.setOpenWindow(windowID, win);
                for (final ClientListener l : cl.getClientListeners()) {
                    l.windowOpened(windowID, win, registry);
                }

            } else if (packet instanceof ServerStatisticsPacket) {
                final Map<String, Integer> values = (Map<String, Integer>) packet.accessPacketMethod("getValues");
                for (final ClientListener l : cl.getClientListeners()) {
                    l.statisticsReceived(values);
                }
            } else if (packet instanceof ServerPlayerListItemPacket) {
                final HashMap<UUID, PlayerInfo> playersTabList = cl.getPlayersTabList();
                final Action action = (Action) packet.accessPacketMethod("getAction");
                final List<PlayerListItem> playerList = (List<PlayerListItem>) packet
                        .accessPacketMethod("getPlayersList");
                for (final PlayerListItem player : playerList) {
                    final UUID pid = player.getUuid();
                    switch (action) {
                        case ADD_PLAYER: {
                            playersTabList.put(pid, new PlayerInfo(player.getPlayerName(), player.getTextures(),
                                    player.getDisplayName(), player.getPing(), pid));
                            break;
                        }
                        case UPDATE_DISPLAY_NAME: {
                            if (!playersTabList.containsKey(pid)) {
                                break;
                            }
                            final PlayerInfo old = playersTabList.get(pid);
                            playersTabList.put(pid, new PlayerInfo(old.getName(), old.getTexture(),
                                    player.getDisplayName(), old.getPing(), pid));
                            break;
                        }
                        case REMOVE_PLAYER: {
                            playersTabList.remove(pid);
                            break;
                        }
                        case UPDATE_LATENCY: {
                            if (!playersTabList.containsKey(pid)) {
                                break;
                            }
                            final PlayerInfo old = playersTabList.get(pid);
                            playersTabList.put(pid, new PlayerInfo(old.getName(), old.getTexture(),
                                    old.getDisplayName(), player.getPing(), pid));
                            break;
                        }
                        case UPDATE_GAMEMODE: {
                            break;
                        }
                    }
                }

            } else if (packet instanceof ServerJoinGamePacket) {
                final int entityID = (int) packet.accessPacketMethod("getEntityID");
                cl.setEntityID(entityID);
                cl.getPlayersTabList().clear();

                if (!up.isSendMCBrand())
                    return;

                final String cname = protocol > 340 ? "minecraft:brand" : "MC|Brand";

                for (int x = 0; x < cl.getInventory().getSize(); x++) {
                    cl.getInventory().putItem(x, new ItemStack((short) 0, 1, (short) 0, null));
                }

                cl.sendPacket(PacketFactory.constructPacket(registry, "ClientPluginMessagePacket", cname,
                        up.getBrand().getBytes()));

                for (int x = 0; x <= 1; x++) {
                    cl.sendPacket(PacketFactory.constructPacket(registry, "ClientStatusPacket", x));
                }
            } else if (packet instanceof ServerUpdateHealthPacket) {
                if (((float) packet.accessPacketMethod("getHealth") <= 0)) {
                    cl.sendPacket(PacketFactory.constructPacket(registry, "ClientStatusPacket", 0));
                }
                final float hp = (float) packet.accessPacketMethod("getHealth");
                final int food = (int) packet.accessPacketMethod("getFood");
                for (final ClientListener ls : cl.getClientListeners()) {
                    ls.healthUpdate(hp, food);
                }
            } else if (packet instanceof ServerLoginSuccessPacket
                    || packet instanceof net.defekt.mc.chatclient.protocol.packets.alt.clientbound.login.ServerLoginSuccessPacket) {
                cl.setCurrentState(State.IN);
            } else if (packet instanceof ServerKeepAlivePacket
                    || packet instanceof net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerKeepAlivePacket) {
                lastKeepAlivePacket = System.currentTimeMillis();
                if (up.isIgnoreKeepAlive())
                    return;
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(up.getAdditionalPing());
                            if (packet instanceof ServerKeepAlivePacket) {
                                final long id = (long) packet.accessPacketMethod("getId");
                                cl.sendPacket(PacketFactory.constructPacket(registry, "ClientKeepAlivePacket", id));
                            } else {
                                final int id = (int) packet.accessPacketMethod("getId");
                                cl.sendPacket(PacketFactory.constructPacket(registry, "ClientKeepAlivePacket", id));
                            }
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (packet instanceof ServerChatMessagePacket) {
                final String json = (String) packet.accessPacketMethod("getMessage");

                for (final ClientListener ls : cl.getClientListeners()) {
                    ls.messageReceived(ChatMessages.parse(json), (Position) packet.accessPacketMethod("getPosition"));
                }
            } else if (packet instanceof ServerPlayerPositionAndLookPacket
                    || packet instanceof net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerPlayerPositionAndLookPacket) {
                final double x = (double) packet.accessPacketMethod("getX");
                final double y = (double) packet.accessPacketMethod("getY");
                final double z = (double) packet.accessPacketMethod("getZ");
                final float yaw = (float) packet.accessPacketMethod("getYaw");
                final float pitch = (float) packet.accessPacketMethod("getPitch");

                cl.setX(x);
                cl.setY(y);
                cl.setZ(z);
                cl.setYaw(yaw);
                cl.setPitch(pitch);

                if (packet instanceof ServerPlayerPositionAndLookPacket) {
                    final int teleportID = (int) packet.accessPacketMethod("getTeleportID");
                    cl.sendPacket(new ClientTeleportConfirmPacket(registry, teleportID));
                }

//				cl.sendPacket(PacketFactory
//						.constructPacket(registry, "ClientPlayerPositionAndLookPacket", x, y, z, yaw, pitch, true)
//						);

                synchronized (cl.getLock()) {
                    cl.getLock().notify();
                }
            } else if (packet instanceof ServerDisconnectPacket) {
                final String json = (String) packet.accessPacketMethod("getReason");
                final boolean dsIgnore = up.isIgnoreDisconnect();

                for (final ClientListener ls : cl.getClientListeners())
                    if (dsIgnore) {
                        ls.messageReceived("\u00A7cPacket " + Integer.toHexString(packet.getID()) + ": "
                                + ChatMessages.parse(json), Position.CHAT);
                    } else {
                        ls.disconnected(ChatMessages.parse(json));
                    }
                if (!dsIgnore) {
                    cl.close();
                }
            } else if (packet instanceof ServerPluginMessagePacket) {
                final String channel = (String) packet.accessPacketMethod("getChannel");
                final byte[] data = (byte[]) packet.accessPacketMethod("getDataF");

                final String commonChannelName = channel.replace("minecraft:", "").replace("MC|", "").toLowerCase();

                if (channel.equalsIgnoreCase("fml|hs")) {
                    byte discriminator = data[0];
                    switch (discriminator) {
                        case 0: {
                            byte protocol = data[1];
                            cl.sendPacket(new ClientPluginMessagePacket(registry, channel, new byte[] { 1, protocol }));

                            ModInfo[] mods;
                            try {
                                StatusInfo info = MinecraftStat.serverListPing(cl.getHost(), cl.getPort(), 10000);
                                mods = info.getModList().toArray(new ModInfo[0]);
                            } catch (Exception ex) {
                                mods = new ModInfo[0];
                            }
                            ByteArrayOutputStream modListBuffer = new ByteArrayOutputStream();
                            VarOutputStream modListStream = new VarOutputStream(modListBuffer);
                            modListStream.writeByte(2);
                            modListStream.writeByte(mods.length);
                            for (ModInfo mod : mods) {
                                modListStream.writeString(mod.getModID());
                                modListStream.writeString(mod.getVersion());
                            }

                            cl.sendPacket(
                                    new ClientPluginMessagePacket(registry, channel, modListBuffer.toByteArray()));
                            break;
                        }
                        case 2: {
                            cl.sendPacket(new ClientPluginMessagePacket(registry, channel, new byte[] { -1, 2 }));
                            cl.sendPacket(new ClientPluginMessagePacket(registry, channel, new byte[] { -1, 3 }));
                            break;
                        }
                        case -1: {
                            byte phase = data[1];
                            byte response = 0;
                            switch (phase) {
                                case 2: {
                                    response = 4;
                                    break;
                                }
                                case 3: {
                                    response = 5;
                                    break;
                                }
                            }
                            cl.sendPacket(
                                    new ClientPluginMessagePacket(registry, channel, new byte[] { -1, response }));
                        }
                    }
                }

                if (commonChannelName.equals("register")) {
                    cl.sendPacket(PacketFactory.constructPacket(registry, "ClientPluginMessagePacket", channel, data));
                }
            } else if (packet instanceof ServerResourcePackSendPacket
                    || packet instanceof net.defekt.mc.chatclient.protocol.packets.alt.clientbound.play.ServerResourcePackSendPacket) {
                if (up.isShowResourcePackMessages()) {
                    for (final ClientListener ls : cl.getClientListeners()) {
                        ls.messageReceived(
                                up.getResourcePackMessage().replace("%res",
                                        (String) packet.accessPacketMethod("getUrl")),
                                up.getResourcePackMessagePosition());
                    }
                }

                final boolean altResourcePack = protocol <= 110;

                final List<Object> rsPackArgs = new ArrayList<>();
                if (altResourcePack) {
                    rsPackArgs.add(packet.accessPacketMethod("getHash"));
                }

                rsPackArgs.add(up.getResourcePackBehavior());

                Object[] rsPackArgsArray = new Object[rsPackArgs.size()];
                rsPackArgsArray = rsPackArgs.toArray(rsPackArgsArray);

                cl.sendPacket(
                        PacketFactory.constructPacket(registry, "ClientResourcePackStatusPacket", rsPackArgsArray));
            }

        } catch (

        final IOException | InvalidKeySpecException | NoSuchAlgorithmException | InvalidKeyException
                | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
                | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

}
