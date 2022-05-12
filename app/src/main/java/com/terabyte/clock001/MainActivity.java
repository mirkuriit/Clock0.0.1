package com.terabyte.clock001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        if(AlarmDatabaseManager.getAlarmList()==null) {
            AlarmDatabaseManager.importDb(getApplicationContext(), new PostExecuteCode() {
                @Override
                public void doInPostExecute() {
                    actionsInOnCreate();
                }
            });
        }
        else {
            actionsInOnCreate();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlarmDatabaseManager.exportDb(getApplicationContext());
    }


    private void actionsInOnCreate() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentById(R.id.frameLayoutForFragments)==null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frameLayoutForFragments, new AlarmFragment());
            fragmentTransaction.commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch(item.getItemId()) {
                    case R.id.menuItemAlarm:
                        transaction.replace(R.id.frameLayoutForFragments, new AlarmFragment());
                        break;
                    case R.id.menuItemStopwatch:
                        transaction.replace(R.id.frameLayoutForFragments, new StopwatchFragment());
                        break;
                    case R.id.menuItemTimer:
                        TimerFragment timerFragment;
                        if(TimerService.getRunning()) {
                            timerFragment = new TimerFragment(Const.MODE_RUN);
                        }
                        else {
                            timerFragment = new TimerFragment(Const.MODE_SLEEP);
                        }
                        transaction.replace(R.id.frameLayoutForFragments, timerFragment);
                        break;
                }
                transaction.commit();
                return true;
            }
        });
    }
}