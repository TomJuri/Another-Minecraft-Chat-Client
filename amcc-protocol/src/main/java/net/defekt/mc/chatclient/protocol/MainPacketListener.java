package net.defekt.mc.chatclient.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.defekt.mc.chatclient.protocol.MojangAPI.RequestResponse;
import net.defekt.mc.chatclient.protocol.data.ChatMessages;
import net.defekt.mc.chatclient.protocol.data.DummyItemsWindow;
import net.defekt.mc.chatclient.protocol.data.Hosts;
import net.defekt.mc.chatclient.protocol.data.ItemStack;
import net.defekt.mc.chatclient.protocol.data.ItemsWindow;
import net.defekt.mc.chatclient.protocol.data.Messages;
import net.defekt.mc.chatclient.protocol.data.ModInfo;
import net.defekt.mc.chatclient.protocol.data.PlayerInfo;
import net.defekt.mc.chatclient.protocol.data.StatusInfo;
import net.defekt.mc.chatclient.protocol.entity.Entity;
import net.defekt.mc.chatclient.protocol.entity.Player;
import net.defekt.mc.chatclient.protocol.io.IOUtils;
import net.defekt.mc.chatclient.protocol.io.VarOutputStream;
import net.defekt.mc.chatclient.protocol.packets.PacketFactory;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry.State;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerChatMessagePacket;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerEntityRelativeMovePacket;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerEntityTeleportPacket;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerKeepAlivePacket;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerLoginSuccessPacket;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerOpenWindowPacket;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerPlayerListItemPacket;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerPlayerListItemPacket.Action;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerPlayerListItemPacket.PlayerListItem;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerPlayerPositionAndLookPacket;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerResourcePackSendPacket;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseServerSpawnEntityPacket;
import net.defekt.mc.chatclient.protocol.packets.abstr.BaseSpawnPlayerPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginEncryptionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginResponsePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.login.ServerLoginSetCompressionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket.Position;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerCloseWindowPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerConfirmTransactionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerDestroyEntitiesPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerDisconnectPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerJoinGamePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerOpenWindowPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPlayerPositionAndLookPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerPluginMessagePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerSetSlotPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerStatisticsPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerTimeUpdatePacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerUpdateHealthPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerWindowItemsPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.login.ClientLoginEncryptionPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientKeepAlivePacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientPluginMessagePacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientTeleportConfirmPacket;

public class MainPacketListener extends AnnotatedPacketListener {

    protected MainPacketListener(MinecraftClient client) {
        super(client);
    }

    @PacketHandler
    protected void spawnEntity(BaseServerSpawnEntityPacket sp) {
        cl.getStoredEntities().put(sp.getId(), new Entity(sp.getUid(), sp.getType(), sp.getX(), sp.getY(), sp.getZ()));
    }

    @PacketHandler
    protected void encryptionRequest(ServerLoginEncryptionPacket sPacket) throws Exception {
        switch (cl.getAuthType()) {
            case Offline: {
                for (final ClientListener ls : cl.getClientListeners()) {
                    ls.disconnected(Messages.getString("MinecraftClient.clientErrorDisconnectedNoAuth"));
                }
                cl.close();
                break;
            }
            case TheAltening:
            case Mojang: {
                final PublicKey publicKey = sPacket.getPublicKey();
                final byte[] verifyToken = sPacket.getVerifyToken();
                final String serverID = sPacket.getServerID();

                final byte[] clientSecret = new byte[16];
                new SecureRandom().nextBytes(clientSecret);

                final Cipher rsa = Cipher.getInstance("RSA");
                rsa.init(Cipher.ENCRYPT_MODE, publicKey);

                final byte[] encryptedSecret = rsa.doFinal(clientSecret);
                final byte[] encryptedToken = rsa.doFinal(verifyToken);

                final String sha = IOUtils.sha1(serverID.getBytes(), clientSecret, publicKey.getEncoded());

                final RequestResponse resp = MojangAPI.makeJSONRequest(
                        (cl.getAuthType() == AuthType.Mojang ? Hosts.MOJANG_SESSIONSERVER
                                : Hosts.ALTENING_SESSIONSERVER) + "/session/minecraft/join",
                        new HashMap<String, JsonElement>() {
                            {
                                put("accessToken", new JsonPrimitive(cl.getAuthToken()));
                                put("selectedProfile", new JsonPrimitive(cl.getAuthID()));
                                put("serverId", new JsonPrimitive(sha));
                            }
                        });
                try {
                    final JsonObject json = resp.getJson();
                    if (json.has("error")) {
                        final String errMsg = json.has("errorMessage") ? json.get("errorMessage").getAsString()
                                : json.get("error").getAsString();
                        for (final ClientListener ls : cl.getClientListeners()) {
                            ls.disconnected(Messages.getString("MinecraftClient.clientErrorDisconnected") + errMsg);
                        }
                        cl.close();
                        break;
                    }
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }

                cl.sendPacket(new ClientLoginEncryptionPacket(registry, encryptedSecret, encryptedToken));
                cl.enableEncryption(clientSecret);
                break;
            }
            default: {
                for (final ClientListener ls : cl.getClientListeners()) {
                    ls.disconnected(Messages.getString("MinecraftClient.clientErrorDisconnectedNoAuth"));
                }
                cl.close();
                break;
            }
        }
    }

    @PacketHandler
    protected void timeUpdate(ServerTimeUpdatePacket packet) {
        for (final ClientListener cls : cl.getClientListeners()) {
            cls.timeUpdated(packet.getTime(), packet.getWorldAge());
        }
    }

    @PacketHandler
    protected void confirmTransaction(ServerConfirmTransactionPacket packet) throws IOException {
        if (!up.isEnableInventoryHandling() || protocol >= 755) return;

        final int windowID = packet.getWindowID();
        final short actionID = packet.getActionID();
        final boolean accepted = packet.isAccepted();

        final ItemsWindow win = windowID == 0 ? cl.getInventory()
                : cl.getOpenWindows().containsKey(windowID) ? cl.getOpenWindows().get(windowID) : null;

        if (win != null) if (accepted) {
            win.finishTransaction(actionID);
        } else {
            win.cancelTransaction(actionID);
        }

        if (!accepted) {
            cl.sendPacket(PacketFactory.constructPacket(registry, "ClientConfirmTransactionPacket", (byte) windowID,
                    actionID, accepted));
        }
    }

    @PacketHandler
    protected void windowSlot(ServerSetSlotPacket packet) {
        if (!up.isEnableInventoryHandling() || protocol >= 755) return;

        final int windowID = packet.getWindowID();

        final short slot = packet.getSlot();
        final ItemStack item = packet.getItem();
        if (windowID == 0 || (cl.getOpenWindows().containsKey(windowID) && cl.getOpenWindows().get(windowID) != null)) {
            final ItemsWindow iWin = windowID == 0 ? cl.getInventory() : cl.getOpenWindows().get(windowID);
            iWin.putItem(slot, item);
        }
    }

    @PacketHandler
    protected void windowClose(ServerCloseWindowPacket packet) {
        if (!up.isEnableInventoryHandling() || protocol >= 755) return;

        final int windowID = packet.getWindowID();
        if (cl.getOpenWindows().containsKey(windowID) && cl.getOpenWindows().get(windowID) != null) {
            cl.getOpenWindows().get(windowID).closeWindow();
        } else if (windowID == 0) {
            cl.getInventory().closeWindow();
        }
    }

    @PacketHandler
    protected void windowItems(ServerWindowItemsPacket packet) {
        if (!up.isEnableInventoryHandling() || protocol >= 755) return;

        final int windowID = packet.getWindowID();
        final List<ItemStack> items = packet.getItems();
        if (windowID == 0 || (cl.getOpenWindows().containsKey(windowID) && cl.getOpenWindows().get(windowID) != null)) {
            final ItemsWindow iWin = windowID == 0 ? cl.getInventory() : cl.getOpenWindows().get(windowID);
            for (int x = 0; x < items.size(); x++) {
                iWin.putItem(x, items.get(x));
            }
        }
    }

    @PacketHandler
    protected void windowOpened(BaseServerOpenWindowPacket packet) { // TODO
        if (!up.isEnableInventoryHandling() || protocol >= 755) return;

        final int windowID = packet.getWindowID();
        final String windowTitle = ChatMessages.removeColors(ChatMessages.parse(packet.getWindowTitle()));
        int slots = 0;
        if (packet instanceof ServerOpenWindowPacket) {
            slots = packet.getSlots();
        } else {
            final int windowType = packet.getWindowInt();
            if (windowID <= 5) {
                slots = (windowType + 1) * 9;
            } else
                return;
        }

        final ItemsWindow win = new DummyItemsWindow(windowTitle, slots, windowID);
        cl.setOpenWindow(windowID, win);
        for (final ClientListener l : cl.getClientListeners()) {
            l.windowOpened(windowID, win, registry);
        }
    }

    @PacketHandler
    protected void statsReceived(ServerStatisticsPacket packet) {
        final Map<String, Integer> values = packet.getValues();
        for (final ClientListener l : cl.getClientListeners()) {
            l.statisticsReceived(values);
        }
    }

    @PacketHandler
    protected void playeListUpdated(BaseServerPlayerListItemPacket packet) {
        final HashMap<UUID, PlayerInfo> playersTabList = cl.getPlayersTabList();
        final Action action = packet.getAction();
        final List<PlayerListItem> playerList = packet.getPlayersList();
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
                    playersTabList.put(pid, new PlayerInfo(old.getName(), old.getTexture(), player.getDisplayName(),
                            old.getPing(), pid));
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
                    playersTabList.put(pid, new PlayerInfo(old.getName(), old.getTexture(), old.getDisplayName(),
                            player.getPing(), pid));
                    break;
                }
                case UPDATE_GAMEMODE: {
                    break;
                }
            }
        }
    }

    @PacketHandler
    protected void onJoinGame(ServerJoinGamePacket packet) throws IOException {
        final int entityID = packet.getEntityID();
        cl.setEntityID(entityID);
        cl.getPlayersTabList().clear();

        if (!up.isSendMCBrand()) return;

        final String cname = protocol > 340 ? "minecraft:brand" : "MC|Brand";

        for (int x = 0; x < cl.getInventory().getSize(); x++) {
            cl.getInventory().putItem(x, new ItemStack((short) 0, 1, (short) 0, null));
        }

        cl.sendPacket(
                PacketFactory.constructPacket(registry, "ClientPluginMessagePacket", cname, up.getBrand().getBytes()));

        for (int x = 0; x <= 1; x++) {
            cl.sendPacket(PacketFactory.constructPacket(registry, "ClientStatusPacket", x));
        }
    }

    @PacketHandler
    protected void updateHealth(ServerUpdateHealthPacket packet) throws IOException {
        if (packet.getHealth() <= 0) {
            cl.sendPacket(PacketFactory.constructPacket(registry, "ClientStatusPacket", 0));
        }
        final float hp = packet.getHealth();
        final int food = packet.getFood();
        for (final ClientListener ls : cl.getClientListeners()) {
            ls.healthUpdate(hp, food);
        }
    }

    @PacketHandler
    protected void onKeepAliveReceived(BaseServerKeepAlivePacket ka) {
        lastKeepAlivePacket = System.currentTimeMillis();
        if (up.isIgnoreKeepAlive()) return;
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(up.getAdditionalPing());
                    long id = ka.getPid();
                    if (ka.isLegacy())
                        cl.sendPacket(
                                new net.defekt.mc.chatclient.protocol.packets.alt.serverbound.play.ClientKeepAlivePacket(
                                        registry, (int) id));
                    else
                        cl.sendPacket(new ClientKeepAlivePacket(registry, id));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @PacketHandler
    protected void onChatReceived(BaseServerChatMessagePacket msg) {

        final String json = msg.getMessage();

        for (final ClientListener ls : cl.getClientListeners()) {
            ls.messageReceived(ChatMessages.parse(json, cl), msg.getPosition());
        }
    }

    @PacketHandler
    protected void playerPositionAndLook(BaseServerPlayerPositionAndLookPacket p) throws IOException {
        final double x = p.getX();
        final double y = p.getY();
        final double z = p.getZ();
        final float yaw = p.getYaw();
        final float pitch = p.getPitch();

        cl.setX(x);
        cl.setY(y);
        cl.setZ(z);
        cl.setYaw(yaw);
        cl.setPitch(pitch);

        if (p instanceof ServerPlayerPositionAndLookPacket) {
            final int teleportID = p.getTeleportID();
            cl.sendPacket(new ClientTeleportConfirmPacket(registry, teleportID));
        }

        synchronized (cl.getLock()) {
            cl.getLock().notify();
        }
    }

    @PacketHandler
    protected void playDisconnect(ServerDisconnectPacket packet) {
        final String json = packet.getReason();
        final boolean dsIgnore = up.isIgnoreDisconnect();

        for (final ClientListener ls : cl.getClientListeners())
            if (dsIgnore) {
                ls.messageReceived(
                        "\u00a7cPacket " + Integer.toHexString(packet.getID()) + ": " + ChatMessages.parse(json),
                        Position.CHAT);
            } else {
                ls.disconnected(ChatMessages.parse(json));
            }
        if (!dsIgnore) {
            cl.close();
        }
    }

    @PacketHandler
    protected void pluginMessageReceived(ServerPluginMessagePacket packet) throws IOException {
        final String channel = packet.getChannel();
        final byte[] data = packet.getDataF();

        final String commonChannelName = channel.replace("minecraft:", "").replace("MC|", "").toLowerCase();

        if (channel.equalsIgnoreCase("fml|hs")) {
            final byte discriminator = data[0];
            switch (discriminator) {
                case 0: {
                    final byte protocol = data[1];
                    cl.sendPacket(new ClientPluginMessagePacket(registry, channel, new byte[] { 1, protocol }));

                    ModInfo[] mods;
                    try {
                        final StatusInfo info = MinecraftStat.serverListPing(cl.getHost(), cl.getPort(), 10000);
                        mods = info.getModList().toArray(new ModInfo[0]);
                    } catch (final Exception ex) {
                        mods = new ModInfo[0];
                    }
                    final ByteArrayOutputStream modListBuffer = new ByteArrayOutputStream();
                    final VarOutputStream modListStream = new VarOutputStream(modListBuffer);
                    modListStream.writeByte(2);
                    modListStream.writeByte(mods.length);
                    for (final ModInfo mod : mods) {
                        modListStream.writeString(mod.getModID());
                        modListStream.writeString(mod.getVersion());
                    }

                    cl.sendPacket(new ClientPluginMessagePacket(registry, channel, modListBuffer.toByteArray()));
                    break;
                }
                case 2: {
                    cl.sendPacket(new ClientPluginMessagePacket(registry, channel, new byte[] { -1, 2 }));
                    cl.sendPacket(new ClientPluginMessagePacket(registry, channel, new byte[] { -1, 3 }));
                    break;
                }
                case -1: {
                    final byte phase = data[1];
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
                    cl.sendPacket(new ClientPluginMessagePacket(registry, channel, new byte[] { -1, response }));
                }
            }
        }

        if (commonChannelName.equals("register")) {
            cl.sendPacket(PacketFactory.constructPacket(registry, "ClientPluginMessagePacket", channel, data));
        }
    }

    @PacketHandler
    protected void resPackReceived(BaseServerResourcePackSendPacket packet) throws IOException {
        if (up.isShowResourcePackMessages()) {
            for (final ClientListener ls : cl.getClientListeners()) {
                ls.messageReceived(up.getResourcePackMessage().replace("%res", packet.getUrl()),
                        up.getResourcePackMessagePosition());
            }
        }

        final boolean altResourcePack = protocol <= 110;

        final List<Object> rsPackArgs = new ArrayList<>();
        if (altResourcePack) {
            rsPackArgs.add(packet.getHash());
        }

        rsPackArgs.add(up.getResourcePackBehavior());

        Object[] rsPackArgsArray = new Object[rsPackArgs.size()];
        rsPackArgsArray = rsPackArgs.toArray(rsPackArgsArray);

        cl.sendPacket(PacketFactory.constructPacket(registry, "ClientResourcePackStatusPacket", rsPackArgsArray));
    }

    @PacketHandler
    protected void spawnPlaye(BaseSpawnPlayerPacket sp) {
        if (sp.getId() == cl.getEntityID()) return;
        cl.getStoredEntities().put(sp.getId(), new Player(sp.getUid(), sp.getX(), sp.getY(), sp.getZ()));
    }

    @PacketHandler
    protected void removeEntities(ServerDestroyEntitiesPacket packet) {
        final Map<Integer, Entity> ets = cl.getStoredEntities();
        for (final int id : packet.getEntityIDs()) {
            ets.remove(id);
        }
    }

    @PacketHandler
    protected void entityRelativeMove(BaseServerEntityRelativeMovePacket move) {
        final Entity entity = cl.getEntity(move.getEntityID());
        if (entity == null) return;
        final double x = entity.getX();
        final double y = entity.getY();
        final double z = entity.getZ();

        final double dX = move.getDeltaX();
        final double dY = move.getDeltaY();
        final double dZ = move.getDeltaZ();

        entity.setX(x + dX);
        entity.setY(y + dY);
        entity.setZ(z + dZ);
        for (final ClientListener listener : cl.getClientListeners()) {
            listener.entityMoved(entity, move.getEntityID());
        }
    }

    @PacketHandler
    protected void entityTeleport(BaseServerEntityTeleportPacket tp) {
        final double x = tp.getX();
        final double y = tp.getY();
        final double z = tp.getZ();
        final int id = tp.getId();
        final Entity et = cl.getEntity(id);
        if (et != null) {
            et.setX(x);
            et.setZ(y);
            et.setZ(z);
            for (final ClientListener listener : cl.getClientListeners()) {
                listener.entityMoved(et, id);
            }
        }
    }

    @PacketHandler
    protected void setCompression(ServerLoginSetCompressionPacket cp) {
        cl.setCompression(true);
    }

    @PacketHandler
    protected void loginDisconnect(ServerLoginResponsePacket e) {
        for (final ClientListener ls : cl.getClientListeners()) {
            ls.disconnected(ChatMessages.parse(e.getResponse()));
        }
        cl.close();
    }

    @PacketHandler
    protected void loginSuccess(BaseServerLoginSuccessPacket e) {
        cl.setCurrentState(State.IN);
        try {
            String uid = e.getUuid();
            cl.setUid(UUID.fromString(uid));
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

}
