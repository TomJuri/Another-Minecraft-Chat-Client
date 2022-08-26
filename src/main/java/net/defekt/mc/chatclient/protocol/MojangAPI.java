package net.defekt.mc.chatclient.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import net.defekt.mc.chatclient.protocol.data.Hosts;
import net.defekt.mc.chatclient.protocol.io.IOUtils;
import net.defekt.mc.chatclient.ui.Messages;

/**
 * This class is used to communicate with Mojang API
 * 
 * @author Defective4
 *
 */
public class MojangAPI {

    /**
     * Class containing a json response from server
     * 
     * @author Defective4
     *
     */
    public static class RequestResponse {
        private final int code;
        private final String response;

        /**
         * @param code     response code
         * @param response response data
         */
        public RequestResponse(final int code, final String response) {
            super();
            this.code = code;
            this.response = response;
        }

        /**
         * @return response code
         */
        public int getCode() {
            return code;
        }

        /**
         * @return response data
         */
        public String getResponse() {
            return response;
        }

        /**
         * @return response parsed to json
         */
        public JsonObject getJson() {
            return new JsonParser().parse(response).getAsJsonObject();
        }

    }

    /**
     * Get last known UUID of specified player
     * 
     * @param username player's username
     * @return UUID of player
     * @throws IOException thrown whene there was error communicating with API
     */
    public static String getUUID(final String username) throws IOException {
        final String js = new String(IOUtils
                .readFully(new URL(Hosts.MOJANG_APISERVER + "/users/profiles/minecraft/" + username).openStream()));
        if (js.isEmpty()) return null;

        return new JsonParser().parse(js).getAsJsonObject().get("id").getAsString();
    }

    private static final Map<String, MojangUser> cachedAccounts = new HashMap<String, MojangUser>();

    /**
     * Authenticates user against given endpoint
     * 
     * @param username client's username or email
     * @param password client's password
     * @param endpoint authentication server's address
     * @return information containing data required for client to join a server
     * @throws MalformedURLException never thrown under normal circumstances
     * @throws IOException           thrown when there was ANY error authenticating
     *                               the user
     */
    public static MojangUser authenticateUser(final String username, final String password, final String endpoint)
            throws MalformedURLException, IOException {
        RequestResponse resp;
        try {
            resp = makeJSONRequest(endpoint + "/authenticate", new HashMap<String, JsonElement>() {
                {
                    put("agent", new JsonPrimitive("Minecraft"));
                    put("username", new JsonPrimitive(username));
                    put("password", new JsonPrimitive(password));
                    put("requestUser", new JsonPrimitive(false));
                }
            });
        } catch (final Exception ex) {
            throw new IOException(Messages.getString("MojangAPI.authConnectError") + ": " + ex.toString());
        }
        JsonObject json;
        try {
            json = resp.getJson();
            if (json == null) throw new IllegalStateException();
        } catch (final Exception ex) {
            throw new IOException(Messages.getString("MojangAPI.authResponseError"));
        }

        if (json.has("error")) {
            final String errorMsg = json.has("errorMessage") ? json.get("errorMessage").getAsString()
                    : json.get("error").getAsString();
            throw new IOException(Messages.getString("MojangAPI.authError") + ": " + errorMsg);
        } else if (json.has("accessToken") && json.has("selectedProfile")) {
            final String accessToken = json.get("accessToken").getAsString();
            final JsonObject selected = json.getAsJsonObject("selectedProfile");
            final String id = selected.get("id").getAsString();
            final String name = selected.get("name").getAsString();
            final MojangUser mUser = new MojangUser(accessToken, id, name);
            cachedAccounts.put(username, mUser);
            return mUser;
        } else
            throw new IOException(Messages.getString("MojangAPI.mojangResponseError"));
    }

    /**
     * Makes a json request to specified endpoint
     * 
     * @param endpoint server address
     * @param values   json key-value pairs to send with request
     * @return response returned from server
     * @throws MalformedURLException when endpoint is not a valid URL
     * @throws IOException           when there was an error while making request
     */
    public static RequestResponse makeJSONRequest(final String endpoint, final Map<String, JsonElement> values)
            throws MalformedURLException, IOException {
        final JsonObject jsonData = new JsonObject();
        for (final Map.Entry<String, JsonElement> value : values.entrySet()) {
            jsonData.add(value.getKey(), value.getValue());
        }

        final HttpURLConnection con = (HttpURLConnection) new URL(endpoint).openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        final OutputStream os = con.getOutputStream();
        os.write(jsonData.toString().getBytes());
        os.close();

        final String response = new String(IOUtils.readFully(con.getInputStream(), true));

        return new RequestResponse(con.getResponseCode(), response);
    }

    /**
     * Get skin URL of specified player
     * 
     * @param uuid player's UUID
     * @return player's skin URL
     * @throws IOException thrown when there was error communicating with API
     */
    public static String getSkin(final String uuid) throws IOException {
        final JsonObject el = new JsonParser()
                .parse(new String(IOUtils.readFully(
                        new URL(Hosts.MOJANG_SESSIONSERVER + "/session/minecraft/profile/" + uuid).openStream())))
                .getAsJsonObject();
        if (el.has("error")) return null;

        for (final JsonElement rel : el.get("properties").getAsJsonArray()) {
            final JsonObject obj = rel.getAsJsonObject();
            if (obj.get("name").getAsString().equals("textures")) return obj.get("value").getAsString();
        }

        return null;
    }
}
