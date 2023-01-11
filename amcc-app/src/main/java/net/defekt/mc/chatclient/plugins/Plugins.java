package net.defekt.mc.chatclient.plugins;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import net.defekt.mc.chatclient.api.AMCPlugin;
import net.defekt.mc.chatclient.api.PluginDescription;
import net.defekt.mc.chatclient.ui.MultipartRequest;

public class Plugins {

    private static final String pluginVerifyURL = "http://127.0.0.1/verify.php"; // TODO

    private static final Map<String, Boolean> cache = new ConcurrentHashMap<>();

    public static void verify(PluginDescription... plugins) {
        JsonArray root = new JsonArray();
        String hash;
        for (PluginDescription desc : plugins) {
            hash = desc.sha256();
            if (hash != null) root.add(hash);
        }

        try {
            MultipartRequest req = new MultipartRequest();
            req.addField("hashes", root.toString().getBytes("UTF-8"));
            String json = new String(req.send(new URL(pluginVerifyURL)), "utf-8");
            JsonArray ar = JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement el : ar) {
                if (el instanceof JsonPrimitive) {
                    cache.put(el.getAsString(), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isVerified(PluginDescription plugin) {
        String hash = plugin.sha256();
        if (hash != null) {
            return cache.getOrDefault(hash, false);
        }
        return false;
    }

    public static File PLUGIN_DIR = new File("plugins");

    public static void loadPlugin(PluginDescription desc) {
        try (URLClassLoader ucl = new URLClassLoader(
                new URL[] { new URL("file:///" + desc.getOrigin().getAbsolutePath()) },
                Plugins.class.getClassLoader())) {

            AMCPlugin instance = (AMCPlugin) ucl.loadClass(desc.getMain()).newInstance();
            instance.onEnable();
        } catch (Exception e) {
            System.err.println("An error occured while loading " + desc.getName() + "!");
            e.printStackTrace();
        }
    }

    public static PluginDescription[] listPlugins() {
        return listPlugins(false);
    }

    public static PluginDescription[] listRemotePlugins() {
        List<PluginDescription> list = new ArrayList<PluginDescription>();

        return list.toArray(new PluginDescription[0]);
    }

    public static PluginDescription[] listPlugins(boolean allowDuplicates) {
        List<PluginDescription> list = new ArrayList<PluginDescription>();

        if (!PLUGIN_DIR.exists()) PLUGIN_DIR.mkdirs();

        if (PLUGIN_DIR.isDirectory()) {
            for (File file : PLUGIN_DIR.listFiles()) {
                String name = file.getName();
                try (ZipFile zFile = new ZipFile(file)) {
                    ZipEntry entry = zFile.getEntry("plugin.json");
                    if (entry == null) throw new IOException("No plugin.json found in the archive!");
                    Reader reader = new InputStreamReader(zFile.getInputStream(entry));

                    PluginDescription desc = createDesc(reader);
                    desc.setOrigin(file);
                    boolean canAdd = true;
                    if (!allowDuplicates) for (PluginDescription cur : list)
                        if (cur.getUID().equals(desc.getUID())) {
                            canAdd = false;
                            break;
                        }
                    if (canAdd) list.add(desc);
                } catch (Exception e) {
                    System.err.println("Failed to load " + name + "!");
                    e.printStackTrace();
                }
            }
        }

        return list.toArray(new PluginDescription[0]);
    }

    private static PluginDescription createDesc(Reader reader) throws IOException {
        PluginDescription desc = new Gson().fromJson(reader, PluginDescription.class);
        if (desc.getName() == null) throw new IOException("Plugin name can't be null!");
        if (desc.getApi() == null) throw new IOException("Plugin api can't be null!");
        if (desc.getVersion() == null) throw new IOException("Plugin version can't be null!");
        if (desc.getMain() == null) throw new IOException("Plugin main can't be null!");
        return desc;
    }
}
