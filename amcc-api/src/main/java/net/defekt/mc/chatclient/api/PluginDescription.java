package net.defekt.mc.chatclient.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.UUID;

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

    public String sha256() {
        if (origin.exists()) try (InputStream is = new FileInputStream(origin)) {
            MessageDigest sha = MessageDigest.getInstance("sha-256");
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) > 0) {  
                sha.update(buffer, 0, read);
            }
            byte[] digest = sha.digest();
            StringBuilder bd = new StringBuilder();
            for (byte b : digest)
                bd.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            return bd.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUID() {
        return UUID.nameUUIDFromBytes((version + ":" + main + ":" + author + ":" + name).getBytes()).toString();
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
