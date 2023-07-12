package net.defekt.mc.chatclient.ui;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultipartRequest {
    private final Map<String, byte[]> fields = new ConcurrentHashMap<>();

    public void addField(final String name, final byte[] data) {
        fields.put(name, data);
    }

    public byte[] send(final URL url) throws IOException {

        final String bnd = "WebKitFormBoundary" + Long.toHexString(System.currentTimeMillis());

        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(3000);
        con.setReadTimeout(3000);
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Java");
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + bnd);

        final OutputStream os = con.getOutputStream();
        final PrintWriter pw = new PrintWriter(os);

        for (final Map.Entry<String, byte[]> field : fields.entrySet()) {
            pw.println("--" + bnd);
            pw.println("Content-Disposition: form-data; name=\"" + field.getKey() + "\"");
            pw.println();
            pw.flush();
            os.write(field.getValue());
            os.flush();
            pw.println();
        }

        pw.println("--" + bnd + "--");
        pw.flush();
        final int code = con.getResponseCode();
        final boolean error = code >= 400;
        if (error) throw new IOException("An error occured: " + code + " " + con.getResponseMessage());
        final InputStream in = con.getInputStream();
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            output.write(buffer, 0, len);
        }
        in.close();

        return output.toByteArray();
    }
}
