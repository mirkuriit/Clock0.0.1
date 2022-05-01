package com.terabyte.clock001;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * I wanted to create alarm ringing with ForegroundService or IntentService, but I needed to use a stuff that
 * contains the functionality of this 2 classes together. So, the main problem of ForegroundService - there is only one instance
 * of ForegroundService for all alarms. But user can turn on many alarms! That problem can solve IntentService but it can't work
 * when application is stopped...
 * <p>
 * So, after some hours I decided to use WorkManager engine.
 * This class extends Worker is like our task that we need compile in another thread.
 * This class has doWork() method. In this method we have another thread and here we just wait some time.
 * And after waiting we just return success! We don't care, what Result.success() means, we realize about Worker finishing in special listener.
 * This listener's name is WorkManager.getInstance(context).getWorkInfoByIdLiveData(..) and its location is AlarmWorkLauncher class
 * And finally in this listener (not here 'cause there is another thread) we start AlarmRingActivity!
 */

public class AlarmWorker extends Worker {
    public AlarmWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        long mills = getInputData().getLong(Const.DATA_KEY_MILLS, -1);
        if (mills == -1) {
            return Result.failure();
        }
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Result.success();
    }
}
