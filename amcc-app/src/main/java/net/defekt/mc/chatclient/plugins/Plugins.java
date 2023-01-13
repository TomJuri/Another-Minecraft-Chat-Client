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
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import net.defekt.mc.chatclient.api.AMCPlugin;
import net.defekt.mc.chatclient.api.PluginDescription;
import net.defekt.mc.chatclient.ui.MultipartRequest;

public class Plugins {

    private static final String pluginVerifyURL = "http://127.0.0.1/verify.php";
    private static final String pluginRepoURL = "http://127.0.0.1/repo.json"; // TODO
    public static final String pluginStarsURL = "http://127.0.0.1/stars.php";

    private static final Map<String, Boolean> cache = new ConcurrentHashMap<>();

    public static class StarStats {

        public Map<String, RepoStats> repos = new ConcurrentHashMap<String, Plugins.StarStats.RepoStats>();

        public int getStars(String repo) {
            return repos.containsKey(repo) ? repos.get(repo).stars : 0;
        }

        public boolean hasStarred(String repo) {
            return repos.containsKey(repo) ? repos.get(repo).starred : false;
        }

        public class RepoStats {
            private final int stars;
            private final boolean starred;

            public RepoStats(boolean starred, int stars) {
                this.stars = stars;
                this.starred = starred;
            }

            public int getStars() {
                return stars;
            }

            public boolean isStarred() {
                return starred;
            }
        }
    }

    public static StarStats fetchStars(String uid) {
        StarStats stats;

        try (Reader rdr = new InputStreamReader(new URL(pluginStarsURL + "?id=" + uid).openStream())) {
            stats = new Gson().fromJson(rdr, StarStats.class);
        } catch (Exception e) {
            e.printStackTrace();
            stats = new StarStats();
        }

        return stats;
    }

    public static void verify(Consumer<Exception> errorConsumer, PluginDescription... plugins) {
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
            JsonObject reg = JsonParser.parseString(json).getAsJsonObject();
            for (JsonElement el : reg.get("verified").getAsJsonArray()) {
                if (el instanceof JsonPrimitive) {
                    cache.put(el.getAsString(), true);
                }
            }
            for (JsonElement el : reg.get("malicious").getAsJsonArray()) {
                if (el instanceof JsonPrimitive) {
                    cache.put(el.getAsString(), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorConsumer.accept(e);
        }
    }

    public static final int PLUGIN_MALICIOUS = 2;
    public static final int PLUGIN_VERIFIED = 1;
    public static final int PLUGIN_UNVERIFIED = 0;

    public static int getPluginFlag(PluginDescription plugin) {
        String hash = plugin.sha256();
        if (hash != null) {
            return cache.containsKey(hash) ? cache.get(hash) ? 1 : 2 : 0;
        }
        return 0;
    }

    public static File PLUGIN_DIR = new File("plugins");

    @SuppressWarnings("resource")
    public static AMCPlugin loadPlugin(PluginDescription desc) {
        try {
            URLClassLoader ucl = new URLClassLoader(
                    new URL[] { new URL("file:///" + desc.getOrigin().getAbsolutePath()) },
                    Plugins.class.getClassLoader());

            AMCPlugin instance = (AMCPlugin) ucl.loadClass(desc.getMain()).newInstance();
            instance.onLoaded();
            return instance;
        } catch (Exception e) {
            System.err.println("An error occured while loading " + desc.getName() + "!");
            e.printStackTrace();
            return null;
        }
    }

    public static PluginDescription[] listPlugins() {
        return listPlugins(false);
    }

    public static PluginDescription[] listRemotePlugins(Consumer<Exception> errorConsumer) {
        List<PluginDescription> list = new ArrayList<PluginDescription>();

        try (Reader reader = new InputStreamReader(new URL(pluginRepoURL).openStream())) {
            JsonArray plugins = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement el : plugins) {
                try {
                    PluginDescription desc = new Gson().fromJson(el, PluginDescription.class);
                    parseDesc(desc);
                    list.add(desc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            errorConsumer.accept(e);
        }

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

                    PluginDescription desc = new Gson().fromJson(reader, PluginDescription.class);
                    parseDesc(desc);
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

    private static void parseDesc(PluginDescription desc) throws IOException {
        if (desc.getName() == null) throw new IOException("Plugin name can't be null!");
        if (desc.getApi() == null) throw new IOException("Plugin api can't be null!");
        if (desc.getVersion() == null) throw new IOException("Plugin version can't be null!");
        if (desc.getMain() == null) throw new IOException("Plugin main can't be null!");
    }
}
