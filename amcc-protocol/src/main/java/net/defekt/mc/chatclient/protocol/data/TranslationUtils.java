package net.defekt.mc.chatclient.protocol.data;

import com.google.gson.*;
import net.defekt.mc.chatclient.protocol.ProtocolEntry;
import net.defekt.mc.chatclient.protocol.ProtocolNumber;
import net.defekt.mc.chatclient.protocol.data.UserPreferences.Language;
import net.defekt.mc.chatclient.protocol.packets.PacketFactory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains translation keys used by Minecraft chat messages.<br>
 * They are used in parsing of chat messages
 *
 * @author Defective4
 * @see ChatMessages
 */

public class TranslationUtils {
    private static final Map<Language, Map<String, String>> translationKeys = new HashMap<UserPreferences.Language, Map<String, String>>() {
        {
            final Language lang = UserPreferences.prefs().getAppLanguage();
            final Map<String, String> kMap = new HashMap<String, String>();
            try (final Reader reader = new InputStreamReader(TranslationUtils.class.getResourceAsStream(
                    "/resources/lang/minecraft/" + lang.getCode().toLowerCase() + ".json"))) {
                final JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                reader.close();

                for (final Map.Entry<String, JsonElement> element : obj.entrySet()) {
                    if (element.getValue() instanceof JsonPrimitive) {
                        try {
                            kMap.put(element.getKey(), element.getValue().getAsString());
                        } catch (final Exception e) {

                        }
                    }
                }

            } catch (final Exception e) {
                e.printStackTrace();
            }
            put(lang, kMap);
        }

    };
    private static final Map<Integer, Map<Integer, ItemInfo>> items = new HashMap<Integer, Map<Integer, ItemInfo>>() {
        {
            final List<Integer> protocols = new ArrayList<Integer>();
            final Map<Integer, Integer> protocolBinds = PacketFactory.getProtocolBinds();
            for (final ProtocolNumber protocol : ProtocolNumber.values()) {
                int protocolNum = protocol.protocol;
                if (protocolBinds.containsKey(protocolNum)) {
                    protocolNum = protocolBinds.get(protocolNum);
                }
                if (!protocols.contains(protocolNum)) {
                    protocols.add(protocolNum);
                }
            }

            for (final int protocol : protocols) {
                final ProtocolEntry pNum = ProtocolNumber.getForNumber(protocol);
                try {
                    final HashMap<Integer, ItemInfo> infs = new HashMap<>();
                    final String pName = pNum.name;
                    if (TranslationUtils.class.getResourceAsStream("/resources/items/" + pName + ".json") == null) {
                        continue;
                    }
                    final JsonArray el = JsonParser.parseReader(new InputStreamReader(TranslationUtils.class.getResourceAsStream(
                            "/resources/items/" + pName + ".json"))).getAsJsonArray();
                    for (final JsonElement elem : el) {
                        final JsonObject job = elem.getAsJsonObject();
                        final JsonObject items = job.get("items").getAsJsonObject().get("item").getAsJsonObject();
                        for (final Entry<String, JsonElement> item : items.entrySet()) {
                            final String itemS = item.getKey();
                            final JsonObject itemData = item.getValue().getAsJsonObject();
                            final int itemID = itemData.get("numeric_id").getAsInt();
                            String itemName = itemS;
                            if (itemData.has("name")) {
                                itemName = translateKey("item." + itemData.get("name").getAsString() + ".name");
                                if (itemName.equals(itemData.get("name")
                                                            .getAsString()) && itemData.has("display_name")) {
                                    itemName = itemData.get("display_name").getAsString();
                                }
                            } else if (itemData.has("display_name")) {
                                itemName = itemData.get("display_name").getAsString();
                            }
                            infs.put(itemID, new ItemInfo(itemName, itemS));
                        }
                    }
                    put(protocol, infs);

                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private TranslationUtils() {
    }

    /**
     * Get an item info from its id and protocol
     *
     * @param id       item's ID
     * @param protocol protocol of this item
     * @return item information
     */
    public static ItemInfo getItemForID(final int id, int protocol) {
        if (PacketFactory.getProtocolBinds().containsKey(protocol)) {
            protocol = PacketFactory.getProtocolBinds().get(protocol);
        }
        final ItemInfo none = new ItemInfo("" + id, "" + id);

        if (items.containsKey(protocol)) {
            final Map<Integer, ItemInfo> itemMap = items.get(protocol);
            return itemMap.containsKey(id) ? itemMap.get(id) : none;
        }
        return none;
    }

    /**
     * Translate a key
     *
     * @param key key
     * @return translated string
     */
    public static String translateKey(final String key) {
        final Language lang = translationKeys.containsKey(UserPreferences.prefs().getAppLanguage()) ?
                UserPreferences.prefs().getAppLanguage() :
                Language.English;
        final Map<String, String> kMap = translationKeys.get(lang);
        if (kMap.containsKey(key)) return kMap.get(key);
        return key;
    }
}
