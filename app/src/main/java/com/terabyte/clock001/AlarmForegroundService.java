package com.terabyte.clock001;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmForegroundService extends Service {
    public AlarmForegroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}