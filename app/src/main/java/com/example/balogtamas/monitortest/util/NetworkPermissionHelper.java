package com.example.balogtamas.monitortest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.example.balogtamas.monitortest.R;

public class NetworkPermissionHelper {

    private Context context;
    SharedPreferences sharedPreferences;
    public NetworkPermissionHelper(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedpreference_name_key), Context.MODE_PRIVATE);
    }

    public boolean hasPermission(String permission)
    {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public  boolean hasPermissions(String[] permissions)
    {
        boolean hasPermissions = true;
        for (String permission : permissions) {
            if(!(hasPermission(permission))) {
                hasPermissions = false;
                break;
            }
        }
        sharedPreferences.edit().putBoolean(context.getString(R.string.network_permission_is_granted_key), hasPermissions).apply();
        return hasPermissions;
    }
}
