package com.kanishk.mozilladownloader;

import android.content.Context;

import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;

public class FetchUtil {
    public static Fetch getDownloader(Context context) {
        FetchConfiguration downloadConfig = new FetchConfiguration.Builder(context)
                .build();
        Fetch fetch = Fetch.Impl.getInstance(downloadConfig);
        return fetch;
    }
}