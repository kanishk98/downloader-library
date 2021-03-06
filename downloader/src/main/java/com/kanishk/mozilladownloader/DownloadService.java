package com.kanishk.mozilladownloader;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class DownloadService extends IntentService {

    private final String TAG = getClass().getSimpleName();

    public DownloadService() {
        super("DownloadService");
    }

    public void download(MozillaDownload download) {
        Intent intent = new Intent(getApplicationContext(), DownloadExecutor.class);
        intent.putExtra(Constants.MOZILLA_DOWNLOAD, download);
        getApplicationContext().startService(intent);
    }

    public void stopDownload(MozillaDownload download) {
        Intent intent = new Intent(Constants.STOP_DOWNLOAD);
        intent.putExtra(Constants.MOZILLA_DOWNLOAD, download);
        getApplicationContext().sendBroadcast(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // TODO: Investigate possible protocols that user may direct to
        // Current implementation supports FTP and HTTP only
        Context context = (Context) intent.getSerializableExtra(Constants.CONTEXT);
        MozillaDownload download = (MozillaDownload) intent.getSerializableExtra("MozillaDownload");
        if (download.getStatus() == DownloadStatus.SCHEDULED) {
            download(download);
        } else if (download.getStatus() == DownloadStatus.CANCELLING || download.getStatus() == DownloadStatus.PAUSING) {
            stopDownload(download);
        }
    }
}
