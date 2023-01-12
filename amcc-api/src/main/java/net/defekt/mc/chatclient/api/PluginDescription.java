package net.defekt.mc.chatclient.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * This class is a container for information associated with a plugin
 * 
 * @author Defective4
 *
 */
@SuppressWarnings("javadoc")
public class PluginDescription {
    private final String name;
    private final String version;
    private final String main;
    private final String[] description;
    private final String author;
    private final String api;
    private final String website;
    private transient File origin;
    private final String remote;

    /**
     * Default constructor
     * 
     * @param name
     * @param version
     * @param main
     * @param description
     * @param author
     * @param api
     * @param website
     * @param remote
     */
    public PluginDescription(String name, String version, String main, String[] description, String author, String api,
            String website, String remote) {
        super();
        this.name = name;
        this.version = version;
        this.main = main;
        this.description = description;
        this.author = author;
        this.api = api;
        this.website = website;
        this.remote = remote;

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

    public URL getWebsite() {
        try {
            return new URL(website);
        } catch (Exception e) {
            return null;
        }
    }

    public String getWebsiteString() {
        URL site = getWebsite();
        return site == null ? null : site.toString();
    }

    public File getOrigin() {
        return origin;
    }

    public void setOrigin(File origin) {
        this.origin = origin;
    }

    public String getRemote() {
        return remote;
    }
}
