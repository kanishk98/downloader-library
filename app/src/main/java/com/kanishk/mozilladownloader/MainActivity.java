package com.kanishk.mozilladownloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private MozillaDownload download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button pauseButton = findViewById(R.id.pause_download);
        Button cancelButton = findViewById(R.id.cancel_download);
        final MozillaDownloader mozillaDownloader = MozillaDownloader.getDownloader(getApplicationContext());
        if (mozillaDownloader != null) {
            download = new MozillaDownload();
            download.setUid(Util.generateUID());
            download.setUrl("https://download.sublimetext.com/Sublime%20Text%20Build%203200%20x64%20Setup.exe");
            Calendar calendar = Calendar.getInstance();
            calendar.set(2019, 3, 17, 20, 41);
            download.setScheduledTime(calendar.getTime());
            mozillaDownloader.scheduleDownload(download);
        }
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mozillaDownloader != null) {
                    mozillaDownloader.pauseDownload(download);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mozillaDownloader != null) {
                    mozillaDownloader.cancelDownload(download);
                }
            }
        });
    }
}
