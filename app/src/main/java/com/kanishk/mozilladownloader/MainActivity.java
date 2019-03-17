package com.kanishk.mozilladownloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MozillaDownloader mozillaDownloader = MozillaDownloader.getDownloader(getApplicationContext());
    }
}
