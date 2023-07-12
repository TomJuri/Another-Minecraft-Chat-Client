package net.defekt.mc.chatclient.protocol;

/**
 * Enum containing all authentication methods supported by client
 *
 * @author Defective4
 */
@SuppressWarnings("javadoc")
public enum AuthType {
    Offline,
    Microsoft,
    @Deprecated TheAltening;

    public static AuthType[] valuesX() {
        return new AuthType[]{Offline, Microsoft};
    }
}
