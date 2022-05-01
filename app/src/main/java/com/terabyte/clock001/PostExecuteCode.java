package com.terabyte.clock001;

import java.util.List;

/**
 * That interface is used in AlarmDatabaseManager methods
 *methods have default word because in some realisations of this interface we need work only with one-two methods of interface
 * And in Java if we write default word we have to add { } to write default realisation of method, but in our architecture
 * we don't need have some code in default realisation
 */

public interface PostExecuteCode {
    //we override this method only when we use method getAlarmRepeatingByParentAlarmId() of class AlarmDatabaseManager
    default void doInPostExecuteWhenWeGotAlarmRepeating(AlarmRepeating alarmRepeating) {

    }

    //we override this method only when we use method getAlarmPuzzleByParentAlarmId() of class AlarmDatabaseManager
    default void doInPostExecuteWhenWeGotAlarmPuzzle(AlarmPuzzle alarmPuzzle) {

    }

    //we override this method when we use method getAlarmById() of class AlarmDatabaseManager
    default void doInPostExecuteWhenWeGotAlarm(Alarm alarm) {

    }

    //we override this method when we use methods getAllAlarms() of class AlarmDatabaseManager
    default void doInPostExecuteWhenWeGotAllAlarms(List<Alarm> alarms) {

    }

    //we override this method when we use method createAlarm of class AlarmDatabaseManager
    default void doInPostExecuteWhenWeGotIdOfCreatedAlarm(Long createdAlarmId) {

    }

    //we use this method in other situations kinda importDb()
    default void doInPostExecute() {

    }



}
