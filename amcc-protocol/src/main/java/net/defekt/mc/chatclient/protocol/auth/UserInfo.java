package net.defekt.mc.chatclient.protocol.auth;

public class UserInfo {
    private final String id, name, token, refreshToken;

    public UserInfo(String id, String name, String token, String refreshToken) {
        super();
        this.id = id;
        this.name = name;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
