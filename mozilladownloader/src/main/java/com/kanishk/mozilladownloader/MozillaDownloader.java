package com.kanishk.mozilladownloader;

import android.app.PendingIntent;
import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

import android.app.AlarmManager;
import android.content.Intent;

public class MozillaDownloader {
    
    private static final MozillaDownloader downloader = new MozillaDownloader();
    private Context context;
    private PriorityQueue<MozillaDownload> downloadQueue;
    private DownloadStatusListener downloadStatusListener;
    private AlarmManager alarmManager;
    
    private MozillaDownloader() {
        // private constructor to prevent external instantiation
    }

    private void setupDownloadStatusListener(DownloadStatusListener downloadStatusListener) {
        this.downloadStatusListener = downloadStatusListener;
    }

    private boolean flushQueueToDisk(PriorityQueue<MozillaDownload> queue) {
        /*
            need to store copies of scheduled downloads on disk
            because AlarmManager clears after turning off device
        */
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
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
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
        Intent downloadIntent = new Intent();
        downloadIntent.putExtra("MozillaDownload", download);
        // TODO: CHANGE IDENTIFIER OF PENDING INTENT TO UNIQUE INTEGER ID
        PendingIntent pendingIntent = PendingIntent.getService(context, download.getUid().hashCode(), downloadIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, download.getScheduledTime().getTime(), pendingIntent);
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
