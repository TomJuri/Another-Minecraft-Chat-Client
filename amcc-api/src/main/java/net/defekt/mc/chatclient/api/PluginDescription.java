package net.defekt.mc.chatclient.api;

import java.io.File;

public class PluginDescription {
    private final String name;
    private final String version;
    private final String main;
    private final String[] description;
    private final String author;
    private final String api;
    private final String website;
    private transient File origin;

    public PluginDescription(String name, String version, String main, String[] description, String author, String api,
            String website) {
        super();
        this.name = name;
        this.version = version;
        this.main = main;
        this.description = description;
        this.author = author;
        this.api = api;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getMain() {
        return main;
    }

    public String[] getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getApi() {
        return api;
    }

    public String getWebsite() {
        return website;
    }

    public File getOrigin() {
        return origin;
    }

    public void setOrigin(File origin) {
        this.origin = origin;
    }
}
