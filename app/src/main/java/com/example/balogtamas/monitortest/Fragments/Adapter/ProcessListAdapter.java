package com.example.balogtamas.monitortest.Fragments.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.balogtamas.monitortest.R;

import org.w3c.dom.Text;

import java.util.List;

import data.data.apps.AppData;
import data.data.apps.ProcessData;

public class ProcessListAdapter extends ArrayAdapter<ProcessData> {

    private static final String TAG = "ProcessListAdapter";

    private Context context;
    private int mResource;

    public ProcessListAdapter(@NonNull Context context, int resource, @NonNull List<ProcessData> objects) {
        super(context, resource, objects);
        this.context = context;
        mResource = resource;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(additionalInfo.getVisibility() == View.VISIBLE) {
                        additionalInfo.setVisibility(View.GONE);
                    } else additionalInfo.setVisibility(View.VISIBLE);
                }
            });
        }

        TextView pid;
        TextView name;
        TextView pkgName;
        TextView additionalInfo;
        //ProcessData processData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        ProcessData process = getItem(position);
        try {
            ViewHolder holder;
            StringBuilder sb = new StringBuilder();
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(mResource, parent, false);
                holder = new ViewHolder(convertView);
                //ugyanaz volt a layout neve, mint az ID
                    holder.pid = convertView.findViewById(R.id.adapter_mem_process_entry_pid);
                    holder.name = convertView.findViewById(R.id.adapter_mem_process_entry_name);
                    holder.pkgName = convertView.findViewById(R.id.adapter_mem_process_entry_pkg_name);
                    //holder.processData = process;
                    holder.additionalInfo = convertView.findViewById(R.id.adapter_mem_process_entry_additional);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if(process.getAppData().toString() != null) {
                if(android.os.Process.myPid() == process.getAppData().getmPID()) {
                    holder.pid.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                }
                holder.pid.setText(Integer.toString(process.getAppData().getmPID()));
                holder.name.setText(process.getAppData().getmName());
                holder.pkgName.setText(process.getAppData().getmPackage());
                sb.append(process.getMemData()).append(process.getProcessNetData().toStringNotEmpty());
                holder.additionalInfo.setText(sb.toString());
                // /holder.additionalInfo.setText(process.getMemData().toString());
            }
            return convertView;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage());
            return convertView;
        }
    }
}
