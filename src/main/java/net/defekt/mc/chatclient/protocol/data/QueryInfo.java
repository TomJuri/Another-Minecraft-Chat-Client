package net.defekt.mc.chatclient.protocol.data;

/**
 * Stores information from server query
 * 
 * @author Defective4
 *
 */
public class QueryInfo {
    private final String motd;
    private final String gamemode;
    private final String map;
    private final String onlinePlayer;
    private final String maxPlayers;

    /**
     * Construct query information object
     * 
     * @param motd         server motd
     * @param gamemode     server's gamemode, usually "SMP"
     * @param map          server's map
     * @param onlinePlayer online players
     * @param maxPlayers   maximum players
     */
    public QueryInfo(final String motd, final String gamemode, final String map, final String onlinePlayer,
            final String maxPlayers) {
        super();
        this.motd = motd;
        this.gamemode = gamemode;
        this.map = map;
        this.onlinePlayer = onlinePlayer;
        this.maxPlayers = maxPlayers;
    }

    /**
     * 
     * @return server's motd
     */
    public String getMotd() {
        return motd;
    }

    /**
     * 
     * @return server's gamemode
     */
    public String getGamemode() {
        return gamemode;
    }

    /**
     * 
     * @return server's map
     */
    public String getMap() {
        return map;
    }

    /**
     * 
     * @return online players
     */
    public String getOnlinePlayer() {
        return onlinePlayer;
    }

    /**
     * 
     * @return maximum players
     */
    public String getMaxPlayers() {
        return maxPlayers;
    }
}
