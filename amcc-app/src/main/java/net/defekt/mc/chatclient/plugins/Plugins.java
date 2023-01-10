package net.defekt.mc.chatclient.plugins;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.gson.Gson;

import net.defekt.mc.chatclient.api.AMCPlugin;
import net.defekt.mc.chatclient.api.PluginDescription;

public class Plugins {

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
                    if (desc.getName() == null) throw new IOException("Plugin name can't be null!");
                    if (desc.getApi() == null) throw new IOException("Plugin api can't be null!");
                    if (desc.getVersion() == null) throw new IOException("Plugin version can't be null!");
                    if (desc.getMain() == null) throw new IOException("Plugin main can't be null!");
                    desc.setOrigin(file);
                    list.add(desc);
                } catch (Exception e) {
                    System.err.println("Failed to load " + name + "!");
                    e.printStackTrace();
                }
            }
        }

        return list.toArray(new PluginDescription[0]);
    }
}
