package com.kanishk.mozilladownloader;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class DownloadService extends Service {

    private final String TAG = getClass().getSimpleName();

    private void download(MozillaDownload download) {
        // since execution time limit is 10 minutes, chain into granular downloads
        List<OneTimeWorkRequest> requests = new ArrayList<>();
        long chunks = (long) Math.ceil(download.getTotalBytes() / download.getChunkBytes());
        for (long i = 0; i < chunks; ++i) {
            @SuppressLint("RestrictedApi")
            Data data = new Data.Builder()
                    .put(Constants.MOZILLA_DOWNLOAD, download)
                    .putLong(Constants.STARTING_POS_DOWNLOAD, i)
                    .build();
            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(DownloadWorker.class)
                    .setInputData(data)
                    .addTag(download.getUid())
                    .build();
            requests.add(request);
        }
        // chains all of the downloads to happen at the same time
        WorkContinuation workContinuation = (WorkContinuation) WorkManager.getInstance().beginWith(requests).enqueue();
    }

    private void cancel(MozillaDownload download) {

    }

    private void pause(MozillaDownload download) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO: Investigate possible protocols that user may direct to
        // Current implementation supports FTP and HTTP only
        MozillaDownload download = (MozillaDownload) intent.getSerializableExtra(getString(R.string.mozilla_download));
        download.setTotalBytes(Util.findTotalBytes(download.getUrl()));
        if (download.getStatus() == DownloadStatus.SCHEDULED && download.getTotalBytes() != -1) {
            download(download);
        } else if (download.getStatus() == DownloadStatus.CANCELLING) {
            cancel(download);
        } else if (download.getStatus() == DownloadStatus.PAUSING) {
            pause(download);
        }
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
