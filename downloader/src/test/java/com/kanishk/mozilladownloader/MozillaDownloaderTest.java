package com.kanishk.mozilladownloader;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Calendar;

import static org.junit.Assert.assertNotNull;

public class MozillaDownloaderTest {

    private MozillaDownloader downloader;
    private MozillaDownload download;
    private Context context;

    @Before
    public void setup() {
        context = Mockito.mock(Context.class);
    }

    @Test
    public void isDownloaderNull() {
        downloader = MozillaDownloader.getDownloader(context);
        assertNotNull(downloader);
    }

    @Test
    public void isDownloadNull() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 0);
        MozillaDownloadBuilder builder = new MozillaDownloadBuilder()
                .setUrl("https://www.github.com")
                .setScheduledTime(calendar.getTime());
        download = builder.createMozillaDownload();
        assertNotNull(download);
    }

    @Test(expected = NullPointerException.class)
    public void isURLValid() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 0);
        MozillaDownloadBuilder builder = new MozillaDownloadBuilder()
                .setUrl(null)
                .setScheduledTime(calendar.getTime());
        download = builder.createMozillaDownload();
    }

    @Test(expected = NullPointerException.class)
    public void isDateNull() {
        MozillaDownloadBuilder builder = new MozillaDownloadBuilder()
                .setUrl("https://www.github.com")
                .setScheduledTime(null);
        download = builder.createMozillaDownload();
    }
}