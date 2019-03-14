package com.kanishk.mozilladownloader;

import android.content.Context;

import com.google.gson.Gson;
import com.squareup.tape2.QueueFile;

import java.io.File;
import java.io.IOException;

public class MozillaDownloader {
    
    private static final MozillaDownloader downloader = new MozillaDownloader();
    private QueueFile queueFile;
    private static final String TAG = "MozillaDownloader";
    private DownloadStatusListener downloadStatusListener;
    
    private MozillaDownloader() {
        // private constructor to prevent external instantiation
    }

    private void setupDownloadStatusListener(DownloadStatusListener downloadStatusListener) {
        this.downloadStatusListener = downloadStatusListener;
    }

    public QueueFile initDownloader(Context context) throws IOException {
        // must call initDownloader() before using any other function from this class
        File file = new File(context.getFilesDir() + "queue_file.json");
        if (!file.exists()) {
            queueFile = new QueueFile.Builder(file).build();
        }
        return queueFile;
    }


    public void scheduleDownload(MozillaDownload download) throws IOException {
        // add download to queueFile
        download.setStatus(DownloadStatus.SCHEDULED);
        downloadStatusListener.onStatusChange(download);
        String downloadJson = new Gson().toJson(download);
        queueFile.add(downloadJson.getBytes());
    }

    private static MozillaDownloader getDownloader() {
        return downloader;
    }
}
