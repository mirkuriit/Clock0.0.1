package com.terabyte.clock001;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class AlarmForegroundService extends Service {
    public AlarmForegroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        class SleepTask extends AsyncTask<Long, Void, Void> {
            public SleepTask() {

            }

            @Override
            protected Void doInBackground(Long... longs) {
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
            }
        }
        return START_NOT_STICKY;
    }
}