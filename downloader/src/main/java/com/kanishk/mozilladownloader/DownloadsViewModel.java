package com.kanishk.mozilladownloader;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2core.Func;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DownloadsViewModel extends AndroidViewModel {

    private MutableLiveData<List<Download>> downloadLiveData;

    public DownloadsViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<List<Download>> initDownloads() {
        final MutableLiveData<List<Download>> liveData = new MutableLiveData<>();
        Fetch fetch = FetchUtil.getDownloader(getApplication().getApplicationContext());
        fetch.getDownloads(new Func<List<Download>>() {
            @Override
            public void call(@NotNull List<Download> result) {
                liveData.postValue(result);
            }
        });
        return liveData;
    }

    public LiveData<List<Download>> getDownloads() {
        if (downloadLiveData == null) {
            downloadLiveData = initDownloads();
        }
        return downloadLiveData;
    }

    public void addDownload(Download download) {
        if (downloadLiveData == null) {
            downloadLiveData = initDownloads();
        }
        downloadLiveData.getValue().add(download);
    }

    public void updateDownload(Download targetDownload) {
        if (downloadLiveData == null) {
            downloadLiveData = initDownloads();
        }
        List<Download> downloads = downloadLiveData.getValue();
        for (int i = 0; i < downloads.size(); ++i) {
            Download download = downloads.get(i);
            if (download.getFile().equals(targetDownload.getFile())) {
                downloads.remove(i);
                downloads.add(i, targetDownload);
                return;
            }
        }
    }

    public void removeDownload(Download targetDownload) {
        if (downloadLiveData == null) {
            downloadLiveData = initDownloads();
        }
        List<Download> downloads = downloadLiveData.getValue();
        for (int i = 0; i < downloads.size(); ++i) {
            Download download = downloads.get(i);
            if (download.getFile().equals(targetDownload.getFile())) {
                downloads.remove(i);
                return;
            }
        }
    }
}
