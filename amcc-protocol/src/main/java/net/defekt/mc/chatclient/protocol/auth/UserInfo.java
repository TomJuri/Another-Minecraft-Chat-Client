package net.defekt.mc.chatclient.protocol.auth;

import java.io.Serializable;

@SuppressWarnings("javadoc")
public class UserInfo implements Serializable {
    private String username, token, uuid, refresh, skin;

    public UserInfo(String username, String token, String uuid, String refresh, String skin) {
        super();
        this.username = username;
        this.token = token;
        this.uuid = uuid;
        this.refresh = refresh;
        this.skin = skin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    @Override
    public String toString() {
        return username;
    }
}
