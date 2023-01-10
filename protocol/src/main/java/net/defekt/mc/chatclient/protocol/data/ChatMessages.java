package net.defekt.mc.chatclient.protocol.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import net.defekt.mc.chatclient.protocol.MinecraftClient;

/**
 * Class containing methods to make parsing chat messages easier
 * 
 * @see ClientListener
 * @see ChatColor
 * @author Defective4
 *
 */
public class ChatMessages {

    private static final String pChar = "\u00a7";

    private ChatMessages() {
    }

    /**
     * Parse JSON chat message
     * 
     * @param json chat message
     * @return parsed text
     */
    public static String parse(String json) {
        return parse(json, null);
    }

    public static String parse(String json, MinecraftClient client) {
        json = json.replace(pChar + "k", "").replace(pChar + "l", "").replace(pChar + "m", "").replace(pChar + "n", "");
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
                            UUID uid = UUID.fromString(obj.get("amcPlayer").getAsString());
                            PlayerInfo inf = client.getPlayersTabList().get(uid);
                            if (inf != null) {
                                text = inf.getName();
                            }
                        } catch (Exception e) {
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
