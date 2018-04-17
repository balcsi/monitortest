package com.example.balogtamas.monitortest.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.balogtamas.monitortest.R;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    SharedPreferences sharedPreferences;
    boolean usageStatsGranted;
    boolean networkGranted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(getString(R.string.sharedpreference_name_key), MODE_PRIVATE);
        usageStatsGranted = sharedPreferences.getBoolean(getString(R.string.usagestats_permission_is_granted_key), false);
        networkGranted = sharedPreferences.getBoolean(getString(R.string.network_permission_is_granted_key), false);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.i(TAG, "onStart: " + usageStatsGranted);
        Log.d(TAG, "onStart:\n" + "usageStatsGranted: " + usageStatsGranted +"\nnetworkGranted: " + networkGranted);
        if(usageStatsGranted && networkGranted) {
            this.startActivity(new Intent(this, DataActivity.class));
            finish();
        } else {
            this.startActivity(new Intent(this, IntroActivity.class));
            //elvileg a startactivity-nek meg kellene hivnia az ondestroyt () az eredeti activity-n, de finish() nélkül nem megy a vissza gomb
            //TODO::startactivity+finish()
            finish();
        }

    }

}
