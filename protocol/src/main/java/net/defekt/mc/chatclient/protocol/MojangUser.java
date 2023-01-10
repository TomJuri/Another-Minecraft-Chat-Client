package net.defekt.mc.chatclient.protocol;

/**
 * Class containing user authentication unfo
 * 
 * @author Defective4
 *
 */
public class MojangUser {
    private String accessToken;
    private String userID;
    private String userName;

    @SuppressWarnings("javadoc")
    protected MojangUser(final String accessToken, final String userID, final String userName) {
        super();
        this.accessToken = accessToken;
        this.userID = userID;
        this.userName = userName;
    }

    /**
     * @return user's access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @return user's online UUID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @return user's in-game name
     */
    public String getUserName() {
        return userName;
    }

    @SuppressWarnings("javadoc")
    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    @SuppressWarnings("javadoc")
    public void setUserID(final String userID) {
        this.userID = userID;
    }

    @SuppressWarnings("javadoc")
    public void setUserName(final String userName) {
        this.userName = userName;
    }
}
