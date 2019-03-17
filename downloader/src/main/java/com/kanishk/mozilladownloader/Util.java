package com.kanishk.mozilladownloader;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class Util {

    private static final String TAG = "Util";

    public static void saveDownload(MozillaDownload download, Context context) throws IOException {
        File pausedDownloads = new File(context.getFilesDir(), Constants.PAUSED_DOWNLOADS);
        pausedDownloads.mkdir();
        File downloadFile = new File(pausedDownloads.getPath() + download.getUid());
        FileOutputStream fileOutputStream = new FileOutputStream(downloadFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(download);
        objectOutputStream.close();
        fileOutputStream.close();
    }
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
        String protocol = url.getProtocol();
        if (protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https")) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                Log.d(TAG, String.valueOf(connection.getContentLength()));
                return connection.getContentLength() < 0 ? Long.MAX_VALUE : connection.getContentLength();
            } catch (java.io.IOException e) {
                Log.e("Util", e.getStackTrace().toString());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        // non-HTTP download, implement infinite progress bar
        return Long.MAX_VALUE;
    }
}
