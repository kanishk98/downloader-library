package com.kanishk.mozilladownloader;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class Util {
    public static String generateUID() {
        return UUID.randomUUID().toString();
    }
    public static long findTotalBytes(String stringUrl) {
        // TODO: Use Apache NetCommons library for finding size of FTP download
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("Util", e.getStackTrace().toString());
        }
        if (url == null) {
            return -1;
        }
        String protocol = url.getProtocol();
        if (protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https")) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                return connection.getContentLength();
            } catch (java.io.IOException e) {
                Log.e("Util", e.getStackTrace().toString());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                    return -1;
                }
            }
        }
        // non-HTTP download, implement infinite progress bar
        return Long.MAX_VALUE;
    }
}
