package com.kanishk.mozilladownloader;

import android.content.Context;
import android.util.Pair;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;

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
        addListener(fetch);
        return res[0];
    }

    public void addListener(Fetch fetch) {
        fetch.addListener(new FetchListener() {
            @Override
            public void onAdded(Download download) {

            }

            @Override
            public void onQueued(Download download, boolean b) {

            }

            @Override
            public void onWaitingNetwork(Download download) {

            }

            @Override
            public void onCompleted(Download download) {

            }

            @Override
            public void onError(Download download, Error error, Throwable throwable) {

            }

            @Override
            public void onDownloadBlockUpdated(Download download, DownloadBlock downloadBlock, int i) {

            }

            @Override
            public void onStarted(Download download, List<? extends DownloadBlock> list, int i) {

            }

            @Override
            public void onProgress(Download download, long l, long l1) {

            }

            @Override
            public void onPaused(Download download) {

            }

            @Override
            public void onResumed(Download download) {

            }

            @Override
            public void onCancelled(Download download) {

            }

            @Override
            public void onRemoved(Download download) {

            }

            @Override
            public void onDeleted(Download download) {

            }
        });
    }

}
