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

    protected MojangUser(String accessToken, String userID, String userName) {
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
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @SuppressWarnings("javadoc")
    public void setUserID(String userID) {
        this.userID = userID;
    }

    @SuppressWarnings("javadoc")
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
