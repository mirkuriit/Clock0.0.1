package com.terabyte.clock001;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.util.List;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingResult pendingResult = goAsync();
        DatabaseTask task = new DatabaseTask(context, pendingResult);
        task.execute(AlarmDatabaseClient.getInstance(context).getAppDatabase());
    }

    class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
        private Context context;
        private PendingResult pendingResult;

        public DatabaseTask(Context context, PendingResult pendingResult) {
            this.context = context;
            this.pendingResult = pendingResult;
        }

        @Override
        protected Void doInBackground(AlarmDatabase... alarmDatabases) {
            AlarmDatabase db = alarmDatabases[0];
            List<Alarm> enabledAlarmList = db.alarmDao().getAllAlarmsByEnabled(true);
            List<AlarmRepeating> alarmRepeatingList = db.alarmRepeatingDao().getAll();

            for(Alarm alarm : enabledAlarmList) {
                if(alarm.isRepeat) {
                    for(AlarmRepeating alarmRepeating : alarmRepeatingList) {
                        if(alarmRepeating.parentAlarmId == alarm.id) {
                            AlarmManagerLauncher.startTask(context, alarm.id, alarm.hour, alarm.minute, alarmRepeating.getArrayOfBooleanDays());
                            //we delete already-used alarmRepeating for optimization
                            alarmRepeatingList.remove(alarmRepeating);
                            break;
                        }
                    }

                }
                else {
                    AlarmManagerLauncher.startTask(context, alarm.id, alarm.hour, alarm.minute);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            pendingResult.finish();
        }
    }
}