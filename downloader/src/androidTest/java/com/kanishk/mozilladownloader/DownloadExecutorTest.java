package com.kanishk.mozilladownloader;

import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DownloadExecutorTest {

    private MozillaDownloader downloader;
    private MozillaDownload download;

    private final String TAG = getClass().getSimpleName();

    private void constructDownload(String url) {
        download = new MozillaDownload();
        download.setUid(Util.generateUID());
        download.setUrl(url);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 0, 0, 0);
        download.setScheduledTime(calendar.getTime());
    }

    @Before
    public void setup() {
        downloader = MozillaDownloader.getDownloader(InstrumentationRegistry.getContext());
        assertNotNull("Downloader object was null", downloader);
        constructDownload("https://media.readthedocs.org/pdf/zulip/1.5.0/zulip.pdf");
    }

    @Test
    public void setDownloadAlarm() throws Exception {
        boolean alarmSet = downloader.scheduleDownload(download);
        assertEquals(true, alarmSet);
    }
}