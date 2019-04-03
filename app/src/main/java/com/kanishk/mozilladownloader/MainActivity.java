package com.kanishk.mozilladownloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private MozillaDownload download;
    private Button startButton;
    private Button pauseButton;
    private Button cancelButton;
    private Button resumeButton;
    private SharedPreferences sharedPreferences;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().penaltyLog().detectAll().build());
        }*/
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.start_download);
        pauseButton = findViewById(R.id.pause_download);
        cancelButton = findViewById(R.id.cancel_download);
        resumeButton = findViewById(R.id.resume_download);
        final MozillaDownloader mozillaDownloader = MozillaDownloader.getDownloader(getApplicationContext());
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mozillaDownloader != null) {
                    download = new MozillaDownloadBuilder().setUrl(Constants.PDF_20MB).createMozillaDownload();
                    mozillaDownloader.scheduleDownload(download);
                }
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mozillaDownloader != null) {
                    mozillaDownloader.pauseDownload(download);
                }
            }
        });
        resumeButton.setOnClickListener(new View.OnClickListener() {
            // STARTS ONLY THE LAST PAUSED DOWNLOAD
            // CAN EASILY BE EXTENDED TO RESUME DOWNLOADS BY UID
            @Override
            public void onClick(View view) {
                if (mozillaDownloader != null) {
                    sharedPreferences = getSharedPreferences(Constants.SAVED_DOWNLOADS, Context.MODE_PRIVATE);
                    Map savedDownloads = sharedPreferences.getAll();
                    Iterator iterator = savedDownloads.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry pair = (Map.Entry) iterator.next();
                        String pausedDownloadJson = (String) pair.getValue();
                        Log.d(TAG, "" + pausedDownloadJson);
                        if (pausedDownloadJson != null) {
                            mozillaDownloader.resumeDownload(pausedDownloadJson);
                            sharedPreferences.edit().remove((String) pair.getKey()).commit();
                            break;
                        }
                        iterator.remove();
                    }
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
