package com.example.balogtamas.monitortest.Fragments.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.balogtamas.monitortest.Fragments.CPUFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//https://stackoverflow.com/questions/18747975/difference-between-fragmentpageradapter-and-fragmentstatepageradapter
public class PagerAdapter extends FragmentPagerAdapter {


    private static final String TAG = "PagerAdapter";
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> fragmentTitleList = new ArrayList<>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);

        /*Fragment fragment = null;
        switch (position) {
            case 1 : fragment = new CPUFragment();
            break;

            case 2 : //return "MEM";
            break;

            case 3 : //return "HARDWARE";
            break;

            default: //return "";
            break;
        }
        return fragment;*/
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.i(TAG, "getPageTitle: " + fragmentTitleList.get(position));
        return fragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }
}
