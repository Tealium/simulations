package com.tealium.concurrentmodification;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity implements Runnable {

    private static final String KEY_YEAR = "year";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DAY = "day";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_SECOND = "second";

    private Map<String, Integer> dateTimeInfo;
    private TextView label;
    private Handler handler;
    private boolean isStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.label = (TextView) this.findViewById(R.id.main_label);

        this.dateTimeInfo = new HashMap<>(6);
        this.startUpdateThread();

        this.handler = new Handler();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.isStarted = true;
        this.run();
    }

    @Override
    protected void onStop() {
        this.isStarted = false;
        super.onStop();
    }

    @Override
    public void run() {

        String summary = "";

        for (Map.Entry<String, Integer> entry : this.dateTimeInfo.entrySet()) {
            summary += entry.getKey() + "=" + entry.getValue() + " ";
        }

        label.setText(summary);

        if (this.isStarted) {
            this.handler.postDelayed(this, 10);
        }
    }

    private void startUpdateThread() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        Calendar calendar = Calendar.getInstance();
                        dateTimeInfo.remove(KEY_YEAR);
                        dateTimeInfo.put(KEY_YEAR, calendar.get(Calendar.YEAR));

                        dateTimeInfo.remove(KEY_MONTH);
                        dateTimeInfo.put(KEY_MONTH, calendar.get(Calendar.MONTH));

                        dateTimeInfo.remove(KEY_DAY);
                        dateTimeInfo.put(KEY_DAY, calendar.get(Calendar.DAY_OF_MONTH));

                        dateTimeInfo.remove(KEY_HOUR);
                        dateTimeInfo.put(KEY_HOUR, calendar.get(Calendar.HOUR_OF_DAY));

                        dateTimeInfo.remove(KEY_MINUTE);
                        dateTimeInfo.put(KEY_MINUTE, calendar.get(Calendar.MINUTE));

                        dateTimeInfo.remove(KEY_SECOND);
                        dateTimeInfo.put(KEY_SECOND, calendar.get(Calendar.SECOND));
                    }
                }, 0, 10, TimeUnit.MILLISECONDS);
    }
}
