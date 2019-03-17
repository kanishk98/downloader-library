package com.kanishk.mozilladownloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.Map;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DownloadWorker extends Worker {

	private final String TAG = getClass().getSimpleName();

    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    private void download(MozillaDownload download, FileChannel downloadChannel, ReadableByteChannel readableByteChannel, long channelPosition, long totalBytes) throws IOException {
    	Log.d(TAG, "Downloading " + download.getUrl());
        long bytesDownloaded = downloadChannel.transferFrom(readableByteChannel, channelPosition, totalBytes);
        download.setDownloadedBytes(bytesDownloaded);
    }

    private void cancel(MozillaDownload download) throws IOException {
    	File destinationFile = new File(download.getTargetPath());
 		destinationFile.delete();
 		download.setStatus(DownloadStatus.CANCELLED);
    }

    private void pause(MozillaDownload download, Context context) {
    	download.setStatus(DownloadStatus.PAUSED);
        List<OneTimeWorkRequest> requests = download.getWorkRequestList();
        for (OneTimeWorkRequest request : requests) {
            WorkManager.getInstance().cancelWorkById(request.getId());
        }
        try {
            Util.saveDownload(download, context);
        } catch (IOException e) {
            Log.e(TAG, e.getStackTrace().toString());
        }
    }

    private void resume(MozillaDownload download) throws IOException {
        // TODO: Use Util.readDownload(download) to restart saved Workers
    }

    @NonNull
    @Override
    public Result doWork() {
        Map map = getInputData().getKeyValueMap();
    	long startingPos = (long) map.get(Constants.STARTING_POS_DOWNLOAD);
    	MozillaDownload download = (MozillaDownload) map.get(Constants.MOZILLA_DOWNLOAD);
    	Context context = (Context) map.get(Constants.CONTEXT);
    	DownloadStatus newStatus = (DownloadStatus) map.get(Constants.DOWNLOAD_STATUS);
    	try {
            File destinationFile = new File(download.getTargetPath());
            URL url = new URL(download.getUrl());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileChannel downloadChannel = new FileOutputStream(destinationFile, destinationFile.exists()).getChannel();
            if (newStatus == DownloadStatus.SCHEDULED) {
                download.setStatus(DownloadStatus.RUNNING);
                download(download, downloadChannel, readableByteChannel, startingPos, download.getChunkBytes());
            } else if (newStatus == DownloadStatus.CANCELLING) {
                cancel(download);
            }
            return Result.success();
        } catch (java.io.IOException e) {
            Log.e(TAG, e.getStackTrace().toString());
            pause(download, context);
            // TODO: Change download.status back to original value
            return Result.failure();
        }
    }
}
