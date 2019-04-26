package com.kanishk.mozilladownloader;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button pauseButton;
    private Button cancelButton;
    private Button resumeButton;
    private EditText editText;
    private TimePicker timePicker;
    private DatePicker datePicker;

    private String TAG = getClass().getSimpleName();

    private Date getChosenDate() {
        int hour, minute, day, month, year;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            datePicker.getDayOfMonth();

            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        if (hour < 0 || minute < 0) {
            return null;
        }

        return calendar.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.url_field);
        timePicker = (TimePicker) findViewById(R.id.time_picker);
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        startButton = (Button) findViewById(R.id.start_download);
        pauseButton = (Button) findViewById(R.id.pause_download);
        cancelButton = (Button) findViewById(R.id.cancel_download);
        resumeButton = (Button) findViewById(R.id.resume_download);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = editText.getText().toString();
                Date chosenDate = getChosenDate();
                new MozillaDownloader().scheduleDownload(chosenDate, url, MainActivity.this);
            }
        });

        // TODO: Define pause/resume/cancel Buttons based on list of in-progress downloads;
    }
}
