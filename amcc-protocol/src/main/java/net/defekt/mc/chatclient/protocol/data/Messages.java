package net.defekt.mc.chatclient.protocol.data;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class used to access externalized strings keys
 *
 */
public class Messages {
    private static final String BUNDLE_NAME = "resources.lang." + UserPreferences.prefs().getAppLanguage().getCode();

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String translateColorCodes(char code, String message) {
        return message.replace(code, '§');
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
            return RESOURCE_BUNDLE.getString(key);
        } catch (final MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
