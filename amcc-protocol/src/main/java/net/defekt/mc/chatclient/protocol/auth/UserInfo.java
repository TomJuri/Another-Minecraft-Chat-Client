package net.defekt.mc.chatclient.protocol.auth;

import java.io.Serializable;

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public String getUuid() {
        return uuid;
    }

    public String getRefresh() {
        return refresh;
    }

    public String getSkin() {
        return skin;
    }

    @Override
    public String toString() {
        return username;
    }
}
