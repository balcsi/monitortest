package com.example.balogtamas.monitortest.Activities;

//import android.Manifest;
//import android.app.AlertDialog;
//import android.app.AppOpsManager;
//import android.app.DialogFragment;
//import android.app.usage.UsageStats;
//import android.app.usage.UsageStatsManager;
//import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
//import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;

import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.balogtamas.monitortest.Dialogs.CpuInfoDialog;
import com.example.balogtamas.monitortest.Dialogs.IntervalReadDialog;
import com.example.balogtamas.monitortest.Dialogs.MemInfoDialog;
import com.example.balogtamas.monitortest.Dialogs.WarningDialog;
import com.example.balogtamas.monitortest.Fragments.Adapter.PagerAdapter;
import com.example.balogtamas.monitortest.Fragments.CPUFragment;
import com.example.balogtamas.monitortest.Fragments.MEMFragment;
import com.example.balogtamas.monitortest.Fragments.ProcessFragment;
import com.example.balogtamas.monitortest.Interfaces.ICpuDataSender;
import com.example.balogtamas.monitortest.Interfaces.IMemDataSender;

//import com.example.balogtamas.monitortest.Interfaces.MemFragmentEntryOnClick;
import com.example.balogtamas.monitortest.Interfaces.IProcessDataSender;
import com.example.balogtamas.monitortest.R;
import com.example.balogtamas.monitortest.ServiceMonitor;
import com.example.balogtamas.monitortest.util.UsageStatsPermissionHelper;

import java.util.ArrayList;
import data.data.apps.AppData;
import data.data.apps.ProcessData;
import data.data.mem.GlobalMemData;


public class DataActivity extends AppCompatActivity {

    private static final String TAG = "DataActivity";

    //permission check
    UsageStatsPermissionHelper usageStatsPermissionHelper;
    Context context;

    //coordinatorlayout
    ViewPager viewPager;
    TabLayout tabLayout;
    PagerAdapter pagerAdapter;
    Toolbar toolbar;

    SharedPreferences sharedPreferences;
    int readInterval;
    boolean getGlobalNetwork;




    //cpufragment
    private ICpuDataSender cpuDataSender;

    //memfragment
    private IMemDataSender memDataSender;

    //processfragment
    private IProcessDataSender iProcessDataSender;

    //service management
    ServiceMonitor serviceMonitor;
    boolean isBound = false;
    boolean isRunning = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceMonitor = ((ServiceMonitor.ServiceMonitorDataBinder) (service)).getService();
            if(serviceMonitor != null) {
                mHandler.post(mRunnable);
                isBound = true;
                isRunning = true;
                Log.i(TAG, "onServiceConnected: binded");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceMonitor = null;
            isBound = false;
            isRunning = false;
    }};


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(sharedPreferences.getBoolean(getString(R.string.readIntervalChanged), false)) {
                readInterval = sharedPreferences.getInt(getString(R.string.readInterval), R.integer.default_readInterval);
                sharedPreferences.edit().putBoolean(getString(R.string.readIntervalChanged), false).apply();
            }
            mHandler.postDelayed(this, readInterval);

            //void->int/float --> így spórolunk függvényhívást
            ((TextView) (toolbar.findViewById(R.id.toolbar_lin_lay)).findViewById(R.id.toolbar_cpu)).setText("CPU: " + getCpuUsage() + "%");
            ((TextView) (toolbar.findViewById(R.id.toolbar_lin_lay)).findViewById(R.id.toolbar_mem)).setText("MEM: " + (int) getGlobalMemData() + "MB");
            if(getGlobalNetwork) {
                Log.d(TAG, "run: " + serviceMonitor.getNetworkData());
            }
        }
    };
    private Handler mHandler = new Handler();

    //debughoz:
    //ArrayList<AppData> processList = new ArrayList<>();
    //ArrayList<ProcessData> processDataArrayList = new ArrayList<>();

    //private final Thread readThread = new Thread(mRunnable);

    //TextView textView;
    //EditText editText;
    //Button btn;

    //interfészekhez

    public void setCpuDataSender (ICpuDataSender cpuDataSender)
    {
        this.cpuDataSender = cpuDataSender;
    }

    public void setMemDataSender (IMemDataSender memDataSender)
    {
        this.memDataSender = memDataSender;
    }

    public ICpuDataSender getCpuDataSender() {
        return cpuDataSender;
    }

    public IMemDataSender getMemDataSender() {
        return memDataSender;
    }

    public IProcessDataSender getiProcessDataSender() {
        return iProcessDataSender;
    }

    public void setiProcessDataSender(IProcessDataSender iProcessDataSender) {
        this.iProcessDataSender = iProcessDataSender;
    }

    int getCpuUsage()
    {
        int retVal = 0;
        if(serviceMonitor!= null) {
            if(cpuDataSender != null) {
                retVal = serviceMonitor.getCPU();
                cpuDataSender.drawCpuGraph(retVal);
            }
        }
        return retVal;
    }

    float getGlobalMemData()
    {
        GlobalMemData globalMemData;
        float retVal= 0f;
        if(serviceMonitor!= null) {
            if(memDataSender != null) {
                globalMemData = serviceMonitor.getGlobalMem();
                memDataSender.drawMemPieChart(globalMemData);
                memDataSender.drawMemBarChart(globalMemData);
                retVal = serviceMonitor.getFreeMem();
                if(retVal != 0) {
                    retVal = retVal/1000;
                }
            }
        }
        return retVal;
    }

    //a folyamatokhoz tartozó fragmentben a folyamatlista frissítésére
    public void displayProcesses()
    {
        if(serviceMonitor!= null) {
            if (iProcessDataSender != null) {
                iProcessDataSender.displayProcesses(serviceMonitor.getProcesses());
            }
        }
    }

    //menüben 'refresh'
    void doOneTick()
    {
        getCpuUsage();
        getGlobalMemData();
        displayProcesses();
    }


    /* void displayProcesses()
    {
        if(serviceMonitor != null) {
            if (iProcessDataSender != null) {
                iProcessDataSender.displayProcesses(serviceMonitor.getProcesses());
            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        context = getApplicationContext();
        initCoordinatorLayout();
        initToolBar();
        sharedPreferences = getSharedPreferences(getString(R.string.sharedpreference_name_key), MODE_PRIVATE);
        readInterval = sharedPreferences.getInt(getString(R.string.readInterval),getResources().getInteger(R.integer.default_readInterval));
        getGlobalNetwork = true;
        usageStatsPermissionHelper = new UsageStatsPermissionHelper(getApplicationContext());
        usageStatsPermissionHelper.getUsagePermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(serviceMonitor!= null) {
            startMonitor();
        }
        if(!(usageStatsPermissionHelper.getUsageStats())) {
            //ha kinyomjuk az engedélyt, de a stackben még megvan ez a activity
            showWarningDialog();
        }
    }

    void showWarningDialog()
    {
        WarningDialog newFragment = new WarningDialog();
        newFragment.show(getFragmentManager(), getString(R.string.dialog_warning_name_key));
    }

    private void initCoordinatorLayout()
    {
        viewPager = findViewById(R.id.activity_data_view_pager);
        tabLayout = findViewById(R.id.activity_data_tab_layout);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        setupAdapter(pagerAdapter);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setupAdapter(PagerAdapter adapter)
    {
        pagerAdapter.addFragment(new CPUFragment(), "CPU");
        pagerAdapter.addFragment(new MEMFragment(), "MEM");
        pagerAdapter.addFragment(new ProcessFragment(), "Processes");
    }
    private void initToolBar()
    {
        toolbar = findViewById(R.id.activity_data_tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_data_menu, menu);
        //MenuItem item = menu.findItem(R.id.menu_item_switch);
        //item.setActionView(R.layout.menu_switch);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_actionStop :
                if(isRunning) {
                    stopMonitor();
                } else {
                    startMonitor();
                }
                //return isRunning;
                break;

            case R.id.menu_actionSetInterval :
                //https://stackoverflow.com/questions/4473940/android-best-practice-returning-values-from-a-dialog
                new IntervalReadDialog().show(getFragmentManager(), getString(R.string.dialog_readInterval_title_text));
                Log.d(TAG, "onOptionsItemSelected: " + readInterval);
                break;

            case R.id.menu_actionRefresh :
                doOneTick();
                break;

            case R.id.menu_actionCpuInfo :
                new CpuInfoDialog().show(getFragmentManager(), "dialog_cpu_info");
                break;

            case R.id.menu_actionMemInfo :
                new MemInfoDialog().show(getFragmentManager(), "dialog_mem_info");
                break;

            case R.id.menu_actionGetGlobalNetwork :
                getGlobalNetwork = !getGlobalNetwork;
                Log.d(TAG, "onOptionsItemSelected: getGlobalNetworkChanged to " + getGlobalNetwork );

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    void stopMonitor()
    {
        mHandler.removeCallbacks(mRunnable);
        isRunning = false;
    }
    void startMonitor()
    {
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
        isRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMonitor();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, ServiceMonitor.class), serviceConnection, BIND_AUTO_CREATE);
    }

    //debug
    private void stopThread()
    {
        try {
            //readThread.interrupt();
        } catch  (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stopService(new Intent(this, ServiceMonitor.class));
        stopMonitor();
        unbindService(serviceConnection);

    }

}
