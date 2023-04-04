package net.defekt.mc.chatclient.protocol.data;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.defekt.mc.chatclient.protocol.auth.UserInfo;
import net.defekt.mc.chatclient.protocol.packets.general.clientbound.play.ServerChatMessagePacket.Position;
import net.defekt.mc.chatclient.protocol.packets.general.serverbound.play.ClientResourcePackStatusPacket.Status;

/**
 * Class containing user's preferences.<br>
 * It is intended to be saved on disk every time the application exits.
 * 
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public class UserPreferences implements Serializable {

    private static final long serialVersionUID = 5064975536053236721L;

    private static UserPreferences instance;

    private List<String> enabledPlugins;
    private transient List<String> haltedPlugins;
    private List<String> deletedPlugins;
    private List<String> trustedAuthors;
    private List<UserInfo> msUsers;

    public List<String> getTrustedAuthors() {
        if (trustedAuthors == null) trustedAuthors = new ArrayList<>();
        return trustedAuthors;
    }

    private String userID = generateHWID();

    public String getUserID() {
        if (userID == null) userID = generateHWID();
        return userID;
    }

    public static String generateUserID(final long seed) {
        final Random rand = new Random(seed);
        String id = "User";
        while (id.length() < 16)
            id += Integer.toString(rand.nextInt(10));
        return id;
    }

    public static String generateUserID() {
        final Random rand = new Random();
        String id = "User";
        while (id.length() < 16)
            id += Integer.toString(rand.nextInt(10));
        return id;
    }

    private static String generateHWID() {
        try {
            long seed = 0;
            final Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            while (ifaces.hasMoreElements()) {
                final NetworkInterface iface = ifaces.nextElement();
                if (!iface.isLoopback() && iface.getHardwareAddress() != null) {
                    final byte[] addr = iface.getHardwareAddress();
                    for (int x = 0; x < addr.length; x++) {
                        seed += (addr[x] * Math.pow(100, x + 1));
                    }
                }
            }
            return generateUserID(seed);
        } catch (final Exception ex) {
            ex.printStackTrace();
            return generateUserID();
        }
    }

    public List<String> getEnabledPlugins() {
        if (enabledPlugins == null) enabledPlugins = new ArrayList<>();
        return enabledPlugins;
    }

    public List<String> getHaltedPlugins() {
        if (haltedPlugins == null) haltedPlugins = new ArrayList<>();
        return haltedPlugins;
    }

    public List<String> getDeletedPlugins() {
        if (deletedPlugins == null) deletedPlugins = new ArrayList<>();
        return deletedPlugins;
    }

    public boolean isEnabled(final String name) {
        return enabledPlugins.contains(name);
    }

    public static UserPreferences prefs() {
        if (instance == null) instance = UserPreferences.load();
        return instance;
    }

    private UserPreferences() {
        initDefaults();
    }

    private void initDefaults() {
        if (brand == null) {
            brand = "vanilla";
        }

        if (resourcePackMessage == null) {
            resourcePackMessage = "";
        }

        if (trayMessageMode == null) {
            trayMessageMode = Constants.TRAY_MESSAGES_KEY_MENTION;
        }

        if (unicodeCharactersMode == null) {
            unicodeCharactersMode = Constants.UNICODECHARS_KEY_AUTO;
        }

        if (maxPacketsOnList <= 0) {
            maxPacketsOnList = 500;
        }

        if (proxies == null) {
            proxies = Collections.synchronizedList(new ArrayList<ProxySetting>());
        }

        if (uiTheme == null || uiTheme.isEmpty()) {
            uiTheme = "System";
        }

        if (configVersion < 100) configVersion = DEFAULT_CONFIG_VERSION;

        if (openCounts < 0) openCounts = 1;

        if (configVersion < 101) autoLoginCommand = "login %s";
    }

    /**
     * Skin rules are used to adjust skin cache behavior
     * 
     * @author Defective4
     *
     */
    public static enum SkinRule {
        /**
         * Indicates that skins should be fetched from server
         */
        SERVER,
        /**
         * Indicates that skins should be downloaded using Mojang API
         */
        MOJANG_API,
        /**
         * Skins won't be fetched
         */
        NONE
    }

    /**
     * Language enum is used to set application's language
     * 
     * @author Defective4
     *
     */
    public static enum Language {
        English("EN"), Polish("PL"), 简体中文("CN");

        private final String code;

        private Language(final String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public static class Constants {
        public static final String TRAY_MESSAGES_KEY_ALWAYS = "Always";
        public static final String TRAY_MESSAGES_KEY_MENTION = "On mention";
        public static final String TRAY_MESSAGES_KEY_KEYWORD = "On keyword";
        public static final String TRAY_MESSAGES_KEY_NEVER = "Never";

        public static final String UNICODECHARS_KEY_AUTO = "Automatic";
        public static final String UNICODECHARS_KEY_FORCE_UNICODE = "Force Unicode";
        public static final String UNICODECHARS_KEY_FORCE_CUSTOM = "Force Custom Font";

        public static final int WINDOW_CLOSE_ALWAYS_ASK = 0;
        public static final int WINDOW_CLOSE_TO_TRAY = 1;
        public static final int WINDOW_CLOSE_EXIT = 2;
    }

    public static final ColorPreferences defaultColorPreferences = new ColorPreferences();

    protected final List<ServerEntry> servers = Collections.synchronizedList(new ArrayList<ServerEntry>());

    public List<ProxySetting> proxies = null;

    public static class ColorPreferences implements Serializable {

        private static final long serialVersionUID = 1L;
        private String colorEnabledButton = "6f6f6f";
        private String colorEnabledHoverButton = "7c86be";
        private String colorDisabledButton = "2d2d2d";
        private String colorText = Integer.toHexString(Color.white.getRGB()).substring(2);
        private String disabledColorText = Integer.toHexString(Color.lightGray.getRGB()).substring(2);

        public ColorPreferences() {
        }

        public String getColorEnabledButton() {
            return colorEnabledButton;
        }

        public String getColorEnabledHoverButton() {
            return colorEnabledHoverButton;
        }

        public String getColorDisabledButton() {
            return colorDisabledButton;
        }

        public void setColorEnabledButton(final String colorEnabledButton) {
            this.colorEnabledButton = colorEnabledButton;
        }

        public void setColorEnabledHoverButton(final String colorEnabledHoverButton) {
            this.colorEnabledHoverButton = colorEnabledHoverButton;
        }

        public void setColorDisabledButton(final String colorDisabledButton) {
            this.colorDisabledButton = colorDisabledButton;
        }

        public String getColorText() {
            return colorText;
        }

        public String getDisabledColorText() {
            return disabledColorText;
        }

        public void setColorText(final String colorText) {
            this.colorText = colorText;
        }

        public void setDisabledColorText(final String disabledColorText) {
            this.disabledColorText = disabledColorText;
        }

    }

    private String autoLoginCommand = null;

    public String getAutoLoginCommand() {
        if (autoLoginCommand == null) autoLoginCommand = "login %s";
        return autoLoginCommand;
    }

    private static final transient int DEFAULT_CONFIG_VERSION = 101;
    private int configVersion = DEFAULT_CONFIG_VERSION;

    private int openCounts = 0;

    private boolean disableDiscordPresence = false;
    private boolean hideDiscordNickname = false;
    private boolean hideDiscordServer = false;

    private String uiTheme = "System";
    private boolean disableCustomButtons = false;

    private Language appLanguage = Language.English;
    private boolean wasLangSet = false;

    private ColorPreferences colorPreferences = new ColorPreferences();
    private List<String> lastUsernames = new ArrayList<String>();
    private transient boolean usernameAlertSeen = false;

    private Status resourcePackBehavior = Status.LOADED;
    private boolean showResourcePackMessages = true;
    private String resourcePackMessage = "[Resource Pack Received: %res]";
    private Position resourcePackMessagePosition = Position.HOTBAR;

    private SkinRule skinFetchRule = SkinRule.SERVER;

    private boolean ignoreKeepAlive = false;
    private boolean ignoreDisconnect = false;
    private boolean forceLegacySLP = false;
    private int additionalPing = 0;
    private boolean sendMCBrand = true;
    private String brand = "vanilla";

    private String trayMessageMode = Constants.TRAY_MESSAGES_KEY_MENTION;
    private String unicodeCharactersMode = Constants.UNICODECHARS_KEY_AUTO;
    private boolean trayShowDisconnectMessages = true;
    private int closeMode = Constants.WINDOW_CLOSE_ALWAYS_ASK;
    private String[] trayKeyWords = new String[0];

    private boolean enableInventoryHandling = true;
    private boolean hideIncomingWindows = false;
    private boolean hiddenWindowsResponse = true;
    private boolean loadInventoryTextures = true;
    private boolean showWindowsInTray = true;
    private boolean sendWindowClosePackets = true;

    private int maxPacketsOnList = 500;
    private boolean disablePacketAnalyzer = false;

    public static final File oldServerFile = new File("mcc.prefs");
    public static final File serverFile = new File("mcc.prefs.dat");

    public static UserPreferences load() {
        try {
            if (oldServerFile.isFile()) {
                UserPreferences prefs;
                try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(oldServerFile))) {
                    prefs = (UserPreferences) is.readObject();
                    prefs.initDefaults();
                }
                try (OutputStream os = new FileOutputStream(UserPreferences.serverFile)) {
                    final PrintWriter pw = new PrintWriter(Base64.getEncoder().wrap(os));
                    pw.println(new GsonBuilder().setPrettyPrinting().create().toJson(prefs));
                    pw.flush();
                }
                oldServerFile.delete();
                return prefs;
            } else if (serverFile.isFile()) {
                try (InputStream is = new FileInputStream(serverFile)) {
                    final InputStreamReader reader = new InputStreamReader(Base64.getDecoder().wrap(is));
                    final UserPreferences prefs = new Gson().fromJson(reader, UserPreferences.class);
                    prefs.initDefaults();
                    return prefs;
                }
            } else
                return new UserPreferences();
        } catch (final Exception e) {
            e.printStackTrace();
            return new UserPreferences();
        }
    }

    public boolean isLangUnicodeSupported() {
        switch (appLanguage) {
            case 简体中文: {
                return false;
            }
            default:
                return true;
        }
    }

    public String[] getTrayKeyWords() {
        return trayKeyWords;
    }

    public void setTrayKeyWords(final String[] trayKeyWords) {
        this.trayKeyWords = trayKeyWords;
    }

    public List<ServerEntry> getServers() {
        return servers;
    }

    public Status getResourcePackBehavior() {
        return resourcePackBehavior;
    }

    public void setResourcePackBehavior(final Status resourcePackBehavior) {
        this.resourcePackBehavior = resourcePackBehavior;
    }

    public boolean isShowResourcePackMessages() {
        return showResourcePackMessages;
    }

    public String getResourcePackMessage() {
        return resourcePackMessage;
    }

    public void setShowResourcePackMessages(final boolean showResourcePackMessages) {
        this.showResourcePackMessages = showResourcePackMessages;
    }

    public void setResourcePackMessage(final String resourcePackMessage) {
        this.resourcePackMessage = resourcePackMessage;
    }

    public Position getResourcePackMessagePosition() {
        return resourcePackMessagePosition;
    }

    public void setResourcePackMessagePosition(final Position resourcePackMessagePosition) {
        this.resourcePackMessagePosition = resourcePackMessagePosition;
    }

    public SkinRule getSkinFetchRule() {
        return skinFetchRule;
    }

    public void setSkinFetchRule(final SkinRule skinFetchRule) {
        this.skinFetchRule = skinFetchRule;
    }

    public boolean isIgnoreKeepAlive() {
        return ignoreKeepAlive;
    }

    public boolean isSendMCBrand() {
        return sendMCBrand;
    }

    public String getBrand() {
        return brand;
    }

    public void setIgnoreKeepAlive(final boolean ignoreKeepAlive) {
        this.ignoreKeepAlive = ignoreKeepAlive;
    }

    public void setSendMCBrand(final boolean sendMCBrand) {
        this.sendMCBrand = sendMCBrand;
    }

    public void setBrand(final String brand) {
        this.brand = brand;
    }

    public String getTrayMessageMode() {
        return trayMessageMode;
    }

    public int getCloseMode() {
        return closeMode;
    }

    public void setTrayMessageMode(final String trayMessageMode) {
        this.trayMessageMode = trayMessageMode;
    }

    public void setCloseMode(final int closeMode) {
        this.closeMode = closeMode;
    }

    public boolean isTrayShowDisconnectMessages() {
        return trayShowDisconnectMessages;
    }

    public void setTrayShowDisconnectMessages(final boolean trayShowDisconnectMessages) {
        this.trayShowDisconnectMessages = trayShowDisconnectMessages;
    }

    public ColorPreferences getColorPreferences() {
        if (colorPreferences == null) {
            colorPreferences = new ColorPreferences();
        }
        return colorPreferences;
    }

    public int getAdditionalPing() {
        return additionalPing;
    }

    public void setAdditionalPing(final int additionalPing) {
        this.additionalPing = additionalPing;
    }

    public void putUserName(final String username) {
        if (lastUsernames.contains(username)) {
            lastUsernames.remove(username);
        }
        if (!lastUsernames.contains(username)) {
            lastUsernames.add(" ");
            for (int x = lastUsernames.size() - 1; x > 0; x--) {
                lastUsernames.set(x, lastUsernames.get(x - 1));
            }
            lastUsernames.set(0, username);
        }
    }

    public List<String> getLastUserNames() {
        if (lastUsernames == null) {
            lastUsernames = new ArrayList<String>();
        }
        return new ArrayList<String>(lastUsernames);
    }

    public void clearLastUserNames() {
        getLastUserNames();
        lastUsernames.clear();
    }

    public boolean isUsernameAlertSeen() {
        return usernameAlertSeen;
    }

    public void setUsernameAlertSeen(final boolean usernameAlertSeen) {
        this.usernameAlertSeen = usernameAlertSeen;
    }

    public boolean isEnableInventoryHandling() {
        return enableInventoryHandling;
    }

    public boolean isLoadInventoryTextures() {
        return loadInventoryTextures;
    }

    public boolean isShowWindowsInTray() {
        return showWindowsInTray;
    }

    public void setEnableInventoryHandling(final boolean enableInventoryHandling) {
        this.enableInventoryHandling = enableInventoryHandling;
    }

    public void setLoadInventoryTextures(final boolean loadInventoryTextures) {
        this.loadInventoryTextures = loadInventoryTextures;
    }

    public void setShowWindowsInTray(final boolean showWindowsInTray) {
        this.showWindowsInTray = showWindowsInTray;
    }

    public boolean isSendWindowClosePackets() {
        return sendWindowClosePackets;
    }

    public void setSendWindowClosePackets(final boolean sendWindowClosePackets) {
        this.sendWindowClosePackets = sendWindowClosePackets;
    }

    public boolean isHideIncomingWindows() {
        return hideIncomingWindows;
    }

    public boolean isHiddenWindowsResponse() {
        return hiddenWindowsResponse;
    }

    public void setHideIncomingWindows(final boolean hideIncomingWindows) {
        this.hideIncomingWindows = hideIncomingWindows;
    }

    public void setHiddenWindowsResponse(final boolean hiddenWindowsResponse) {
        this.hiddenWindowsResponse = hiddenWindowsResponse;
    }

    public Language getAppLanguage() {
        return appLanguage;
    }

    public void setAppLanguage(final Language appLanguage) {
        this.appLanguage = appLanguage;
        wasLangSet = true;
    }

    public boolean isWasLangSet() {
        return wasLangSet;
    }

    public boolean isIgnoreDisconnect() {
        return ignoreDisconnect;
    }

    public void setIgnoreDisconnect(final boolean ignoreDisconnect) {
        this.ignoreDisconnect = ignoreDisconnect;
    }

    public String getUnicodeCharactersMode() {
        return unicodeCharactersMode;
    }

    public void setUnicodeCharactersMode(final String unicodeCharactersMode) {
        this.unicodeCharactersMode = unicodeCharactersMode;
    }

    public boolean isForceLegacySLP() {
        return forceLegacySLP;
    }

    public void setForceLegacySLP(final boolean forceLegacySLP) {
        this.forceLegacySLP = forceLegacySLP;
    }

    public int getMaxPacketsOnList() {
        return maxPacketsOnList;
    }

    public void setMaxPacketsOnList(final int maxPacketsOnList) {
        this.maxPacketsOnList = maxPacketsOnList;
    }

    public boolean isDisablePacketAnalyzer() {
        return disablePacketAnalyzer;
    }

    public void setDisablePacketAnalyzer(final boolean disablePacketAnalyzer) {
        this.disablePacketAnalyzer = disablePacketAnalyzer;
    }

    public String getUiTheme() {
        return uiTheme;
    }

    public void setUiTheme(final String uiTheme) {
        this.uiTheme = uiTheme;
    }

    public boolean isDisableCustomButtons() {
        return disableCustomButtons;
    }

    public void setDisableCustomButtons(final boolean disableCustomButtons) {
        this.disableCustomButtons = disableCustomButtons;
    }

    public boolean isDisableDiscordPresence() {
        return disableDiscordPresence;
    }

    public boolean isHideDiscordNickname() {
        return hideDiscordNickname;
    }

    public boolean isHideDiscordServer() {
        return hideDiscordServer;
    }

    public void setDisableDiscordPresence(final boolean disableDiscordPresence) {
        this.disableDiscordPresence = disableDiscordPresence;
    }

    public void setHideDiscordNickname(final boolean hideDiscordNickname) {
        this.hideDiscordNickname = hideDiscordNickname;
    }

    public void setHideDiscordServer(final boolean hideDiscordServer) {
        this.hideDiscordServer = hideDiscordServer;
    }

    public int getOpenCounts() {
        return openCounts;
    }

    public void setOpenCounts(final int openCounts) {
        this.openCounts = openCounts;
    }

    public void setAutoLoginCommand(final String autoLoginCommand) {
        this.autoLoginCommand = autoLoginCommand;
    }

    public List<UserInfo> getMsUsers() {
        if (msUsers == null) msUsers = new ArrayList<>();
        return msUsers;
    }
}
