package com.example.balogtamas.monitortest.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.balogtamas.monitortest.R;
import com.example.balogtamas.monitortest.Slides.DescriptionSlide;
import com.example.balogtamas.monitortest.Slides.UsageStatsSlide;
import com.example.balogtamas.monitortest.util.UsageStatsPermissionHelper;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class IntroActivity extends MaterialIntroActivity{

    UsageStatsPermissionHelper usageStatsPermissionHelper;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usageStatsPermissionHelper = new UsageStatsPermissionHelper(getApplicationContext());

        enableLastSlideAlphaExitTransition(true);
        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });
        addSlide(new DescriptionSlide());
        addSlide(new UsageStatsSlide());

        /*addSlide(new SlideFragmentBuilder()
                .backgroundColor(android.support.v7.appcompat.R.color.background_material_light)
                //.possiblePermissions(new String[] {Manifest.permission.PACKAGE_USAGE_STATS})
                .neededPermissions(new String[]{Manifest.permission.PACKAGE_USAGE_STATS})
                .image(agency.tango.materialintroscreen.R.drawable.ic_previous)
                .title("mytitle")
                .description("mydescription")
                .build(),
            new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showMessage("placeholder");
                    }
            }, "next"));*/
    }

    @Override
    public void onFinish() {
        super.onFinish();
        startActivity(new Intent(this, DataActivity.class));

        //DEBUG

        /*final Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Log.i(TAG, "getUsagePermissions: try - " + "" );
        getApplicationContext().startActivity(intent);*/

        /*if(usageStatsPermissionHelper.getUsageStats()) {
            startActivity(new Intent(this, DataActivity.class));
        } else {
            Toast.makeText(this, "A továbblépéshez engedélyezni kell az UsageStats használatát!", Toast.LENGTH_LONG).show();
        }*/


    }
}
