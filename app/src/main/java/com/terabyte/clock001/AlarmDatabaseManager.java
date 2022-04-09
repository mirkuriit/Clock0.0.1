package com.terabyte.clock001;

import android.os.AsyncTask;

import java.util.List;

/**
 * I created this class to easily work with database: delete, update and create objects in Room Database
 * Actually, I've just realize methods of Dao interface.
 * Without that class I would write many AsyncTask classes everywhere. I would be very stupid
 * We can't work with Room Database in main UI thread. So, we work with it in AsyncTask
 *
 * Also, some methods have Interface PostExecuteCode in params. I created this interface to work with our
 * data from Room Database in main thread again (interface methods work in onPostExecute() method of AsyncTask)
 * It's very useful to work with database data in main thread after getting it in AsyncTask thread
 * Obviously, I've just solved architecture problem
 */

public class AlarmDatabaseManager {

    public static void createAlarm(AlarmDatabase db, Alarm alarm, PostExecuteCode code) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmDao().insert(alarm);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                code.doInPostExecute();
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

    public static void deleteAlarm(AlarmDatabase db, Alarm alarm, PostExecuteCode code) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmDao().delete(alarm);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                code.doInPostExecute();
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(db);
    }

    public static void getAllAlarms(AlarmDatabase db, PostExecuteCode code) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, List<Alarm>> {
            @Override
            protected List<Alarm> doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                return db.alarmDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Alarm> alarms) {
                super.onPostExecute(alarms);
                code.doInPostExecuteWhenWeGotAllAlarms(alarms);
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(db);
    }

    public static void createAlarmRepeatingIfNotExists(AlarmDatabase db, long parentAlarmId, boolean[] repeatingDays) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                if(db.alarmRepeatingDao().getByParentAlarmId(parentAlarmId)==null) {
                    AlarmRepeating repeating = new AlarmRepeating(parentAlarmId);
                    //algorithmic govnocode is beginning here
                    // FIXME: 30.03.2022 algorithmic govnocode
                    repeating.mo = repeatingDays[0];
                    repeating.tu = repeatingDays[1];
                    repeating.we = repeatingDays[2];
                    repeating.th = repeatingDays[3];
                    repeating.fr = repeatingDays[4];
                    repeating.sa = repeatingDays[5];
                    repeating.su = repeatingDays[6];
                    //algorithmic gobnocode is finishing here
                    db.alarmRepeatingDao().insert(repeating);
                }
                return null;
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

    public static void createAlarmPuzzle(AlarmDatabase db, AlarmPuzzle alarmPuzzle) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmPuzzleDao().insert(alarmPuzzle);
                return null;
            }
        }

        DatabaseTask task = new DatabaseTask();
        task.execute(db);
    }

    public static void updateAlarmPuzzle(AlarmDatabase db, AlarmPuzzle alarmPuzzle, PostExecuteCode code) {
        class DatabaseTask extends AsyncTask<AlarmDatabase, Void, Void> {
            @Override
            protected Void doInBackground(AlarmDatabase... alarmDatabases) {
                AlarmDatabase db = alarmDatabases[0];
                db.alarmPuzzleDao().update(alarmPuzzle);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                code.doInPostExecute();
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
}
