package com.example.balogtamas.monitortest.util;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;

import com.example.balogtamas.monitortest.R;

import static android.content.Context.MODE_PRIVATE;

public class UsageStatsPermissionHelper {

    private static final String TAG = "UsageStatsPermission";
    private Context context;
    private final int uid = android.os.Process.myUid();
    private SharedPreferences sharedPreferences;


    public UsageStatsPermissionHelper(Context context) {
        this.context = context;
        sharedPreferences = (this.context).getSharedPreferences(context.getString(R.string.sharedpreference_name_key), MODE_PRIVATE);
    }

    public void getUsagePermissions() {
        boolean granted = getUsageStats();
        if (!granted) {
            try {
                final Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.i(TAG, "getUsagePermissions: try - " + "" );
                context.startActivity(intent);
            } catch (final ActivityNotFoundException exception) {
                Log.i(TAG, "getUsagePermissions: catch - " + "" );
                final Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                final ComponentName componentName
                        = new ComponentName("com.android.settings", "com.android.settings.Settings$SecuritySettingsActivity");
                intent.setComponent(componentName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } else {
            Log.i(TAG, "getUsagePermissions: már engedélyezve van.");
        }

        //hivatalos API-ban a NEM system permissionokat így kell elkérni
        //if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_DENIED) {
        //if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.PACKAGE_USAGE_STATS)) {

        //} else {
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PACKAGE_USAGE_STATS}, 1);

        //} else{

        //}
        //Log.i(TAG, "getUsagePermissions: " + granted);
        //return granted;
    }

    public boolean getUsageStats()
    {
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats", uid, context.getApplicationContext().getPackageName());
        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted =  (context.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
         /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usageStatsManager=(UsageStatsManager)getSystemService("usagestats");
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                        UsageStatsManager.INTERVAL_DAILY,
                        0,
                        System.currentTimeMillis());
            granted = queryUsageStats == null || queryUsageStats.isEmpty();
        } else {
            granted = false;
        }
        */
        sharedPreferences.edit().putBoolean(context.getString(R.string.usagestats_permission_is_granted_key), granted).apply();
        return granted;
    }
}
