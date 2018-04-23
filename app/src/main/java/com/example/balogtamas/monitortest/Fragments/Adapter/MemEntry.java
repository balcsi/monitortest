package com.example.balogtamas.monitortest.Fragments.Adapter;

import android.content.Context;

import com.example.balogtamas.monitortest.R;

import java.util.ArrayList;

public class MemEntry {
    int mColorId;
    int mDescriptionId;
    String mName;

    public MemEntry(int mColorId, int mDescriptionId, String mName) {
        this.mColorId = mColorId;
        this.mDescriptionId = mDescriptionId;
        this.mName = mName;
    }

    /*public static ArrayList<MemEntry> getMemEntries(String[] name, Context context) {
        ArrayList<MemEntry> entries = new ArrayList<>();
        for (String mName : name) {
            entries.add(getResources(mName, context));
        }
        return entries;
    }

    private static MemEntry getResources(String name, Context context)
    {
        int colorID = context.getResources().getIdentifier(name, "colors", context.getPackageName());
        int descriptionID = context.getResources().getIdentifier(name, "strings", context.getPackageName());
        return new MemEntry(colorID, descriptionID);
    }*/
}
