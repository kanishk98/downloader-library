package com.kanishk.mozilladownloader;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MozillaDownloader {

    private void enqueueWorkRequest(Date date, String url, String filepath, RequestParams requestParams) {
        WorkManager workManager = WorkManager.getInstance();
        OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(FetchHandler.class)
                .setInputData(createWorkInputData(url, filepath, requestParams))
                .setInitialDelay(date.getTime() - Calendar.getInstance().getTimeInMillis(), TimeUnit.MILLISECONDS)
                .build();
        workManager.enqueue(downloadRequest);
    }

    @SuppressLint("RestrictedApi")
    private Data createWorkInputData(String url, String filepath, RequestParams requestParams) {
        Data.Builder builder = new Data.Builder();
        builder.putString(Constants.DOWNLOAD_URL, url);
        builder.putString(Constants.DOWNLOAD_FILEPATH, filepath);
        builder.putString(Constants.REQUEST_PARAMS, requestParams.getJson());
        return builder.build();
    }

    private String generateDownloadTarget(String url, Context context) {
        if (new RequestUtil().isValidURL(url)) {
            return context.getExternalFilesDir("") + new RequestUtil().generateUID() +
                    url.substring(url.lastIndexOf("."));
        }
        return null;
    }

    public void scheduleDownload(Date date, String url, Context context) {
        String filepath = this.generateDownloadTarget(url, context);
        // TODO: Figure out better ways of getting max concurrent limit
        // TODO: Expose maxConcurrentLimit to developer
        RequestParams requestParams = new RequestParams();
        this.enqueueWorkRequest(date, url, filepath, requestParams);
    }

    public void scheduleDownload(Date date, String url, String filepath, Context context) {
        RequestParams requestParams = new RequestParams();
        this.enqueueWorkRequest(date, url, filepath, requestParams);
    }

    public void scheduleDownload(Date date, String url, String filepath, RequestParams requestParams, Context context) {
        this.enqueueWorkRequest(date, url, filepath, requestParams);
    }

    public void scheduleDownload(Date date, String url, RequestParams requestParams, Context context) {
        String filepath = this.generateDownloadTarget(url, context);
        this.enqueueWorkRequest(date, url, filepath, requestParams);
    }
}
