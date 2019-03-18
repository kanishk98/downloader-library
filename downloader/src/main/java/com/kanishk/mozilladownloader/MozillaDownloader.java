package com.kanishk.mozilladownloader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;

public class MozillaDownloader {

    private Context context;
    private DownloadStatusListener downloadStatusListener;
    private AlarmManager alarmManager;
    private final String TAG = getClass().getSimpleName();
    
    private MozillaDownloader(Context context) {
        // private constructor to prevent external instantiation
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private void setupDownloadStatusListener(DownloadStatusListener downloadStatusListener) {
        this.downloadStatusListener = downloadStatusListener;
    }

    public void scheduleDownload(final MozillaDownload download) {
        download.setStatus(DownloadStatus.SCHEDULED);
        // downloadStatusListener.onStatusChange(download);
        Intent downloadIntent = new Intent(context, DownloadService.class);
        downloadIntent.putExtra("MozillaDownload", download);
        // TODO: CHANGE IDENTIFIER OF PENDING INTENT TO UNIQUE INTEGER ID
        PendingIntent pendingIntent = PendingIntent.getService(context, download.getUid().hashCode(), downloadIntent, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, 0, pendingIntent);
    }

    public void pauseDownload(MozillaDownload download) {
        Log.d(TAG, "Pausing download");
        download.setStatus(DownloadStatus.PAUSING);
        Intent pauseIntent = new Intent(context, DownloadService.class);
        pauseIntent.putExtra("MozillaDownload", download);
        context.startService(pauseIntent);
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
        return new MozillaDownloader(context);
    }
}
