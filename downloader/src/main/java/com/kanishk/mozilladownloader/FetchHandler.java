package com.kanishk.mozilladownloader;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.Request;

import java.util.Map;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FetchHandler extends Worker {

    public FetchHandler(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public boolean enqueueDownload(Fetch fetch, String url, String filepath, RequestParams requestParams) {
        final boolean[] res = new boolean[1];
        Request request = new Request(url, filepath);
        request = new RequestUtil().setRequest(request, requestParams);
        fetch.enqueue(request, updatedRequest -> {
            res[0] = true;
        }, error -> {
            res[0] = false;
        });
        // addListener(fetch);
        return res[0];
    }

    @NonNull
    @Override
    public Result doWork() {
        Map<String, Object> inputData = getInputData().getKeyValueMap();
        Fetch fetch = (Fetch) inputData.get(Constants.FETCH_INSTANCE);
        RequestParams requestParams = (RequestParams) inputData.get(Constants.REQUEST_PARAMS);
        String url = (String) inputData.get(Constants.DOWNLOAD_URL);
        String filepath = (String) inputData.get(Constants.DOWNLOAD_FILEPATH);
        if (enqueueDownload(fetch, url, filepath, requestParams)) {
            return Result.success();
        }
        return Result.failure();
    }
}
