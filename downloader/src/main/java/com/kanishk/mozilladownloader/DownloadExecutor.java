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
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class DownloadExecutor extends IntentService {

    private final String TAG = getClass().getSimpleName();
    private volatile boolean pause = false;
    private volatile boolean cancel = false;
    private IntentFilter intentFilter = new IntentFilter(Constants.STOP_DOWNLOAD);
    private BroadcastReceiver stopDownloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            MozillaDownload download = (MozillaDownload) intent.getSerializableExtra(Constants.MOZILLA_DOWNLOAD);
            if (download.getStatus() == DownloadStatus.PAUSING) {
                Log.d(TAG, "Pause broadcast received");
                pause = true;
            } else if (download.getStatus() == DownloadStatus.CANCELLING) {
                Log.d(TAG, "Cancel broadcast received");
                cancel = true;
            }
        }
    };
    SharedPreferences sharedPreferences = getSharedPreferences(Constants.SAVED_DOWNLOADS, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();

    public DownloadExecutor() {
        super("DownloadExecutor");
    }

    private void download(MozillaDownload download) {
        try {
            download.setTargetPath(getApplicationContext().getExternalFilesDir("sublime/") + download.getUid() +
            download.getUrl().substring(download.getUrl().lastIndexOf(".")));
            File destinationFile = new File(download.getTargetPath());
            URL url = new URL(download.getUrl());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileChannel downloadChannel = new FileOutputStream(destinationFile, destinationFile.exists()).getChannel();
            download.setStatus(DownloadStatus.RUNNING);
            long initialBytes;
            long downloadedBytes = download.getDownloadedBytes();
            Log.d(TAG, String.valueOf(downloadedBytes));
            Log.d(TAG, String.valueOf(download.getChunkBytes()));
            do {
                initialBytes = downloadedBytes;
                long chunkBytes = downloadChannel.transferFrom(readableByteChannel, downloadedBytes, download.getChunkBytes());
                downloadedBytes += chunkBytes;
                Log.d(TAG, "Downloaded " + downloadedBytes + " bytes");
            } while(downloadedBytes > initialBytes && !pause && !cancel);
            if (pause) {
                pause(download);
            }
            if (cancel) {
                cancel(download);
            }
            Log.d(TAG, "Download over at " + downloadedBytes);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void pause(MozillaDownload download) {
        download.setStatus(DownloadStatus.PAUSED);
        editor.putString(String.valueOf(download.getUid()), new Gson().toJson(download)).commit();
    }

    private void cancel(MozillaDownload download) {
        // if previously resumed download is being cancelled
        editor.remove(String.valueOf(download.getUid()));
        File tempFile = new File(download.getTargetPath());
        tempFile.delete();
    }

    public DownloadExecutor(String name) {
        super(name);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(stopDownloadReceiver);
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        registerReceiver(stopDownloadReceiver, intentFilter);
        MozillaDownload download = (MozillaDownload) intent.getSerializableExtra(Constants.MOZILLA_DOWNLOAD);
        download(download);
    }
}