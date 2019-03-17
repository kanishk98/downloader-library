package com.kanishk.mozilladownloader;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;

public class DownloadService extends IntentService {

    private final String TAG = getClass().getSimpleName();

    public DownloadService() {
        super("DownloadService");
    }

    private void download(MozillaDownload download) {
        Log.d(TAG, "Downloading " + download.getUrl());
        // since execution time limit is 10 minutes, chain into granular downloads
        List<OneTimeWorkRequest> requests = new ArrayList<>();
        long chunks = (long) Math.ceil(download.getTotalBytes() / download.getChunkBytes());
        WorkContinuation workContinuation = null;
        for (long i = 0; i < chunks; ++i) {
            Map map = new HashMap<>();
            map.put(Constants.MOZILLA_DOWNLOAD, Util.jsonifyDownload(download));
            map.put(Constants.DOWNLOAD_STATUS, DownloadStatus.SCHEDULED);
            map.put(Constants.STARTING_POS_DOWNLOAD, i);
            Data data = new Data.Builder()
                    .putAll(map)
                    .build();
            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(DownloadWorker.class)
                    .setInputData(data)
                    .addTag(download.getUid())
                    .build();
            if (i == 0) {
                workContinuation = WorkManager.getInstance().beginWith(request);
            } else {
                workContinuation.then(request);
            }
            requests.add(request);
        }
        workContinuation.enqueue();
        download.setWorkRequestList(requests);
        // chains all of the downloads to happen in parallel
        WorkManager.getInstance().beginWith(requests).enqueue();
    }

    private void cancel(MozillaDownload download, Context context) {
        List<OneTimeWorkRequest> requests = download.getWorkRequestList();
        for (OneTimeWorkRequest request : requests) {
            WorkManager.getInstance().cancelWorkById(request.getId());
        }
        Map map = new HashMap<>();
        map.put(Constants.MOZILLA_DOWNLOAD, download);
        map.put(Constants.DOWNLOAD_STATUS, DownloadStatus.CANCELLING);
        map.put(Constants.CONTEXT, context);
        Data data = new Data.Builder()
                    .putAll(map)
                    .build();
        OneTimeWorkRequest cancellationRequest = new OneTimeWorkRequest.Builder(DownloadWorker.class)
                    .setInputData(data)
                    .addTag(download.getUid())
                    .build();
        WorkManager.getInstance().enqueue(cancellationRequest);
    }

    private void pause(MozillaDownload download, Context context) {
        List<OneTimeWorkRequest> requests = download.getWorkRequestList();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // TODO: Investigate possible protocols that user may direct to
        // Current implementation supports FTP and HTTP only
        Log.d(TAG, "Alarm invoked, running service");
        Context context = (Context) intent.getSerializableExtra(Constants.CONTEXT);
        MozillaDownload download = (MozillaDownload) intent.getSerializableExtra(getString(R.string.mozilla_download));
        long downloadSize = Util.findTotalBytes(download.getUrl());
        Log.d(TAG, String.valueOf(downloadSize));
        download.setTotalBytes(downloadSize);

        if (download.getStatus() == DownloadStatus.SCHEDULED && download.getTotalBytes() != -1) {
            download(download);
        } else if (download.getStatus() == DownloadStatus.CANCELLING) {
            cancel(download, context);
        } else if (download.getStatus() == DownloadStatus.PAUSING) {
            pause(download, context);
        }
    }
}
