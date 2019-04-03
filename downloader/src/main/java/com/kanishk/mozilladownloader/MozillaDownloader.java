package com.kanishk.mozilladownloader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

public class MozillaDownloader {

    private Context context;
    private AlarmManager alarmManager;
    private final String TAG = getClass().getSimpleName();
    
    private MozillaDownloader(Context context) {
        // private constructor to prevent external instantiation
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public Context getContext() {
        return context;
    }

    public static Intent createIntent(Context context, MozillaDownload download) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra("MozillaDownload", download);
        return intent;
    }

    public void scheduleDownload(final MozillaDownload download) {
        download.setStatus(DownloadStatus.SCHEDULED);
        Log.d(TAG, "Scheduling download");
        Intent downloadIntent = createIntent(context, download);
        // TODO: CHANGE IDENTIFIER OF PENDING INTENT TO UNIQUE INTEGER ID
        PendingIntent pendingIntent = PendingIntent.getService(context, download.getUid().hashCode(), downloadIntent, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, 0, pendingIntent);
    }

    public void pauseDownload(MozillaDownload download) {
        Log.d(TAG, "Pausing download");
        download.setStatus(DownloadStatus.PAUSING);
        Intent pauseIntent = createIntent(context, download);
        context.startService(pauseIntent);
    }

    public boolean cancelDownload(MozillaDownload download) {
        // intents cannot be distinguished based on their extras
        download.setStatus(DownloadStatus.CANCELLING);
        Intent cancelIntent = createIntent(context, download);
        context.startService(cancelIntent);
        alarmManager.cancel(PendingIntent.getService(context, download.getUid().hashCode(),
                new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
        boolean alarmSet = PendingIntent.getService(context, download.getUid().hashCode(), cancelIntent, PendingIntent.FLAG_NO_CREATE) != null;
        return alarmSet;
    }

    public static MozillaDownloader getDownloader(Context context) {
        return new MozillaDownloader(context);
    }

    public void resumeDownload(String pausedDownloadJson) {
        MozillaDownload download = new Gson().fromJson(pausedDownloadJson, MozillaDownload.class);
        // paused download will have scheduled time before current time
        // AlarmManager will trigger alarm automatically
        Log.d(TAG, "DOWNLOADED BYTES ARE: " + download.getDownloadedBytes());
        scheduleDownload(download);
    }
}
