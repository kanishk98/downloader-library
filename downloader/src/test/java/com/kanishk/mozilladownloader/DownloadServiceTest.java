package com.kanishk.mozilladownloader;

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DownloadServiceTest {

    private DownloadService mockDownloadService;
    private Context context;
    private Intent downloadIntent;
    private MozillaDownload download;
    private MozillaDownloader downloader;

    @Before
    public void setup() {
        mockDownloadService = Mockito.mock(DownloadService.class);
        context = Mockito.mock(Context.class);
        download = Util.constructDownload("http://placeholderTestUrl.com");
        downloader = MozillaDownloader.getDownloader(context);
    }

    @Test
    public void isDownloadStarted() {
        download.setStatus(DownloadStatus.SCHEDULED);
        downloadIntent = MozillaDownloader.createIntent(context, download);
        Mockito.doCallRealMethod().when(mockDownloadService).onHandleIntent(downloadIntent);
        mockDownloadService.onHandleIntent(downloadIntent);
        Mockito.verify(mockDownloadService).download(download);
    }

    @Test
    public void isDownloadPaused() {
        download.setStatus(DownloadStatus.PAUSING);
        downloadIntent = MozillaDownloader.createIntent(context, download);
        Mockito.doCallRealMethod().when(mockDownloadService).onHandleIntent(downloadIntent);
        mockDownloadService.onHandleIntent(downloadIntent);
        Mockito.verify(mockDownloadService).stopDownload(download);
    }

    @Test
    public void isDownloadCancelled() {
        download.setStatus(DownloadStatus.CANCELLING);
        downloadIntent = MozillaDownloader.createIntent(context, download);
        Mockito.doCallRealMethod().when(mockDownloadService).onHandleIntent(downloadIntent);
        mockDownloadService.onHandleIntent(downloadIntent);
        Mockito.verify(mockDownloadService).stopDownload(download);
    }
}