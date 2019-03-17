package com.kanishk.mozilladownloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MozillaDownloader mozillaDownloader = MozillaDownloader.getDownloader(getApplicationContext());
        if (mozillaDownloader != null) {
            MozillaDownload download = new MozillaDownload();
            download.setUid(Util.generateUID());
            download.setUrl("https://www.google.com");
            Calendar calendar = Calendar.getInstance();
            calendar.set(2019, 3, 17, 20, 41);
            download.setScheduledTime(calendar.getTime());
            mozillaDownloader.scheduleDownload(download);
        }
    }
}
