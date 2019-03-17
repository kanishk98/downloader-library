package com.kanishk.mozilladownloader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;

public class MozillaDownloader {
    
    private static final MozillaDownloader downloader = new MozillaDownloader();
    private Context context;
    private DownloadStatusListener downloadStatusListener;
    private AlarmManager alarmManager;
    
    private MozillaDownloader() {
        // private constructor to prevent external instantiation
    }

    private void setupDownloadStatusListener(DownloadStatusListener downloadStatusListener) {
        this.downloadStatusListener = downloadStatusListener;
    }

    // TODO: What if app accidentally calls initDownloader() with a different context?
    public void initDownloader(Context context) throws IOException {
        // must call initDownloader() before using any other function from this class
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void scheduleDownload(MozillaDownload download) {
        download.setStatus(DownloadStatus.SCHEDULED);
        downloadStatusListener.onStatusChange(download);
        Intent downloadIntent = new Intent();
        downloadIntent.putExtra("MozillaDownload", download);
        // TODO: CHANGE IDENTIFIER OF PENDING INTENT TO UNIQUE INTEGER ID
        PendingIntent pendingIntent = PendingIntent.getService(context, download.getUid().hashCode(), downloadIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, download.getScheduledTime().getTime(), pendingIntent);
    }

    public void pauseDownload(MozillaDownload download) {
        if (download.getStatus() == DownloadStatus.RUNNING) {
            download.setStatus(DownloadStatus.PAUSING);
            Intent pauseIntent = new Intent();
            pauseIntent.putExtra("MozillaDownload", download);
            context.startService(pauseIntent);
        }
    }

    public void cancelDownload(MozillaDownload download) {
        // intents cannot be distinguished based on their extras
        if (download.getStatus() == DownloadStatus.RUNNING) {
            download.setStatus(DownloadStatus.CANCELLING);
            Intent cancelIntent = new Intent();
            cancelIntent.putExtra("MozillaDownload", download);
            context.startService(cancelIntent);
        } else {
            alarmManager.cancel(PendingIntent.getService(context, download.getUid().hashCode(),
                    new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }

    public void rescheduleDownload(MozillaDownload download, Date newTime) {
        // download.getScheduledTime() != newTime
        if (download.getStatus() == DownloadStatus.RUNNING) {
            pauseDownload(download);
        }
        download.setScheduledTime(newTime);
        scheduleDownload(download);
    }

    public static MozillaDownloader getDownloader(Context context) {
        try {
            new MozillaDownloader().initDownloader(context);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return downloader;
    }
}
