package com.example.balogtamas.monitortest;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import data.data.CPU.CpuDataReader;
import data.data.apps.AppData;
import data.data.apps.AppDataReader;
import data.data.apps.ProcessData;
import data.data.mem.GlobalMemData;
import data.data.mem.MemData;
import data.data.mem.MemDataReader;
import data.data.network.GlobalNetworkData;
import data.data.network.NetworkDataReader;


public class ServiceMonitor extends Service {

    private static final String TAG = "ServiceMonitor";
    private final Binder mBinder = new ServiceMonitorDataBinder();

    private MemDataReader memDataReader;
    private CpuDataReader cpuDataReader;
    private AppDataReader appDataReader;
    private NetworkDataReader networkDataReader;

    public class ServiceMonitorDataBinder extends Binder {
        public ServiceMonitor getService()
        {
            return ServiceMonitor.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: IBinder bound.");
        return new ServiceMonitorDataBinder();
    }

    public int getCPU() {
        cpuDataReader.update();
        return cpuDataReader.getCpuUsage();
    }

    /*public MemData getMem(int pid) {
        memDataReader.update(pid);
       return memDataReader.getMemData();
    }*/

    private ArrayList<MemData> getMemList()
    {
        return memDataReader.updateAll();
    }

   /* public MemData getMem(int pid)
    {
        return memDataReader.getData(pid);
    }*/

    public void getMem()
    {
        ArrayList<MemData> memDataArrayList = getMemList();
        for (MemData memData : memDataArrayList) {
            Log.i(TAG, "getMem: "+ memData.toString());
        }
    }


    public GlobalMemData getGlobalMem()
    {
        return memDataReader.getTotalMemData();
    }

    public ArrayList<AppData> getAppDatas() { return appDataReader.getAppData(); }

    public ArrayList<ProcessData> getProcesses() { return appDataReader.getProcessData(); }

    public GlobalNetworkData getNetworkData()
    {
        networkDataReader.update();
        return networkDataReader.getNetworkData();
    }



    @Override
    public void onCreate() {
        cpuDataReader = new CpuDataReader();
        memDataReader = new MemDataReader(getApplicationContext());
        appDataReader = new AppDataReader(getApplicationContext());
        networkDataReader = new NetworkDataReader(getApplicationContext());
    }

}
