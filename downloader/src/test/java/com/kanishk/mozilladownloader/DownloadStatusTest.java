package com.kanishk.mozilladownloader;

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;


@FixMethodOrder(MethodSorters.DEFAULT)
public class DownloadStatusTest {

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
        downloadIntent = Mockito.mock(Intent.class);
    }

    @Test
    public void isServiceRun() {
        mockDownloadService.onHandleIntent(downloadIntent);
        Mockito.verify(mockDownloadService, Mockito.times(1)).onHandleIntent(downloadIntent);
    }
}