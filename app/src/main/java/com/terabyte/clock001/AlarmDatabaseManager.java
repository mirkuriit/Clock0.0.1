package com.terabyte.clock001;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * I created this class to easily work with database: delete, update and create objects in Room Database
 * Actually, I've just realize methods of Dao interface.
 * Without that class I would write many AsyncTask classes everywhere. I would be very stupid
 * We can't work with Room Database in main UI thread. So, we work with it in AsyncTask
 * <p>
 * Also, some methods have Interface PostExecuteCode in params. I created this interface to work with our
 * data from Room Database in main thread again (interface methods work in onPostExecute() method of AsyncTask)
 * It's very useful to work with database data in main thread after getting it in AsyncTask thread
 * Obviously, I've just solved architecture problem
 */

public class AlarmDatabaseManager {
    private static ArrayList<Alarm> alarmList = null;

    private static ArrayList<AlarmRepeating> alarmRepeatingList = null;

    private static ArrayList<AlarmPuzzle> alarmPuzzleList = null;


    public static void createAlarm(Context context, Alarm alarm, PostExecuteCode code) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Long> {
            @Override
            protected Long doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                return db.alarmDao().insert(alarm);
            }

            @Override
            protected void onPostExecute(Long createdAlarmId) {
                code.doInPostExecuteWhenWeGotIdOfCreatedAlarm(createdAlarmId);
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(AlarmDatabaseClient.getInstance(context).getAppDatabase());
    }

    public static void updateAlarm(Context context, Alarm alarm) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmDao().update(alarm);
                return null;
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(AlarmDatabaseClient.getInstance(context).getAppDatabase());
    }

    public static void deleteAlarm(Context context, Alarm alarm) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmDao().delete(alarm);
                return null;
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AlarmDatabaseClient.getInstance(context).getAppDatabase());
    }

    public static void getAlarmById(Context context, long alarmId, PostExecuteCode code) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Alarm> {
            @Override
            protected Alarm doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                return db.alarmDao().getAlarmById(alarmId);
            }

            @Override
            protected void onPostExecute(Alarm alarm) {
                super.onPostExecute(alarm);
                code.doInPostExecuteWhenWeGotAlarm(alarm);
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(AlarmDatabaseClient.getInstance(context).getAppDatabase());
    }


    public static void createAlarmRepeating(Context context, AlarmRepeating alarmRepeating, PostExecuteCode code) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Long> {
            @Override
            protected Long doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                return db.alarmRepeatingDao().insert(alarmRepeating);
            }

            @Override
            protected void onPostExecute(Long createdAlarmRepeatingId) {
                code.doInPostExecuteWhenWeGotIdOfCreatedAlarmRepeating(createdAlarmRepeatingId);
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(AlarmDatabaseClient.getInstance(context).getAppDatabase());
    }

    public static void updateAlarmRepeating(Context context, AlarmRepeating alarmRepeating) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmRepeatingDao().update(alarmRepeating);
                return null;
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AlarmDatabaseClient.getInstance(context).getAppDatabase());
    }

    public static void deleteAlarmRepeating(Context context, AlarmRepeating alarmRepeating) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmRepeatingDao().delete(alarmRepeating);
                return null;
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AlarmDatabaseClient.getInstance(context).getAppDatabase());
    }

    public static void getAlarmRepeatingByParentAlarmId(Context context, long parentAlarmId, PostExecuteCode code) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, AlarmRepeating> {
            @Override
            protected AlarmRepeating doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                return db.alarmRepeatingDao().getByParentAlarmId(parentAlarmId);
            }

            @Override
            protected void onPostExecute(AlarmRepeating alarmRepeating) {
                super.onPostExecute(alarmRepeating);
                code.doInPostExecuteWhenWeGotAlarmRepeating(alarmRepeating);
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(AlarmDatabaseClient.getInstance(context).getAppDatabase());
    }


    public static void createAlarmPuzzle(Context context, AlarmPuzzle alarmPuzzle, PostExecuteCode code) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Long> {
            @Override
            protected Long doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                return db.alarmPuzzleDao().insert(alarmPuzzle);
            }

            @Override
            protected void onPostExecute(Long createdAlarmPuzzleId) {
                code.doInPostExecuteWhenWeGotIdOfCreatedAlarmPuzzle(createdAlarmPuzzleId);
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(AlarmDatabaseClient.getInstance(context).getAppDatabase());
    }

    public static void updateAlarmPuzzle(Context context, AlarmPuzzle alarmPuzzle) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmPuzzleDao().update(alarmPuzzle);
                return null;
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AlarmDatabaseClient.getInstance(context).getAppDatabase());
    }

    public static void deleteAlarmPuzzle(Context context, AlarmPuzzle alarmPuzzle) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmPuzzleDao().delete(alarmPuzzle);
                return null;
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AlarmDatabaseClient.getInstance(context).getAppDatabase());
    }

    public static void getAlarmPuzzleByParentAlarmId(Context context, long parentAlarmId, PostExecuteCode code) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, AlarmPuzzle> {
            @Override
            protected AlarmPuzzle doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                return db.alarmPuzzleDao().getByParentAlarmId(parentAlarmId);
            }

            @Override
            protected void onPostExecute(AlarmPuzzle alarmPuzzle) {
                super.onPostExecute(alarmPuzzle);
                code.doInPostExecuteWhenWeGotAlarmPuzzle(alarmPuzzle);
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AlarmDatabaseClient.getInstance(context).getAppDatabase());

    }

    ///////////////////////////////////////////////////////////////////////

    //ordinary getters
    public static ArrayList<Alarm> getAlarmList() {
        return alarmList;
    }

    public static ArrayList<AlarmRepeating> getAlarmRepeatingList() {
        return alarmRepeatingList;
    }

    public static ArrayList<AlarmPuzzle> getAlarmPuzzleList() {
        return alarmPuzzleList;
    }

    //these methods can get entity instance from our lists (not from Room database)
    public static Alarm getAlarmFromListById(long alarmId) {
        Alarm result = null;
        for(Alarm alarm: alarmList) {
            if(alarm.id==alarmId) {
                result = alarm;
                break;
            }
        }
        return result;
    }

    public static AlarmRepeating getAlarmRepeatingFromListByParentAlarmId(long parentAlarmId) {
        AlarmRepeating result = null;
        for(AlarmRepeating alarmRepeating: alarmRepeatingList) {
            if(alarmRepeating.parentAlarmId==parentAlarmId) {
                result = alarmRepeating;
                break;
            }
        }
        return result;
    }

    public static AlarmPuzzle getAlarmPuzzleFromListByParentAlarmId(long parentAlarmId) {
        AlarmPuzzle result = null;
        for(AlarmPuzzle alarmPuzzle: alarmPuzzleList) {
            if(alarmPuzzle.parentAlarmId==parentAlarmId) {
                result = alarmPuzzle;
                break;
            }
        }
        return result;
    }

    //this method turns on only one time when application is starting
    public static void importDb(Context context, PostExecuteCode code) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {

            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                AlarmDatabaseManager.alarmList = (ArrayList<Alarm>) db.alarmDao().getAll();
                AlarmDatabaseManager.alarmRepeatingList = (ArrayList<AlarmRepeating>) db.alarmRepeatingDao().getAll();
                AlarmDatabaseManager.alarmPuzzleList = (ArrayList<AlarmPuzzle>) db.alarmPuzzleDao().getAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                code.doInPostExecute();
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AlarmDatabaseClient.getInstance(context).getAppDatabase());
    }

    public static void exportDb(Context context) {
        class ExportTask extends AsyncTask<AlarmDatabase, Void, Void> {
            private ArrayList<Alarm> alarmList;
            private ArrayList<AlarmRepeating> alarmRepeatingList;
            private ArrayList<AlarmPuzzle> alarmPuzzleList;

            public ExportTask(ArrayList<Alarm> alarmList, ArrayList<AlarmRepeating> alarmRepeatingList, ArrayList<AlarmPuzzle> alarmPuzzleList) {
                this.alarmList = alarmList;
                this.alarmRepeatingList = alarmRepeatingList;
                this.alarmPuzzleList = alarmPuzzleList;
            }

            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                for(Alarm alarm : alarmList) {
                    if(!alarm.isEnabled) {
                        db.alarmDao().update(alarm);
                    }
                }
                for(AlarmRepeating alarmRepeating : alarmRepeatingList) {
                    db.alarmRepeatingDao().update(alarmRepeating);
                }
                for(AlarmPuzzle alarmPuzzle : alarmPuzzleList) {
                    db.alarmPuzzleDao().update(alarmPuzzle);
                }
                return null;
            }
        }

        ExportTask task = new ExportTask(alarmList, alarmRepeatingList, alarmPuzzleList);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, AlarmDatabaseClient.getInstance(context).getAppDatabase());

    }
}
