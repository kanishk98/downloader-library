package com.kanishk.mozilladownloader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button pauseButton;
    private Button cancelButton;
    private Button resumeButton;
    private EditText editText;

    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.url_field);
        startButton = (Button) findViewById(R.id.start_download);
        pauseButton = (Button) findViewById(R.id.pause_download);
        cancelButton = (Button) findViewById(R.id.cancel_download);
        resumeButton = (Button) findViewById(R.id.resume_download);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = editText.getText().toString();
                if (url != null) {

                }
            }
        });
    }
}
