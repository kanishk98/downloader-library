package com.kanishk.mozilladownloader;

import android.app.ActivityManager;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MozillaDownloaderTest {

    private MozillaDownloader downloader;
    private MozillaDownload download;

    private final String TAG = getClass().getSimpleName();

    @Before
    public void setup() {
        downloader = MozillaDownloader.getDownloader(InstrumentationRegistry.getContext());
        assertNotNull("Downloader object was null", downloader);
        download = Util.constructDownload("https://media.readthedocs.org/pdf/zulip/1.5.0/zulip.pdf");
    }

    @Test
    public void setDownloadAlarm() throws Exception {
        boolean alarmSet = downloader.scheduleDownload(download);
        assertEquals(true, alarmSet);
    }

    @Test
    public void cancelAlarm() throws Exception {
        boolean cancelAlarm = downloader.cancelDownload(download);
        assertEquals(false, cancelAlarm);
    }
}