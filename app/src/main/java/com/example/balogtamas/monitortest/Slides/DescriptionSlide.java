package com.example.balogtamas.monitortest.Slides;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.balogtamas.monitortest.R;
import com.example.balogtamas.monitortest.util.UsageStatsPermissionHelper;

import agency.tango.materialintroscreen.SlideFragment;

public class DescriptionSlide extends SlideFragment {

    TextView title, desc;
    private static final String TAG = "DescriptionSlide";


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
        return super.canMoveFurther();
        //return getUsageStats();
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return super.cantMoveFurtherErrorMessage();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_description, container, false);
        title = view.findViewById(R.id.description_title);
        desc = view.findViewById(R.id.description_desc);
        return view;
    }

}