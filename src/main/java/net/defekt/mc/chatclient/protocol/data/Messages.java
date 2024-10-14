package net.defekt.mc.chatclient.protocol.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class used to access externalized strings keys
 */
public class Messages {
    private static final Properties props = new Properties();

    static {
        try (InputStream is = Messages.class.getResourceAsStream("/resources/lang/" + UserPreferences.prefs()
                                                                                                     .getAppLanguage()
                                                                                                     .getCode()
                                                                                                     .toUpperCase() + ".properties")) {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Messages() {
    }

    /**
     * Get string for this key
     *
     * @param key string key
     * @return string's key
     */
    public static String getString(final String key) {
        try {
            return props.getProperty(key, "!" + key + "!");
        } catch (final Exception e) {
            return '!' + key + '!';
        }
    }
}
