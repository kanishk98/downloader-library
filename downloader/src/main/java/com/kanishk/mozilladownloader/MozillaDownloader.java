package com.kanishk.mozilladownloader;

import android.annotation.SuppressLint;
import android.content.Context;

import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MozillaDownloader {

    public Fetch getDownloader(Context context) {
        FetchConfiguration downloadConfig = new FetchConfiguration.Builder(context)
                .build();
        Fetch fetch = Fetch.Impl.getInstance(downloadConfig);
        return fetch;
    }

    private void enqueueWorkRequest(Date date, Fetch fetch, String url, String filepath, RequestParams requestParams) {
        WorkManager workManager = WorkManager.getInstance();
        OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(FetchHandler.class)
                .setInputData(createWorkInputData(fetch, url, filepath, requestParams))
                .setInitialDelay(date.getTime() - Calendar.getInstance().getTimeInMillis(), TimeUnit.MILLISECONDS)
                .build();
        workManager.enqueue(downloadRequest);
    }

    @SuppressLint("RestrictedApi")
    private Data createWorkInputData(Fetch fetch, String url, String filepath, RequestParams requestParams) {
        Data.Builder builder = new Data.Builder();
        builder.put(Constants.FETCH_INSTANCE, fetch);
        builder.putString(Constants.DOWNLOAD_URL, url);
        builder.putString(Constants.DOWNLOAD_FILEPATH, filepath);
        builder.put(Constants.REQUEST_PARAMS, requestParams);
        return builder.build();
    }

    private String generateDownloadTarget(String url, Context context) {
        return context.getExternalFilesDir("") + new RequestUtil().generateUID() +
                url.substring(url.lastIndexOf("."));
    }

    public void scheduleDownload(Date date, String url, Context context) {
        String filepath = this.generateDownloadTarget(url, context);
        // TODO: Figure out better ways of getting max concurrent limit
        // TODO: Expose maxConcurrentLimit to developer
        Fetch fetch = this.getDownloader(context);
        RequestParams requestParams = new RequestParams();
        this.enqueueWorkRequest(date, fetch, url, filepath, requestParams);
    }

    public void scheduleDownload(Date date, String url, String filepath, Context context) {
        Fetch fetch = this.getDownloader(context);
        RequestParams requestParams = new RequestParams();
        this.enqueueWorkRequest(date, fetch, url, filepath, requestParams);
    }

    public void scheduleDownload(Date date, String url, String filepath, RequestParams requestParams, Context context) {
        Fetch fetch = this.getDownloader(context);
        this.enqueueWorkRequest(date, fetch, url, filepath, requestParams);
    }

    public void scheduleDownload(Date date, String url, RequestParams requestParams, Context context) {
        String filepath = this.generateDownloadTarget(url, context);
        Fetch fetch = this.getDownloader(context);
        this.enqueueWorkRequest(date, fetch, url, filepath, requestParams);
    }
}
