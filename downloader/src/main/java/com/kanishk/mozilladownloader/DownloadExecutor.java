package com.kanishk.mozilladownloader;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class DownloadExecutor extends IntentService {

    private final String TAG = getClass().getSimpleName();
    private MozillaDownload currentDownload = null;
    private volatile boolean pause = false;
    private volatile boolean cancel = false;
    private IntentFilter intentFilter = new IntentFilter(Constants.STOP_DOWNLOAD);
    private BroadcastReceiver stopDownloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            MozillaDownload download = (MozillaDownload) intent.getSerializableExtra(Constants.MOZILLA_DOWNLOAD);
            if (download.getStatus() == DownloadStatus.PAUSING) {
                pause = true;
            } else if (download.getStatus() == DownloadStatus.CANCELLING) {
                cancel = true;
            }
        }
    };
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public DownloadExecutor() {
        super("DownloadExecutor");
    }

    private void download(MozillaDownload download) {
        try {
            download.setTargetPath(getApplicationContext().getExternalFilesDir("/mozilla/") + download.getUid() +
            download.getUrl().substring(download.getUrl().lastIndexOf(".")));
            File destinationFile = new File(download.getTargetPath());
            URL url = new URL(download.getUrl());
            URLConnection connection = url.openConnection();
            if (destinationFile.exists()) {
                connection.setRequestProperty("Range", "bytes=" + destinationFile.length() + "-");
            }
            ReadableByteChannel readableByteChannel = Channels.newChannel(connection.getInputStream());
            FileChannel downloadChannel = new FileOutputStream(destinationFile, destinationFile.exists()).getChannel();
            download.setStatus(DownloadStatus.RUNNING);
            long initialBytes;
            long downloadedBytes = destinationFile.length();
            do {
                initialBytes = downloadedBytes;
                long chunkBytes = downloadChannel.transferFrom(readableByteChannel, downloadedBytes, download.getChunkBytes());
                downloadedBytes += chunkBytes;
                download.setDownloadedBytes(downloadedBytes);
            } while(downloadedBytes > initialBytes && !pause && !cancel);
            if (pause) {
                pause(download);
            }
            if (cancel) {
                cancel(download);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void pause(MozillaDownload download) {
        download.setStatus(DownloadStatus.PAUSED);
        sharedPreferences = getSharedPreferences(Constants.SAVED_DOWNLOADS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(String.valueOf(download.getUid()), new Gson().toJson(download)).commit();
    }

    private void cancel(MozillaDownload download) {
        // if previously resumed download is being cancelled
        sharedPreferences = getSharedPreferences(Constants.SAVED_DOWNLOADS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.remove(String.valueOf(download.getUid()));
        File tempFile = new File(download.getTargetPath());
        tempFile.delete();
    }

    public DownloadExecutor(String name) {
        super(name);
    }

    @Override
    public void onDestroy() {
        pause(currentDownload);
        unregisterReceiver(stopDownloadReceiver);
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        registerReceiver(stopDownloadReceiver, intentFilter);
        MozillaDownload download = (MozillaDownload) intent.getSerializableExtra(Constants.MOZILLA_DOWNLOAD);
        currentDownload = download;
        download(download);
    }
}
