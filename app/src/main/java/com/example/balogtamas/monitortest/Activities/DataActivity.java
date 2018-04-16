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


import com.example.balogtamas.monitortest.Dialogs.IntervalReadDialog;
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
                //setCpuReaderListener();
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
            mHandler.postDelayed(this, readInterval);
            //if(serviceMonitor!= null) {
                ////Log.i(TAG, "cpu usage: " + serviceMonitor.getCPU());
                getCpuUsage();
                getGlobalMemData();
                //displayProcesses();
                ////Log.i(TAG, "mem usage: " + serviceMonitor.getMem(15060).toString());
                ////Log.i(TAG, "mem usage: " + serviceMonitor.getMem());
                //serviceMonitor.getMem();
                ////Log.i(TAG, "global mem usage: " +  serviceMonitor.getGlobalMem().toString());
                //processList = serviceMonitor.getAppDatas();
                //Log.d(TAG, "processlist: " + '\n');
                //for (AppData data : processList) {
                    //Log.d(TAG, data.toString());
                //}
                /*processDataArrayList = serviceMonitor.getProcesses();
                for (ProcessData data : processDataArrayList) {
                    Log.d(TAG, data.toString());
                }*/

                if( usageStatsPermissionHelper!= null && usageStatsPermissionHelper.getUsageStats()) {

                }
                Log.i(TAG, "getUsageStats: " + usageStatsPermissionHelper.getUsageStats());

        }
    };
    private Handler mHandler = new Handler();


    /*MemFragmentEntryOnClick memFragmentEntryOnClick = new MemFragmentEntryOnClick() {
        @Override
        public void onClick() {
            doUpdate = !doUpdate;
        }
    };*/

    //debughoz:
    //ArrayList<AppData> processList = new ArrayList<>();
    //ArrayList<ProcessData> processDataArrayList = new ArrayList<>();


    //private final Thread readThread = new Thread(mRunnable);

    //TextView textView;
    //EditText editText;
    //Button btn;


   // @Override
   /* public int getCpuUsage() {
        if(getCpuReaderListener() != null) {
            if (serviceConnection != null) {
                return serviceMonitor.getCPU();
            } else {
                return 0;
            }
        }
        return 0;
    }*/



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

    void getCpuUsage()
    {
        if(serviceMonitor!= null) {
            if(cpuDataSender != null) {
                cpuDataSender.drawCpuGraph(serviceMonitor.getCPU());
            }
            //return -1;
        }
        //return 0;
    }

    void getGlobalMemData()
    {
        if(serviceMonitor!= null) {
            if(memDataSender != null) {
                memDataSender.drawMemPieChart(serviceMonitor.getGlobalMem());
                memDataSender.drawMemBarChart(serviceMonitor.getGlobalMem());
            }
            //return -1;
        }
        //return 0;
    }

    //TODO ezt publicra tettem, hogy elérjem a fragmentből; gondolom erre is inkább interfész kellene
    public void displayProcesses()
    {
        if(serviceMonitor!= null) {
            if (iProcessDataSender != null) {
                iProcessDataSender.displayProcesses(serviceMonitor.getProcesses());
            }
        }
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
        //textView = findViewById(R.id.textview);
        initCoordinatorLayout();
        initToolBar();

        sharedPreferences = getSharedPreferences(getString(R.string.sharedpreference_name_key), MODE_PRIVATE);
        //sharedPreferences.edit().putInt(getString(R.string.readInterval), R.integer.default_readInterval);
        readInterval = sharedPreferences.getInt(getString(R.string.readInterval),getResources().getInteger(R.integer.default_readInterval));

        //toolbar = findViewById(R.id.activity_data_tool_bar);
        //setSupportActionBar(toolbar);

        //editText = findViewById(R.id.edittext);
        //btn = findViewById(R.id.btn);
        //btn.setText("execute");
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(new ShellExecutor().execute(editText.getText().toString()));
            }
        });*/
        //bindService(new Intent(this, ServiceMonitor.class), serviceConnection, BIND_AUTO_CREATE);
        //readThread.start();
        usageStatsPermissionHelper = new UsageStatsPermissionHelper(getApplicationContext());
        usageStatsPermissionHelper.getUsagePermissions();
        //getUsagePermissions();
        //Log.i(TAG, "onCreate: " + serviceMonitor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(serviceMonitor!= null) {
            //mHandler.removeCallbacks(mRunnable);
            //mHandler.post(mRunnable);
            startMonitor();
        }
        if(!(usageStatsPermissionHelper.getUsageStats())) {
            //ha kinyomjuk az engedélyt, de a stackben még megvan ez a activity
            //ctrl-p feldobja a parameterlistat
            showWarningDialog();
            //new WarningDialog().show(getFragmentManager(),"monitor_warning_dialog"));//(getSupportFragmentManager(), getString(R.string.dialog_warning_name_key));
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

        //initToolBar();
        //initTabLayout();
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
                return isRunning;

            case R.id.menu_actionSetInterval :
                //sharedPreferences.edit().putInt()
                //TODO elsőnek menübe akartam seekbart rakni, végül egy dialogba raktam, ebből kéne még kiszülni a return value-t, mert itt ahogy nézem a stop és start nem hívódik meg
                //https://stackoverflow.com/questions/4473940/android-best-practice-returning-values-from-a-dialog?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
                new IntervalReadDialog().show(getFragmentManager(), getString(R.string.dialog_readInterval_title_text));
                stopMonitor();
                readInterval = sharedPreferences.getInt(getString(R.string.readInterval),getResources().getInteger(R.integer.default_readInterval));
                Log.d(TAG, "onOptionsItemSelected: " + readInterval);
                startMonitor();
                break;

            case R.id.menu_actionRefresh :
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    //
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
        //mHandler.removeCallbacks(mRunnable);

    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, ServiceMonitor.class), serviceConnection, BIND_AUTO_CREATE);

        //startService(new Intent(this, ServiceMonitor.class));

    }

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
        //stopThread();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //TODO:: onRequestPermissionsResult
        //szerintem ez nem vonatkozik a system callokra, nem kell
        switch (requestCode) {
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }
}
