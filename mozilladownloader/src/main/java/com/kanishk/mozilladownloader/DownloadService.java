package com.kanishk.mozilladownloader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class DownloadService extends Service {

    private final String TAG = getClass().getSimpleName();

    private long findTotalBytes(URL url) {
        // TODO: Use Apache NetCommons library for finding size of FTP download
        String protocol = url.getProtocol();
        if (protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https")) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                return connection.getContentLength();
            } catch (java.io.IOException e) {
                Log.e(TAG, e.getStackTrace().toString());
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

    private void download(FileChannel downloadChannel, ReadableByteChannel readableByteChannel, long channelPosition, long totalBytes) {
        try {
            downloadChannel.transferFrom(readableByteChannel, channelPosition, totalBytes);
        } catch (java.io.IOException e) {
            Log.e(TAG, e.getStackTrace().toString());
        }
    }

    private void cancel(MozillaDownload download) {
        
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            // TODO: Investigate possible protocols that user may direct to
            // Current implementation supports FTP and HTTP only
            MozillaDownload download = (MozillaDownload) intent.getSerializableExtra(getString(R.string.mozilla_download));
            File destinationFile = new File(download.getTargetPath());
            URL url = new URL(download.getUrl());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileChannel downloadChannel = new FileOutputStream(destinationFile, destinationFile.exists()).getChannel();
            download.setTotalBytes(findTotalBytes(url));
            if (download.getStatus() == DownloadStatus.SCHEDULED && download.getTotalBytes() != -1) {
                download.setStatus(DownloadStatus.RUNNING);
                download(downloadChannel, readableByteChannel, download.getDownloadedBytes(), download.getTotalBytes());
            } else if (download.getStatus() == DownloadStatus.CANCELLING) {
                cancel(download);
            }
        } catch (java.io.IOException e) {
            Log.e(TAG, e.getStackTrace().toString());
        }
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
