package com.terabyte.clock001;

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

    //we use this method in other situations
    default void doInPostExecute() {

    }



}
