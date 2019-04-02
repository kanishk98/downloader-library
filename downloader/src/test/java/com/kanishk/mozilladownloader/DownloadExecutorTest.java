package com.kanishk.mozilladownloader;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
public class DownloadExecutorTest {

    private DownloadExecutor executor;
    private MozillaDownload download;
    private Context context;

    @Before
    public void setup() {
        context = Mockito.mock(Context.class);
        executor = Mockito.mock(DownloadExecutor.class);
        download = Util.constructDownload("https://github.com");
    }

    @Test
    public void areChunksDownloaded() throws IOException {
        Mockito.doCallRealMethod().when(executor).download(download, context);
        File destinationFile = new File(context.getExternalFilesDir("") + download.getUid() +
                download.getUrl().substring(download.getUrl().lastIndexOf(".")));
        Mockito.doReturn(1l).when(executor).getChunks(download.getDownloadedBytes(), destinationFile, download);
        executor.download(download, context);
        Mockito.verify(executor).getChunks(download.getDownloadedBytes(), destinationFile, download);
    }
}
