package com.example.balogtamas.monitortest.Fragments.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.balogtamas.monitortest.R;

import org.w3c.dom.Text;

import java.util.LinkedHashMap;
import java.util.List;

import data.data.apps.ProcessData;

public class MemoryDescriptionAdapter extends ArrayAdapter<MemEntry>{

    private Context context;
    private int mResource;

    public MemoryDescriptionAdapter(@NonNull Context context, int mResource, @NonNull List<MemEntry> objects) {
        super(context, mResource, objects);
        this.context = context;
        this.mResource = mResource;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
        //View color;
        TextView name;
        TextView description;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MemEntry entry = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder(convertView);
            holder.description = convertView.findViewById(R.id.adapter_mem_description);
            holder.name = convertView.findViewById(R.id.adapter_mem_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.description.setText(context.getString(entry.mDescriptionId));
        holder.name.setText(entry.mName);
        holder.name.setTextColor(entry.mColorId);
        return convertView;
        }
    }

