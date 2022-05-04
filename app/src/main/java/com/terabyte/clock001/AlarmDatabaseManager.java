package com.terabyte.clock001;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

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
    private static ArrayList<Alarm> alarmNotChangedList = null;
    private static ArrayList<Alarm> alarmList;

    private static ArrayList<AlarmRepeating> alarmRepeatingNoChangedList = null;
    private static ArrayList<AlarmRepeating> alarmRepeatingList = null;

    private static ArrayList<AlarmPuzzle> alarmPuzzleNotChangedList = null;
    private static ArrayList<AlarmPuzzle> alarmPuzzleList = null;


    public static void createAlarm(AlarmDatabase db, Alarm alarm, PostExecuteCode code) {
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
        task.execute(db);
    }

    public static void updateAlarm(AlarmDatabase db, Alarm alarm) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmDao().update(alarm);
                return null;
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(db);
    }

    public static void deleteAlarm(AlarmDatabase db, Alarm alarm) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmDao().delete(alarm);
                return null;
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(db);
    }

    public static void getAlarmById(AlarmDatabase db, long alarmId, PostExecuteCode code) {
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
        task.execute(db);
    }

    public static void createAlarmRepeating(AlarmDatabase db, AlarmRepeating alarmRepeating, PostExecuteCode code) {
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
        task.execute(db);
    }

    public static void updateAlarmRepeating(AlarmDatabase db, AlarmRepeating alarmRepeating) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmRepeatingDao().update(alarmRepeating);
                return null;
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(db);
    }

    public static void deleteAlarmRepeating(AlarmDatabase db, AlarmRepeating alarmRepeating) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmRepeatingDao().delete(alarmRepeating);
                return null;
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(db);
    }

    public static void getAlarmRepeatingByParentAlarmId(AlarmDatabase db, long parentAlarmId, PostExecuteCode code) {
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
        task.execute(db);
    }

    public static void createAlarmPuzzle(AlarmDatabase db, AlarmPuzzle alarmPuzzle, PostExecuteCode code) {
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
        task.execute(db);
    }

    public static void updateAlarmPuzzle(AlarmDatabase db, AlarmPuzzle alarmPuzzle) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmPuzzleDao().update(alarmPuzzle);
                return null;
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(db);
    }

    public static void deleteAlarmPuzzle(AlarmDatabase db, AlarmPuzzle alarmPuzzle) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmPuzzleDao().delete(alarmPuzzle);
                return null;
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(db);
    }

    public static void getAlarmPuzzleByParentAlarmId(AlarmDatabase db, long parentAlarmId, PostExecuteCode code) {
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
        task.execute(db);
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
    public static void importDb(AlarmDatabase db, PostExecuteCode code) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {

            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                AlarmDatabaseManager.alarmNotChangedList = (ArrayList<Alarm>) db.alarmDao().getAll();
                AlarmDatabaseManager.alarmList = (ArrayList<Alarm>) AlarmDatabaseManager.alarmNotChangedList.clone();

                AlarmDatabaseManager.alarmRepeatingNoChangedList = (ArrayList<AlarmRepeating>) db.alarmRepeatingDao().getAll();
                AlarmDatabaseManager.alarmRepeatingList = (ArrayList<AlarmRepeating>) AlarmDatabaseManager.alarmRepeatingNoChangedList.clone();

                AlarmDatabaseManager.alarmPuzzleNotChangedList = (ArrayList<AlarmPuzzle>) db.alarmPuzzleDao().getAll();
                AlarmDatabaseManager.alarmPuzzleList = (ArrayList<AlarmPuzzle>) AlarmDatabaseManager.alarmPuzzleNotChangedList.clone();
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                code.doInPostExecute();
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(db);
    }

    //we start this method when we create new entity in our application
    public static void updateAlarmList(Alarm alarm) {
        alarmNotChangedList.add(alarm);
        alarmList.add(alarm);
    }

    public static void updateAlarmRepeatingList(AlarmRepeating alarmRepeating) {
        alarmRepeatingNoChangedList.add(alarmRepeating);
        alarmRepeatingList.add(alarmRepeating);
    }

    public static void updateAlarmPuzzleList(AlarmPuzzle alarmPuzzle) {
        alarmPuzzleNotChangedList.add(alarmPuzzle);
        alarmPuzzleList.add(alarmPuzzle);
    }

    public static void exportDb(Context context) {
        for(Alarm alarm : alarmNotChangedList) {
                if(!alarmList.contains(alarm)) {
                    deleteAlarm(AlarmDatabaseClient.getInstance(context).getAppDatabase(), alarm);
                }
                else {
                    updateAlarm(AlarmDatabaseClient.getInstance(context).getAppDatabase(), alarm);
                }
            }
        for(AlarmRepeating alarmRepeating : alarmRepeatingNoChangedList) {
                if(!alarmRepeatingList.contains(alarmRepeating)) {
                    deleteAlarmRepeating(AlarmDatabaseClient.getInstance(context).getAppDatabase(), alarmRepeating);
                }
                else {
                    updateAlarmRepeating(AlarmDatabaseClient.getInstance(context).getAppDatabase(), alarmRepeating);
                }
            }
        for(AlarmPuzzle alarmPuzzle : alarmPuzzleNotChangedList) {
                if(!alarmPuzzleList.contains(alarmPuzzle)) {
                    deleteAlarmPuzzle(AlarmDatabaseClient.getInstance(context).getAppDatabase(), alarmPuzzle);
                }
                else {
                    updateAlarmPuzzle(AlarmDatabaseClient.getInstance(context).getAppDatabase(), alarmPuzzle);
                }
            }
    }
}
