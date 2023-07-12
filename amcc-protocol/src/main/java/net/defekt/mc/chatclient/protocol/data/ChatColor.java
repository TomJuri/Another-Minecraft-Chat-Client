package net.defekt.mc.chatclient.protocol.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class containing Minecraft's color codes, their names and their RGB
 * representation
 *
 * @author Defective4
 * @see <a href="https://wiki.vg/Chat#Colors">Chat Colors (wiki.vg)</a>
 */
public class ChatColor {
    private static final Map<String, String> colorCodes = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("black", "0");
            put("dark_blue", "1");
            put("dark_green", "2");
            put("dark_aqua", "3");
            put("dark_red", "4");
            put("dark_purple", "5");
            put("gold", "6");
            put("gray", "7");
            put("dark_gray", "8");
            put("blue", "9");
            put("green", "a");
            put("aqua", "b");
            put("red", "c");
            put("light_purple", "d");
            put("yellow", "e");
            put("white", "f");
        }
    };
    private static final Map<String, String> colors = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("0", "0:0:0");
            put("1", "0:0:170");
            put("2", "0:170:0");
            put("3", "0:170:170");
            put("4", "170:0:0");
            put("5", "170:0:170");
            put("6", "255:170:0");
            put("7", "170:170:170");
            put("8", "85:85:85");
            put("9", "85:85:255");
            put("a", "85:255:85");
            put("b", "85:255:255");
            put("c", "255:85:85");
            put("d", "255:85:255");
            put("e", "255:255:85");
            put("f", "255:255:255");
            put("r", "255:255:255");

            put("o", "255:255:255");
            put("k", "255:255:255");
            put("l", "255:255:255");
            put("m", "255:255:255");
            put("n", "255:255:255");
        }
    };

    private ChatColor() {
    }

    /**
     * Parse HEX colored message string to json array
     *
     * @param hexed HEX message string
     * @return json array
     */
    public static JsonArray parseColors(String hexed) {
        JsonArray parts = new JsonArray();
        String result = hexed;
        String[] split = result.split("\u00a7#");
        for (String sec : split) {
            JsonObject obj = new JsonObject();
            try {
                if (sec.length() >= 6) {
                    String color = sec.substring(0, 6);
                    String txt = sec.substring(6);
                    Integer.parseInt(color, 16);
                    obj.add("color", new JsonPrimitive(color));
                    obj.add("text", new JsonPrimitive(txt));
                } else throw new NumberFormatException();
            } catch (Exception e) {
                obj.add("color", new JsonPrimitive("ffffff"));
                obj.add("text", new JsonPrimitive(sec));
            }
            parts.add(obj);
        }

        return parts;
    }

    /**
     * Convert all legacy color codes to HEX
     *
     * @param legacy legacy string
     * @return hexed string
     */
    public static String legacyToHex(String legacy) {
        String hexed = "\u00a7#ffffff" + legacy;

        for (String key : colors.keySet()) {
            String[] rgb = colors.get(key).split(":");
            String hex = toHex(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
            hexed = hexed.replace("\u00a7" + key, "\u00a7#" + hex);
        }

        return hexed;
    }

    /**
     * Converts RGB to HEX value (######)
     *
     * @param r Red value
     * @param g Green value
     * @param b Blue value
     * @return encoded HEX string
     */
    public static String toHex(int r, int g, int b) {
        return pad(Integer.toHexString(r)) + pad(Integer.toHexString(g)) + pad(Integer.toHexString(b));
    }

    private static String pad(String sec) {
        return sec.length() == 1 ? "0" + sec : sec;
    }

    /**
     * Translate Minecraft color code (0-9 a-f) to {@link Color} object.<br>
     * Formatting codes (k-m) are NOT supported yet.
     *
     * @param code color code
     * @return RGB color
     * @deprecated
     */
    public static Color translateColorCode(final String code) {
        if (colors.containsKey(code)) {
            final String[] rgb = colors.get(code).split(":");
            final int r = Integer.parseInt(rgb[0]);
            final int g = Integer.parseInt(rgb[1]);
            final int b = Integer.parseInt(rgb[2]);
            return new Color(r, g, b);
        } else return Color.white;
    }

    /**
     * Translate Minecraft chat color name to color code
     *
     * @param name color name
     * @return color code
     */
    public static String translateColorName(String name) {
        name = name.toLowerCase();

        if (name.contains("#") && name.length() > 1) return name;

        return colorCodes.containsKey(name) ? colorCodes.get(name) : colorCodes.get("white");
    }
}
