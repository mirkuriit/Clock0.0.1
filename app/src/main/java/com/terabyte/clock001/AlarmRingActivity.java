package com.terabyte.clock001;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AlarmRingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);
        long alarmId = getIntent().getExtras().getLong(Const.INTENT_KEY_ALARM_ID);

        TextView textAlarmRingTime = findViewById(R.id.textAlarmRingTime);
        TextView textAlarmRingDescription = findViewById(R.id.textAlarmRingDescription);

        AlarmDatabaseManager.getAlarmById(AlarmDatabaseClient.getInstance(getApplicationContext()).getAppDatabase(), alarmId, new PostExecuteCode() {
            @Override
            public void doInPostExecuteWhenWeGotAlarm(Alarm alarm) {
                textAlarmRingTime.setText(String.format("%02d:%02d", alarm.hour, alarm.minute));
                textAlarmRingDescription.setText(alarm.description);

            }
        });
    }
}