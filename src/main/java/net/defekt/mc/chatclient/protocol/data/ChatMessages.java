package net.defekt.mc.chatclient.protocol.data;

import com.google.gson.*;
import net.defekt.mc.chatclient.protocol.MinecraftClient;
import net.defekt.mc.chatclient.protocol.event.ClientListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class containing methods to make parsing chat messages easier
 *
 * @author Defective4
 * @see ClientListener
 * @see ChatColor
 */
public class ChatMessages {

    private static final String pChar = "\u00a7";

    private ChatMessages() {
    }

    /**
     * Replace specified code character in the message with '§'
     *
     * @param code
     * @param message message to translate
     * @return translated message
     */
    public static String translateColorCodes(final char code, final String message) {
        return message.replace(code, '§');
    }

    /**
     * Parse JSON chat message
     *
     * @param json chat message
     * @return parsed text
     */
    public static String parse(final String json) {
        return parse(json, null);
    }

    /**
     * Parse JSON chat message. <br>
     * Specifying a {@link MinecraftClient} instance allows the parser<br>
     * to translate player-specific elements.
     *
     * @param json
     * @param client
     * @return parsed, human-readable chat message
     */
    public static String parse(String json, final MinecraftClient client) {
        try {
            final JsonElement element = JsonParser.parseString(json);
            String text = "";

            if (element instanceof JsonObject) {
                final JsonObject obj = element.getAsJsonObject();
                text = obj.has("text") ? obj.get("text").getAsString() : "";
                if (obj.has("translate")) {
                    final String translate = obj.get("translate").getAsString();
                    final String translated = TranslationUtils.translateKey(translate);
                    final List<String> with = new ArrayList<String>();

                    if (obj.has("with")) {
                        final JsonArray withArray = obj.getAsJsonArray("with");
                        for (final JsonElement el : withArray) {
                            with.add(parse(el.toString(), client));
                        }
                    }

                    text = String.format(translated, (Object[]) with.toArray(new String[0]));
                }

                if (client != null) {
                    if (obj.has("amcPlayer")) {
                        try {
                            final UUID uid = UUID.fromString(obj.get("amcPlayer").getAsString());
                            final PlayerInfo inf = client.getPlayersTabList().get(uid);
                            if (inf != null) {
                                text = inf.getName();
                            }
                        } catch (final Exception e) {
                        }
                    }
                }

                if (obj.has("color")) {
                    text = pChar + ChatColor.translateColorName(obj.get("color").getAsString()) + text;
                }

                if (obj.has("extra")) {
                    text += parse(obj.get("extra").toString(), client);
                }

            } else if (element instanceof JsonArray) {
                for (final JsonElement el : element.getAsJsonArray()) {
                    text += parse(el.toString(), client);
                }
            } else if (element instanceof JsonPrimitive) return element.getAsString();

            return text;
        } catch (final Exception ex) {
            return json;
        }
    }

    /**
     * Remove colors from message.<br>
     * This method removes all color and formatting codes from message.
     *
     * @param message message to remove colors from
     * @return colorless message
     */
    public static String removeColors(final String message) {
        final StringBuilder colorless = new StringBuilder();
        final char[] chs = message.toCharArray();
        for (int x = 0; x < chs.length; x++)
            if (chs[x] == pChar.charAt(0)) {
                x++;
            } else {
                colorless.append(chs[x]);
            }

        return colorless.toString();
    }
}
