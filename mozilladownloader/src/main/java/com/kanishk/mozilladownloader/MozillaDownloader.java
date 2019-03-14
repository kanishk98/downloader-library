package com.kanishk.mozilladownloader;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

public class MozillaDownloader {
    
    private static final MozillaDownloader downloader = new MozillaDownloader();
    private Context context;
    private PriorityQueue<MozillaDownload> downloadQueue;
    private DownloadStatusListener downloadStatusListener;
    
    private MozillaDownloader() {
        // private constructor to prevent external instantiation
    }

    private void setupDownloadStatusListener(DownloadStatusListener downloadStatusListener) {
        this.downloadStatusListener = downloadStatusListener;
    }

    private boolean flushQueueToDisk(PriorityQueue<MozillaDownload> queue) {
        File file = new File(context.getFilesDir() + "queue_file.json");
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(new Gson().toJson(queue));
            bufferedWriter.close();
            return true;
        } catch (IOException ioException) {
            return false;
        }
    }

    // TODO: What if app accidentally calls initDownloader() with a different context?
    public void initDownloader(Context context) throws IOException {
        // must call initDownloader() before using any other function from this class
        this.context = context;
        downloadQueue = new PriorityQueue<>(0, new Comparator<MozillaDownload>() {
            @Override
            public int compare(MozillaDownload a, MozillaDownload b) {
                return a.getUid().compareTo(b.getUid());
            }
        });
    }

    public void scheduleDownload(MozillaDownload download) {
        download.setStatus(DownloadStatus.SCHEDULED);
        downloadStatusListener.onStatusChange(download);
        downloadQueue.add(download);
    }

    public boolean pauseDownload(MozillaDownload download) {
        return false;
    }

    public boolean cancelDownload(MozillaDownload download) {
        return false;
    }

    public boolean rescheduleDownload(MozillaDownload download) {
        return false;
    }

    private static MozillaDownloader getDownloader() {
        return downloader;
    }
}
