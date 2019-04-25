package com.kanishk.mozilladownloader;

import android.content.Context;
import android.util.Pair;

import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.Request;

import java.util.List;

public class FetchHandler {

    private static String generateDownloadTarget(String url, Context context) {
        return context.getExternalFilesDir("") + Util.generateUID() +
                url.substring(url.lastIndexOf("."));
    }

    public Fetch getDownloader(int concurrentDownloadLimit, Context context) {
        FetchConfiguration downloadConfig = new FetchConfiguration.Builder(context)
                .setDownloadConcurrentLimit(concurrentDownloadLimit)
                .build();
        Fetch fetch = Fetch.Impl.getInstance(downloadConfig);
        return fetch;
    }

    public boolean enqueueDownload(Fetch fetch, String url, Context context) {
        String filepath = generateDownloadTarget(url, context);
        return enqueueDownload(fetch, url, filepath, null, context);
    }

    public boolean enqueueDownload(Fetch fetch, String url, String filepath, Context context) {
        return enqueueDownload(fetch, url, filepath, null, context);
    }

    public boolean enqueueDownload(Fetch fetch, String url, RequestParams requestParams, Context context) {
        String filepath = generateDownloadTarget(url, context);
        return enqueueDownload(fetch, url, filepath, requestParams, context);
    }

    public boolean enqueueDownload(Fetch fetch, String url, String filepath, RequestParams requestParams, Context context) {
        final boolean[] res = new boolean[1];
        Request request = new Request(url, filepath);
        request = Util.setRequest(request, requestParams);
        fetch.enqueue(request, updatedRequest -> {
            res[0] = true;
        }, error -> {
            res[0] = false;
        });
        return res[0];
    }

    public void addListener(Fetch fetch) {
        // TODO: Attach listener and define appropriate behaviour
    }

}
