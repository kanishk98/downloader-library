package com.kanishk.mozilladownloader;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.tape2.QueueFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MozillaDownloader {
    
    private static final MozillaDownloader downloader = new MozillaDownloader();
    private static QueueFile queueFile;
    private static final String TAG = "MozillaDownloader";
    
    private MozillaDownloader() {
        // private constructor to prevent external instantiation
    }

    public static QueueFile initDownloader(Context context) throws IOException {
        // must call initDownloader() before using any other function from this class
        File file = new File(context.getFilesDir() + "queue_file.json");
        if (!file.exists()) {
            queueFile = new QueueFile.Builder(file).build();
        }
        return queueFile;
    }


    public static DownloadStatus scheduleDownload(MozillaDownload download) throws IOException {
        // add download to queueFile
        String downloadJson = new Gson().toJson(download);
        queueFile.add(downloadJson.getBytes());
    }

    private static MozillaDownloader getDownloader() {
        return downloader;
    }
}
