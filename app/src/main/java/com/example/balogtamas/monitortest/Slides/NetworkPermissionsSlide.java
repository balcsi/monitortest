package com.example.balogtamas.monitortest.Slides;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.balogtamas.monitortest.R;
import com.example.balogtamas.monitortest.util.NetworkPermissionHelper;

import agency.tango.materialintroscreen.SlideFragment;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_PHONE_STATE;

public class NetworkPermissionsSlide extends SlideFragment {

    TextView title, desc;
    Button button;
    NetworkPermissionHelper networkPermissionHelper;
    //TODO phone-state nem kell (globális network adatok)
    final String[] permissions = new String[]
            {
                ACCESS_WIFI_STATE,
                ACCESS_NETWORK_STATE,
                INTERNET,
                READ_PHONE_STATE
            };
    private static final String TAG = "NetworkPermissionsSlide";

    @Override
    public int backgroundColor() {
        return R.color.colorPrimary;
    }

    @Override
    public int buttonsColor() {
        return R.color.colorPrimaryDark;
    }

    @Override
    public boolean canMoveFurther() {
        return networkPermissionHelper.hasPermissions(permissions);
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getActivity().getApplicationContext().getString(R.string.slide_network_permission_cant_move_further);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.fragment_network_permissions, container, false);
        this.networkPermissionHelper = new NetworkPermissionHelper(context);
        title = view.findViewById(R.id.network_title);
        desc = view.findViewById(R.id.network_desc);
        button = view.findViewById(R.id.network_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(networkPermissionHelper.hasPermissions(permissions))) {
                    //TODO
                    getActivity().requestPermissions(permissions, 100);
                } else {
                    Toast.makeText(context, context.getString(R.string.intro_activity_permission_granted_already), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==100) {
            //TODO
        }
    }
}