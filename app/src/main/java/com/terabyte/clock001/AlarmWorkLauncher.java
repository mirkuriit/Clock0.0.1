package com.terabyte.clock001;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.UUID;

/**
 * It's manager class like AlarmDatabaseManager class. But I named it "Launcher" because we've already got "manager" word in WorkManager class
 * and it would be strange if we were two manager classes in one part of app.
 *
 * So, it's also to abstract working with WorkManager in one class with static methods.
 *
 * Here in method startAlarmWorker we start AlarmWorker instance with tag that equals alarmId. Tag is used by us to stop this AlarmWorker instance.
 * Actually, in WorkManager we have UUID id field but I don't understand how to save the UUID object in memory of phone: we can't keep it in Room Database or SharedPreferences.
 * To keep UUID objects in Room db I tried to extend UUID class and did it @Entity in Room, but (I wish) UUID class is final...
 *
 * So, without workRequest.getId() I use Tag. Tag always equals alarmId in my program.
 * I've already wrote comment about special listener that signal to us that AlarmWorker instance is finished.
 * So, this listener is here and we can set it by Observer<WorkInfo>. It's also built-in class of big Google WorkManager system. Observer is like View.OnCheckedChangedListener
 * and into Observer we have method onChanged(). This method runs when state of Worker is changed and in this method we can check: is our AlarmWorker instance finished.
 */

public class AlarmWorkLauncher {
    public static void startAlarmWorker(Context context, LifecycleOwner owner, long alarmId, int hour, int minute, Observer<WorkInfo> observer) {
        long mills = DateManager.getMillsForAlarm(hour, minute);

        Data data = new Data.Builder()
                .putLong(Const.DATA_KEY_MILLS, mills)
                .build();

        WorkRequest workRequest = new OneTimeWorkRequest.Builder(AlarmWorker.class)
                .setInputData(data)
                .addTag(String.valueOf(alarmId))
                .build();

        WorkManager.getInstance(context).enqueue(workRequest);

        WorkManager.getInstance(context).getWorkInfoByIdLiveData(workRequest.getId()).observe(owner, observer);
    }

    public static void startAlarmWorker(Context context, LifecycleOwner owner, long alarmId, int hour, int minute, boolean[] days, Observer<WorkInfo> observer) {
        long mills = DateManager.getMillsForAlarm(hour, minute, days);

        Data data = new Data.Builder()
                .putLong(Const.DATA_KEY_MILLS, mills)
                .build();

        WorkRequest workRequest = new OneTimeWorkRequest.Builder(AlarmWorker.class)
                .setInputData(data)
                .addTag(String.valueOf(alarmId))
                .build();

        WorkManager.getInstance(context).enqueue(workRequest);

        WorkManager.getInstance(context).getWorkInfoByIdLiveData(workRequest.getId()).observe(owner, observer);
    }

    public static void stopAlarmWorker(Context context, String tag) {
        WorkManager.getInstance(context).cancelAllWorkByTag(tag);
    }

    public static Observer<WorkInfo> getAlarmWorkerObserver(Context context, long alarmId) {
        return new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if(workInfo.getState().isFinished() & !workInfo.getState().equals(WorkInfo.State.CANCELLED)) {
                    Intent intent = new Intent(context, AlarmRingActivity.class);
                    intent.putExtra(Const.INTENT_KEY_ALARM_ID, alarmId);
                    context.startActivity(intent);
                }
            }
        };
    }
}
