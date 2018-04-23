package com.example.balogtamas.monitortest.Fragments;

import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.balogtamas.monitortest.Activities.DataActivity;
import com.example.balogtamas.monitortest.Fragments.Adapter.ProcessListAdapter;
import com.example.balogtamas.monitortest.Interfaces.IMemDataSender;
import com.example.balogtamas.monitortest.Interfaces.IProcessDataSender;
import com.example.balogtamas.monitortest.R;

import java.util.ArrayList;
import java.util.List;

import data.data.apps.ProcessData;
import data.data.mem.GlobalMemData;
import data.data.mem.MemData;

public class ProcessFragment extends Fragment {


    private static final String TAG = "ProcessFragment";

    private ProcessListAdapter adapter;
    List<ProcessData> processDataList = new ArrayList<>();
    ListView processListView;
    Button button;
    ProgressBar progressBar;




    IProcessDataSender iProcessDataSender = new IProcessDataSender() {

        @Override
        public void displayProcesses(ArrayList<ProcessData> processDataArrayList) {
            processDataList.clear();
            processDataList = processDataArrayList;
            //ui threaden változtatok valamit, muszáj így hívni
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setAdapter();
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_process, container, false);
        //TODO: ez lehet nem kell, illetve ha kell, tuti nem így adunk neki uid-t
        //debug
        view.setId(getContext().getResources().getInteger(R.integer.ProcessFragment_id));
        processListView = view.findViewById(R.id.fragment_process_process_list);
        //setAdapter();
        progressBar = view.findViewById(R.id.fragment_process_progress);
        button = view.findViewById(R.id.fragment_process_refresh);
        button.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FillListViewTask().execute();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() instanceof DataActivity) {
            ((DataActivity)getActivity()).setiProcessDataSender(iProcessDataSender);
            Log.d(TAG, "onActivityCreated: setMemDataSender called.");
        }
        if(((DataActivity)getActivity()).getiProcessDataSender() == iProcessDataSender) {
            setAdapter();
            new FillListViewTask().execute();
        }
    }

    private void setProgressBar(int mode)
    {
        switch (mode) {
            case 0 :
                processListView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            break;

            case 1 :
                progressBar.setVisibility(View.GONE);
                processListView.setVisibility(View.VISIBLE);
            break;
        }
    }

    private void setAdapter()
    {
        adapter = new ProcessListAdapter(getActivity(), R.layout.fragment_mem_process_entry , processDataList);
        processListView.setAdapter(adapter);
    }

    class FillListViewTask extends AsyncTask<Void, Integer, Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... voids) {
            ((DataActivity)getActivity()).displayProcesses();
            return null;
        }

        @Override
        protected void onPreExecute() {
            //ui threaden kell futtatni
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setProgressBar(0);
                }
            });
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //ui threaden kell futtatni
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setProgressBar(1);
                }
            });
            super.onPostExecute(aBoolean);
        }
    }
}
