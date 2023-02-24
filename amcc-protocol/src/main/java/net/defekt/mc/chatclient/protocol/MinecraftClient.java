package net.defekt.mc.chatclient.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.Inflater;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.defekt.mc.chatclient.protocol.data.ChatMessages;
import net.defekt.mc.chatclient.protocol.data.Hosts;
import net.defekt.mc.chatclient.protocol.data.ItemWindowsFactory;
import net.defekt.mc.chatclient.protocol.data.ItemsWindow;
import net.defekt.mc.chatclient.protocol.data.Messages;
import net.defekt.mc.chatclient.protocol.data.PlayerInfo;
import net.defekt.mc.chatclient.protocol.entity.Entity;
import net.defekt.mc.chatclient.protocol.entity.Player;
import net.defekt.mc.chatclient.protocol.event.ClientListener;
import net.defekt.mc.chatclient.protocol.event.MinecraftPacketListener;
import net.defekt.mc.chatclient.protocol.io.ListenerHashMap;
import net.defekt.mc.chatclient.protocol.io.VarInputStream;
import net.defekt.mc.chatclient.protocol.io.VarOutputStream;
import net.defekt.mc.chatclient.protocol.packets.HandshakePacket;
import net.defekt.mc.chatclient.protocol.packets.Packet;
import net.defekt.mc.chatclient.protocol.packets.PacketFactory;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry;
import net.defekt.mc.chatclient.protocol.packets.PacketRegistry.State;
import net.defekt.mc.chatclient.protocol.packets.UnknownPacket;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket.Position;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerStatisticsPacket;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientEntityActionPacket.EntityAction;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientUseEntityPacket.UseType;

/**
 * MinecraftClient is Minecraft protocol implementation at IO level. It is
 * responsible for connecting to a Minecraft server and handling all data
 * received from it.
 * 
 * @see ClientListener
 * @see MinecraftStat
 * @see ProtocolNumber
 * @author Defective4
 */
public class MinecraftClient {

    private final String host;
    private final int port;
    private final int protocol;
    private final PacketRegistry reg;

    private String username = "";

    private double x = Integer.MIN_VALUE;
    private double y = 0;
    private double z = 0;
    private float yaw = 0;
    private float pitch = 0;

    private int entityID = 0;
    private UUID uid = null;

    private final Map<Integer, Entity> storedEntities = new ConcurrentHashMap<>();
    private int trackedEntity = -1;
    private boolean isTrackedAttacking = false;

    private Socket soc = null;
    private OutputStream os = null;
    private VarInputStream is = null;

    private boolean compression = false;
    private final int cThreshold = -1;

    private boolean sneaking = false;
    private boolean sprinting = false;

    private final Object lock = new Object();

    private long startDate = System.currentTimeMillis();

    private static final String notConnectedError = Messages.getString("MinecraftClient.clientErrorNotConnected");

    private final ListenerHashMap<UUID, PlayerInfo> playersTabList = new ListenerHashMap<UUID, PlayerInfo>();

    private final List<InternalPacketListener> packetListeners = new ArrayList<InternalPacketListener>();
    private final List<InternalPacketListener> outPacketListeners = new ArrayList<InternalPacketListener>();
    private final List<ClientListener> clientListeners = Collections.synchronizedList(new ArrayList<ClientListener>());
    private final boolean forge;

    private final Map<Integer, ItemsWindow> openWindows = new HashMap<Integer, ItemsWindow>();
    private ItemsWindow inventory = null;

    private Thread packetReaderThread = null;
    private Thread playerPositionThread = null;

    private final Timer internalTickTimer = new Timer("tickTimer", true);

    private final ItemWindowsFactory windowsFactory = new ItemWindowsFactory();

    /**
     * Add a outbound packet listener to listen for sent packets
     * 
     * @param listener a packet listener implementation
     */
    public void addOutputPacketListener(final InternalPacketListener listener) {
        outPacketListeners.add(listener);
    }

    /**
     * Add a inbound packet listener to listen for sent packets
     * 
     * @param listener a packet listener implementation
     */
    public void addInputPacketListener(final InternalPacketListener listener) {
        packetListeners.add(listener);
    }

    /**
     * Add a client listener to receive client events
     * 
     * @param listener a client listener for receiving client updates
     */
    public void addClientListener(final ClientListener listener) {
        clientListeners.add(listener);
    }

    /**
     * Remove a client listener
     * 
     * @param listener client listener to remove
     */
    public void removeClientListener(final ClientListener listener) {
        clientListeners.remove(listener);
    }

    /**
     * Get a copy of client listener list
     * 
     * @param includeGlobal include listeners defined in {@link GlobalListeners}
     * 
     * @return list of client listeners added to this client
     */
    public List<ClientListener> getClientListeners(final boolean includeGlobal) {
        final List<ClientListener> ls = new ArrayList<ClientListener>();
        if (includeGlobal) Collections.addAll(ls, GlobalListeners.getClientListeners());
        ls.addAll(clientListeners);
        return ls;

    }

    /**
     * Creates a new Minecraft Client ready to connect to specified server
     * 
     * @param host     address of server to connect to
     * @param port     port of target server
     * @param protocol protocol that will be used to connect to server
     * @param forge    whether this client should connect with Forge support
     * @throws IOException thrown when there was an error initializing
     *                     {@link PacketRegistry} for specified protocol (for
     *                     example when could not find a matching packet registry
     *                     implementation for specified protocol)
     */
    public MinecraftClient(final String host, final int port, final int protocol, final boolean forge)
            throws IOException {
        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.forge = forge;
        this.reg = PacketFactory.constructPacketRegistry(protocol);
        state = State.LOGIN;
    }

    private Proxy proxy = null;

    /**
     * Closes this MinecraftClient
     */
    public void close() {
        if (soc != null && !soc.isClosed()) {
            try {
                connected = false;
                try {
                    for (final ItemsWindow win : openWindows.values()) {
                        win.closeWindow();
                    }
                    inventory.closeWindow();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                soc.close();
                if (packetReaderThread != null) {
                    packetReaderThread.interrupt();
                }
                if (playerPositionThread != null) {
                    playerPositionThread.interrupt();
                }
            } catch (final IOException e) {
            }
        }
    }

    private boolean connected = false;

    private State state;

    /**
     * Set current client state. This method is used internally by
     * {@link MainPacketListener} bound to this client
     * 
     * @param state next client state
     */
    protected void setCurrentState(final State state) {
        this.state = state;
    }

    private Cipher eCipher = null;
    private Cipher dCipher = null;
    private boolean isEncrypted = false;

    /**
     * Used to enable encryption between client and server
     * 
     * @param secret secret key to encrypt with
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     */
    protected void enableEncryption(final byte[] secret) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException {
        final SecretKey key = new SecretKeySpec(secret, "AES");
        eCipher = Cipher.getInstance("AES/CFB8/NoPadding");
        eCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(secret));
        dCipher = Cipher.getInstance("AES/CFB8/NoPadding");
        dCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(secret));

        this.is = new VarInputStream(new CipherInputStream(is, dCipher));
        this.os = new CipherOutputStream(os, eCipher);

        isEncrypted = true;
    }

    private String authID = "";
    private String authToken = null;
    private AuthType authType = AuthType.Offline;

    /**
     * Connect to server specified in constructor
     * 
     * @param username username of connecting client
     * @throws IOException thrown when client was unable to connect to target server
     */
    public void connect(final String username) throws IOException {
        connect(AuthType.Offline, username, null);
    }

    /**
     * Connect this client to the server
     * 
     * @param auth     authentication type to use
     * @param token    an username or email
     * @param password authentication password or null it offline
     * @throws IOException
     */
    public void connect(final AuthType auth, final String token, final String password) throws IOException {
        this.authType = auth;
        switch (auth) {
            case TheAltening: {
                final MojangUser user = MojangAPI.authenticateUser(token,
                        auth == AuthType.TheAltening ? "none" : password,
                        auth == AuthType.TheAltening ? Hosts.ALTENING_AUTHSERVER : Hosts.MOJANG_AUTHSERVER);
                this.username = user.getUserName();
                this.authID = user.getUserID();
                this.authToken = user.getAccessToken();
                break;
            }
            case Offline: {
                this.username = token;
                break;
            }
            default: {
                this.username = token;
                break;
            }
        }

        try {
            if (connected || this.soc != null)
                throw new IOException(Messages.getString("MinecraftClient.clientErrorAlreadyConnected"));

            if (proxy != null) {
                this.soc = new Socket(proxy);
            } else {
                this.soc = new Socket();
            }
            soc.connect(new InetSocketAddress(host, port));
            this.connected = true;

            this.os = soc.getOutputStream();
            this.is = new VarInputStream(soc.getInputStream());
            inventory = windowsFactory.createWindow(Messages.getString("MinecraftClient.clientInventoryName"), 46, 0,
                    this, reg);
            packetListeners.add(new MainPacketListener(this));

            final Packet handshake = new HandshakePacket(reg, protocol,
                    host + (forge ? (protocol <= 340 ? "\0FML\0" : "") : ""), port, 2);
            sendPacket(handshake);

            final Packet login = PacketFactory.constructPacket(reg, "ClientLoginRequestPacket", username);
            sendPacket(login);

            packetReaderThread = new Thread(new Runnable() {

                private final Inflater inflater = new Inflater();

                @Override
                public void run() {
                    try {
                        while (connected) {
                            final int len = is.readVarInt();
                            final byte[] data = new byte[len];
                            is.readFully(data);

                            VarInputStream packetbuf = new VarInputStream(new ByteArrayInputStream(data));
                            final int id;
                            final byte[] packetData;
                            int compressed = 0;

                            if (compression) {
                                final int dlen = packetbuf.readVarInt();
                                if (dlen == 0) {
                                    id = packetbuf.readVarInt();
                                    packetData = new byte[len - VarOutputStream.checkVarIntSize(dlen) - 1];
                                    packetbuf.readFully(packetData);
                                    compressed = 2;
                                } else {
                                    final byte[] toProcess = new byte[len - VarOutputStream.checkVarIntSize(dlen)];
                                    packetbuf.readFully(toProcess);

                                    final byte[] inflated = new byte[dlen];
                                    inflater.setInput(toProcess);
                                    inflater.inflate(inflated);
                                    inflater.reset();

                                    packetbuf = new VarInputStream(new ByteArrayInputStream(inflated));
                                    id = packetbuf.readVarInt();

                                    packetData = new byte[dlen - 1];
                                    packetbuf.readFully(packetData);
                                    compressed = 3;

                                }
                            } else {
                                id = packetbuf.readVarInt();
                                packetData = new byte[len - 1];
                                packetbuf.readFully(packetData);
                                compressed = 1;
                            }

                            if (id != -1) {
                                final Class<? extends Packet> pClass = reg.getByID(id, state);
                                Packet packet;

                                if (pClass == null) {
                                    packet = new UnknownPacket(reg, id, packetData);
                                } else {
                                    packet = PacketFactory.constructPacket(reg, pClass.getSimpleName(), packetData);
                                }
                                packet.setCompressed(compressed);
                                packet.setEncrypted(isEncrypted);
                                for (final MinecraftPacketListener listener : GlobalListeners.getListeners()) {
                                    final Packet nPacket = listener.packetReceiving(packet, MinecraftClient.this);
                                    if (nPacket != null) packet = nPacket;
                                    if (packet.isCancelled()) break;
                                }
                                if (!packet.isCancelled()) {
                                    for (final InternalPacketListener lis : packetListeners) {
                                        lis.packetReceived(packet, reg);
                                    }
                                    for (final MinecraftPacketListener listener : GlobalListeners.getListeners()) {
                                        listener.packetReceived(packet, MinecraftClient.this);
                                    }
                                }

                            }
                        }
                    } catch (final Exception e) {
//                        e.printStackTrace();
                        for (final ClientListener cl : getClientListeners(true)) {
                            cl.disconnected(e.toString(), MinecraftClient.this);
                        }
                        close();
                    }
                }
            });
            packetReaderThread.start();

            synchronized (lock) {
                try {
                    lock.wait(5000);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            playerPositionThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        while (true) {
                            Thread.sleep(1000);
                            if (x == Integer.MIN_VALUE) {
                                continue;
                            }
                            try {
                                if (soc.isClosed()) {
                                    close();
                                    return;
                                }
                                final Packet playerPositionPacket = PacketFactory.constructPacket(reg,
                                        "ClientPlayerPositionPacket", x, y, z, true);
                                sendPacket(playerPositionPacket);
                            } catch (final Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (final InterruptedException e) {
                    }
                }
            });
            playerPositionThread.start();

            internalTickTimer.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                    if (!isConnected()) {
                        cancel();
                        return;
                    }
                    synchronized (clientListeners) {
                        for (final ClientListener listener : getClientListeners(true).toArray(new ClientListener[0])) {
                            try {
                                listener.tick(MinecraftClient.this);
                            } catch (final IOException ex) {
                                cancel();
                                return;
                            }
                        }
                    }
                }
            }, 0, 1000 / 20);

            startDate = System.currentTimeMillis();

        } catch (final IOException ex) {
            close();
            throw ex;
        }
    }

    /**
     * Get protocol of this client
     * 
     * @return protocol used by this client
     */
    public int getProtocol() {
        return protocol;
    }

    /**
     * Check if compression is enabled by server
     * 
     * @return compression state
     */
    protected boolean isCompressionEnabled() {
        return compression;
    }

    /**
     * Get compression threshold
     * 
     * @return compression threshold sent by server. -1 if none
     */
    protected int getCThreshold() {
        return cThreshold;
    }

    @SuppressWarnings("javadoc")
    protected Object getLock() {
        return lock;
    }

    /**
     * Get X position of this client in-game
     * 
     * @return X coordinates of client
     */
    public double getX() {
        return x;
    }

    /**
     * Get Y position of this client in-game
     * 
     * @return Y coordinates of client
     */
    public double getY() {
        return y;
    }

    /**
     * Get Z position of this client in-game
     * 
     * @return Z coordinates of client
     */
    public double getZ() {
        return z;
    }

    /**
     * Set X position of this client. This method only sets internal variable, it
     * does NOT change client's position on server.
     * 
     * @param x new X position
     */
    protected void setX(final double x) {
        this.x = x;
        for (final ClientListener cl : getClientListeners(true)) {
            cl.positionChanged(this.x, this.y, this.z, this);
        }
    }

    /**
     * Set Y position of this client. This method only sets internal variable, it
     * does NOT change client's position on server.
     * 
     * @param y new Y position
     */
    protected void setY(final double y) {
        this.y = y;
        for (final ClientListener cl : getClientListeners(true)) {
            cl.positionChanged(this.x, this.y, this.z, this);
        }
    }

    /**
     * Set Z position of this client. This method only sets internal variable, it
     * does NOT change client's position on server.
     * 
     * @param z new Z position
     */
    protected void setZ(final double z) {
        this.z = z;
        for (final ClientListener cl : getClientListeners(true)) {
            cl.positionChanged(this.x, this.y, this.z, this);
        }
    }

    /**
     * Toggle client sneaking state. It also sets client sneaking in-game/
     * 
     * @throws IOException thrown when server was not connected, or there was an
     *                     error sending packet to server
     */
    public void toggleSneaking() throws IOException {
        sneaking = !sneaking;
        final EntityAction action = sneaking ? EntityAction.START_SNEAKING : EntityAction.STOP_SNEAKING;
        try {
            sendPacket(PacketFactory.constructPacket(reg, "ClientEntityActionPacket", entityID, action));
        } catch (final Exception e) {
            sneaking = !sneaking;
            throw e;
        }
    }

    /**
     * Toggle client sprinting state. It also sets client sprinting in-game/
     * 
     * @throws IOException thrown when server was not connected, or there was an
     *                     error sending packet to server
     */
    public void toggleSprinting() throws IOException {
        sprinting = !sprinting;
        final EntityAction action = sprinting ? EntityAction.START_SPRINTING : EntityAction.STOP_SPRINTING;
        try {
            sendPacket(PacketFactory.constructPacket(reg, "ClientEntityActionPacket", entityID, action));
        } catch (final Exception e) {
            sprinting = !sprinting;
            throw e;
        }
    }

    /**
     * Sends a packet to server
     * 
     * @param packet packet to send
     * @throws IOException thrown when there was an error sending packet
     */
    public void sendPacket(Packet packet) throws IOException {
        if (connected && soc != null && !soc.isClosed()) {
            packet.setEncrypted(isEncrypted);
            for (final InternalPacketListener listener : outPacketListeners.toArray(new InternalPacketListener[0])) {
                listener.packetReceived(packet, reg);
                if (packet.isCancelled()) return;
            }
            for (final MinecraftPacketListener listener : GlobalListeners.getListeners()) {
                final Packet nPacket = listener.packetSending(packet, this);
                if (nPacket != null) packet = nPacket;
                if (packet.isCancelled()) return;
            }
            os.write(packet.getData(compression));
            for (final MinecraftPacketListener listener : GlobalListeners.getListeners()) {
                listener.packetSent(packet, this);
            }
        } else
            throw new IOException(notConnectedError);
    }

    /**
     * Send chat message to server
     * 
     * @param message a chat message to send
     * @throws IOException thrown when server was not connected, or there was an
     *                     error sending packet to server
     */
    public void sendChatMessage(final String message) throws IOException {
        final int protocol = PacketFactory.getProtocolFor(reg);
        if ((protocol >= 759) && message.startsWith("/")) {
            sendPacket(PacketFactory.constructPacket(reg, "ClientChatCommandPacket", message));
        } else
            sendPacket(PacketFactory.constructPacket(reg, "ClientChatMessagePacket", message));
    }

    /**
     * Simulates receiving of a chat message in client. This basically results in
     * displaying the message in the chat box. <br>
     * Messages support coloring with '§' character. <br>
     * Messages can be easily colored with
     * {@link ChatMessages#translateColorCodes(char, String)}
     * 
     * @param message  message to display
     * @param position position in which the message should be displayed
     */
    public void receiveMessage(final String message, final Position position) {
        for (final ClientListener ls : getClientListeners(false))
            ls.messageReceived(message, position, this);
    }

    /**
     * Used internally by {@link ClientPacketListener} to set client's entity ID
     * 
     * @param entityID new entity ID
     */
    protected void setEntityID(final int entityID) {
        this.entityID = entityID;
    }

    /**
     * Get client sneaking state of this client. It only returns variable stored
     * locally
     * 
     * @return sneaking state
     */
    public boolean isSneaking() {
        return sneaking;
    }

    /**
     * Get client sprinting state of this client. It only returns variable stored
     * locally
     * 
     * @return sprinting state
     */
    public boolean isSprinting() {
        return sprinting;
    }

    /**
     * Get player list
     * 
     * @return player list received from server
     */
    public ListenerHashMap<UUID, PlayerInfo> getPlayersTabList() {
        return playersTabList;
    }

    /**
     * Get server's address
     * 
     * @return server's hostname
     */
    public String getHost() {
        return host;
    }

    /**
     * Get server's port
     * 
     * @return server's port
     */
    public int getPort() {
        return port;
    }

    /**
     * Get client's username (only if client is connected)
     * 
     * @return client's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get output stream used by this client
     * 
     * @return client's output stream
     * @deprecated
     */
    @Deprecated
    protected OutputStream getOutputStream() {
        return os;
    }

    /**
     * Get client's yaw
     * 
     * @return client's yaw
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Get client's yaw
     * 
     * @return client's yaw
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Check if this entity is tracked
     * 
     * @param entity entity to be checked
     * @return if entity is tracked
     */
    public boolean isTracked(final Entity entity) {
        Entity tracked = null;
        if (storedEntities.containsKey(trackedEntity)) {
            tracked = storedEntities.get(trackedEntity);
        }
        if (tracked != null) return tracked.getUid().equals(entity.getUid());
        return false;
    }

    /**
     * Gets the tracked entity attacking status
     * 
     * @return current status
     */
    public boolean isEntityAttacking() {
        return isTrackedAttacking;
    }

    /**
     * Commands the client to track an entity
     * 
     * @param entity new tracked entity
     * @param attack whether to attack this entity when in range or not
     */
    public void trackEntity(final Entity entity, final boolean attack) {
        for (final int id : storedEntities.keySet().toArray(new Integer[0])) {
            if (storedEntities.get(id).getUid().equals(entity.getUid())) {
                trackedEntity = id;
                isTrackedAttacking = attack;
                for (final ClientListener listener : getClientListeners(true).toArray(new ClientListener[0])) {
                    listener.changedTrackedEntity(trackedEntity, this);
                }
            }
        }
    }

    /**
     * Interact with an entity
     * 
     * @param entity entity to interact with
     * @param type   interaction type
     * @throws IOException when there was an error sending packet
     */
    public void interact(final Entity entity, final UseType type) throws IOException {
        interact(getEntityID(entity), type);
    }

    /**
     * Interact with an entity
     * 
     * @param entityID ID of entity to interact with
     * @param type     interaction type
     * @throws IOException when there was an error sending packet
     */
    public void interact(final int entityID, final UseType type) throws IOException {
        Entity entity = getEntity(entityID);
        if ((entity = getEntity(entityID)) != null && distanceTo(entity) <= 4) {
            lookAt(entity);
            sendPacket(PacketFactory.constructPacket(reg, "ClientUseEntityPacket", entityID, type, isSneaking()));
            if (type == UseType.ATTACK) {
                sendPacket(PacketFactory.constructPacket(reg, "ClientAnimationPacket"));
            }
        }
    }

    /**
     * Get ID associated with this entity
     * 
     * @param entity target entity
     * @return entity's ID, or -1 if no ID was found
     */
    public int getEntityID(final Entity entity) {
        for (final int id : storedEntities.keySet().toArray(new Integer[0])) {
            final Entity ent = storedEntities.get(id);
            if (ent.equals(entity)) return id;
        }
        return -1;
    }

    /**
     * Get entity ID of this client on server
     * 
     * @return client's entity ID
     */
    public int getEntityID() {
        return entityID;
    }

    /**
     * Set client's pitch and yaw to face provided entity
     * 
     * @param entity entity to face
     * @throws IOException when there was an error sending packet
     */
    public void lookAt(final Entity entity) throws IOException {
        lookAt(entity.getX(), entity instanceof Player ? entity.getY() + 2 : entity.getY(), entity.getZ());
    }

    /**
     * Set client's pitch and yaw to face a location
     * 
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @throws IOException when there was an error sending packet
     */
    public void lookAt(final double x, final double y, final double z) throws IOException {
        final double dX = x - this.x;
        final double dY = y - (this.y + 2);
        final double dZ = z - this.z;

        final double distXZ = Math.sqrt(dX * dX + dZ * dZ);
        final double distY = Math.sqrt(distXZ * distXZ + dY * dY);

        yaw = (float) (Math.acos(dX / distXZ) * 180 / Math.PI);
        pitch = (float) (Math.acos(dY / distY) * 180 / Math.PI);

        if (dZ < 0) {
            yaw += Math.abs(180 - yaw) * 2;
        }

        this.yaw -= 90;
        this.pitch -= 90;
        sendPacket(PacketFactory.constructPacket(reg, "ClientPlayerPositionAndLookPacket", this.x, this.y, this.z, yaw,
                pitch, true));
    }

    /**
     * Set client's yaw. This method only sets internal variable, it does NOT change
     * client's position on server.
     * 
     * @param yaw new yaw value
     */
    protected void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    /**
     * Set client's pitch. This method only sets internal variable, it does NOT
     * change client's position on server.
     * 
     * @param pitch new pitch value
     */
    protected void setPitch(final float pitch) {
        this.pitch = pitch;
    }

    private Thread movingThread = null;

    /**
     * Set client's look on server.
     * 
     * @param direction player's look
     */
    public void setLook(final float direction) {
        try {
            this.yaw = direction;
            sendPacket(PacketFactory.constructPacket(reg, "ClientPlayerPositionAndLookPacket", x, y, z, direction, 0f,
                    true));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Move client on server.
     * 
     * @param direction    direction to move to from 0 to 7
     * @param speed        walking speed, from 0 to 1. Too high values may cause
     *                     client to be kicked or even banned from server.
     * @param blocks       distance to move
     * @param lockPosition if set to true, client will only look in target position
     *                     without moving
     */
    public void move(final int direction, final double speed, final double blocks, final boolean lockPosition) {
        if (movingThread == null || !movingThread.isAlive()) {
            movingThread = new Thread(new Runnable() {
                private final double speedModifier = speed;

                @Override
                public void run() {
                    for (int x = 0; x < (1 / speedModifier) * blocks; x++) {
                        try {
                            Thread.sleep(1000 / 20);
                        } catch (final InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        if (MinecraftClient.this.x == Integer.MIN_VALUE) return;
                        double tx = MinecraftClient.this.x;
                        double tz = MinecraftClient.this.z;
                        float nyaw = 0;
                        switch (direction) {
                            case 0: {
                                tz += speedModifier;
                                nyaw = 0;
                                break;
                            }
                            case 1: {
                                tz += speedModifier;
                                tx += speedModifier;
                                nyaw = -45;
                                break;
                            }
                            case 2: {
                                tx += speedModifier;
                                nyaw = -90;
                                break;
                            }
                            case 3: {
                                tz -= speedModifier;
                                tx += speedModifier;
                                nyaw = -135;
                                break;
                            }
                            case 4: {
                                tz -= speedModifier;
                                nyaw = 180;
                                break;
                            }
                            case 5: {
                                tz -= speedModifier;
                                tx -= speedModifier;
                                nyaw = 135;
                                break;
                            }
                            case 6: {
                                tx -= speedModifier;
                                nyaw = 90;
                                break;
                            }
                            case 7: {
                                tz += speedModifier;
                                tx -= speedModifier;
                                nyaw = 45;
                                break;
                            }
                            default: {
                                break;
                            }
                        }

                        setLook(nyaw);
                        if (lockPosition) return;
                        try {
                            setX(tx);
                            setZ(tz);
                            sendPacket(PacketFactory.constructPacket(reg, "ClientPlayerPositionAndLookPacket", tx,
                                    MinecraftClient.this.y, tz, MinecraftClient.this.yaw, 0f, true));
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            movingThread.start();
        }
    }

    /**
     * Request statistics update from server.<br>
     * Server will respond with Statistics (see {@link ServerStatisticsPacket}), but
     * it may not respond at all if another request was made recently.
     * 
     * @throws IOException thrown when there was an error sending packet.
     */
    public void refreshStatistics() throws IOException {
        sendPacket(PacketFactory.constructPacket(reg, "ClientStatusPacket", 1));
    }

    /**
     * Sets window currently shown to client
     * 
     * @param id  window's id
     * @param win opened window
     */
    protected void setOpenWindow(final int id, final ItemsWindow win) {
        for (final ItemsWindow window : openWindows.values()) {
            window.closeWindow(true);
        }
        inventory.closeWindow(true);
        openWindows.clear();
        openWindows.put(id, win);
    }

    /**
     * Get all opened window, except player's inventory window
     * 
     * @return map containing currently open windows with their IDs as keys
     */
    public Map<Integer, ItemsWindow> getOpenWindows() {
        return new HashMap<Integer, ItemsWindow>(openWindows);
    }

    /**
     * Get player's inventory
     * 
     * @return player's inventory window
     */
    public ItemsWindow getInventory() {
        return inventory;
    }

    /**
     * Check if packet compression is enabled
     * 
     * @return whether compression is enabled
     */
    public boolean isCompression() {
        return compression;
    }

    /**
     * Set packet compression
     * 
     * @param compression whether compression should be enabled
     */
    public void setCompression(final boolean compression) {
        this.compression = compression;
    }

    /**
     * Check if client is connected
     * 
     * @return connected state
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * 
     * @return if this client should connect with Forge support
     */
    public boolean isForge() {
        return forge;
    }

    /**
     * @return client's online UUID
     */
    public String getAuthID() {
        return authID;
    }

    /**
     * @return client's authentication token
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * @return authentication method used by client
     */
    public AuthType getAuthType() {
        return authType;
    }

    /**
     * Get map of all stored and alive entities<br>
     * It's completely client-side and may get de-synced occasionally
     * 
     * @return map of stored entities
     */
    public Map<Integer, Entity> getStoredEntities() {
        return storedEntities;
    }

    /**
     * Get entity for provided ID
     * 
     * @param id id of the entity
     * @return Entity associated with this ID or null if none found
     */
    public Entity getEntity(final int id) {
        return storedEntities.containsKey(id) ? storedEntities.get(id) : null;
    }

    /**
     * Get this client's Unique ID (UUID)
     * 
     * @return Client's UID
     */
    public UUID getUid() {
        return uid;
    }

    /**
     * Set client's internal UUID<br>
     * Please note id does not change client's UID on server side
     * 
     * @param uid new UID
     */
    protected void setUid(final UUID uid) {
        this.uid = uid;
    }

    /**
     * Get currently tracked entity
     * 
     * @return tracked entity ID or -1 if none
     */
    public int getTrackedEntity() {
        return trackedEntity;
    }

    /**
     * Set new tracked entity
     * 
     * @param trackedEntity new entity to be tracked
     * @param attack        whether to attack this entity or not
     */
    public void setTrackedEntity(final int trackedEntity, final boolean attack) {
        this.trackedEntity = trackedEntity;
        isTrackedAttacking = attack;
        for (final ClientListener listener : getClientListeners(true).toArray(new ClientListener[0])) {
            listener.changedTrackedEntity(trackedEntity, this);
        }
    }

    /**
     * Calculate distance between client and provided entity
     * 
     * @param entity entity to calculate distance to
     * @return distance between client and entity in blocks
     */
    public double distanceTo(final Entity entity) {
        return Math
                .sqrt(Math.pow(x - entity.getX(), 2) + Math.pow(y - entity.getY(), 2) + Math.pow(z - entity.getZ(), 2));
    }

    /**
     * Get clinet's registry responsible for providing packet IDs
     * 
     * @return client's registry
     */
    public PacketRegistry getReg() {
        return reg;
    }

    /**
     * Get client's SOCKS proxy
     * 
     * @return client's proxy or null if none
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * Set client's SOCKS proxy. <br>
     * Only has effect when used before {@link #connect(String)} or
     * {@link #connect(AuthType, String, String)} methods
     * 
     * @param proxy new SOCKS proxy
     */
    public void setProxy(final Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Get first connection date
     * 
     * @return connection time
     */
    public long getStartDate() {
        return startDate;
    }

    /**
     * Get current game state
     * 
     * @return game state
     */
    public State getState() {
        return state;
    }

    /**
     * Get default item windows factory
     * 
     * @return item windows factory bound to this client
     */
    public ItemWindowsFactory getWindowsFactory() {
        return windowsFactory;
    }
}
