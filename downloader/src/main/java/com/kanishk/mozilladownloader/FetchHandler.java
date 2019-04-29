package com.kanishk.mozilladownloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.Func;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import kotlin.Pair;

public class FetchHandler extends Worker {

    private final String TAG = getClass().getSimpleName();

    public FetchHandler(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public void enqueueDownload(Fetch fetch, String url, String filepath, RequestParams requestParams) {
        List<Request> requests = new ArrayList<>();
        requests.add(new Request(url, filepath));
        fetch.enqueue(requests, new Func<List<Pair<Request, Error>>>() {
            @Override
            public void call(@NotNull List<Pair<Request, Error>> result) {
                for (Pair<Request, Error> pair : result) {
                    if (pair.component2() != null) {
                        // TODO: Convert to real error-handling method
                        Log.e(TAG, String.valueOf(pair.component2().getHttpResponse()));
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public Result doWork() {
        Fetch fetch = FetchUtil.getDownloader(getApplicationContext());
        RequestParams requestParams = new RequestParams().fromJson(getInputData().getString(Constants.REQUEST_PARAMS));
        String url = getInputData().getString(Constants.DOWNLOAD_URL);
        String filepath = getInputData().getString(Constants.DOWNLOAD_FILEPATH);
        enqueueDownload(fetch, url, filepath, requestParams);
        // status below only indicates that enqueueDownload() was called
        return Result.success();
    }
}
