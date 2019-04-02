package com.kanishk.mozilladownloader;

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
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
    public void isURLNull() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 0);
        MozillaDownloadBuilder builder = new MozillaDownloadBuilder()
                .setUrl(null)
                .setScheduledTime(calendar.getTime());
        download = builder.createMozillaDownload();
    }

    @Test(expected = NullPointerException.class)
    public void doesURLHaveExtension() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 0);
        MozillaDownloadBuilder builder = new MozillaDownloadBuilder()
                .setUrl("https://github")
                .setScheduledTime(calendar.getTime());
        download = builder.createMozillaDownload();
    }

    @Test(expected = NullPointerException.class)
    public void doesURLHaveProtocol() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 0);
        MozillaDownloadBuilder builder = new MozillaDownloadBuilder()
                .setUrl("https://github")
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

    @Test
    public void isCreatedIntentValid() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 0);
        MozillaDownloadBuilder builder = new MozillaDownloadBuilder()
                .setUrl("https://www.github.com")
                .setScheduledTime(calendar.getTime());
        download = builder.createMozillaDownload();
        Intent intent = MozillaDownloader.createIntent(context, download);
        assertEquals(download, (MozillaDownload) intent.getSerializableExtra("MozillaDownload"));
    }
}