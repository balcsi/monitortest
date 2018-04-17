package com.example.balogtamas.monitortest.Slides;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.balogtamas.monitortest.R;
import com.example.balogtamas.monitortest.util.UsageStatsPermissionHelper;

import agency.tango.materialintroscreen.SlideFragment;

import static android.content.Context.MODE_PRIVATE;

public class UsageStatsSlide extends SlideFragment {

    TextView title, desc;
    Button button;
    UsageStatsPermissionHelper usageStatsPermissionHelper;

    private static final String TAG = "UsageStatsSlide";

    //int uid = android.os.Process.myUid();

    @Override
    public int backgroundColor() {
        //return super.backgroundColor();
        return R.color.colorPrimary;
    }

    @Override
    public int buttonsColor() {
        //return super.buttonsColor();
        return R.color.colorPrimaryDark;
    }

    @Override
    public boolean canMoveFurther() {
        if(usageStatsPermissionHelper!= null) {
            return usageStatsPermissionHelper.getUsageStats();
        } else {
            return false;
        }
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getActivity().getApplicationContext().getString(R.string.slide_usage_stats_cant_move_further);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        usageStatsPermissionHelper = new UsageStatsPermissionHelper(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_usage_stats_permission, container, false);
        title = view.findViewById(R.id.usagestats_title);
        desc = view.findViewById(R.id.usagestats_desc);
        button = view.findViewById(R.id.usagestats_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usageStatsPermissionHelper.getUsagePermissions();// getUsagePermissions();
            }
        });
        return view;
    }

}
