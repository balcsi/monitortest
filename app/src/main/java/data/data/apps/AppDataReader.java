package data.data.apps;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import data.data.apps.AppData;
import data.data.mem.MemData;
import data.data.network.NetworkDataReader;


public class AppDataReader {

    private UsageStatsManager usageStatsManager;
    private RunningProcesses runningProcesses;
    private ActivityManager activityManager;
    private PackageManager packageManager;
    private NetworkDataReader networkDataReader;

    @SuppressWarnings("WrongConstant")
    public AppDataReader(Context context) {
        runningProcesses = new RunningProcesses();
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        packageManager = context.getApplicationContext().getPackageManager();
        networkDataReader = new NetworkDataReader(context);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            usageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");
        } else {
            usageStatsManager = null;
        }

    }

    public ArrayList<AppData> getAppData()
    {
        ArrayList<AppData> data = new ArrayList<>();
        long now = System.currentTimeMillis();
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000000, now);
        if(usageStatsList != null && usageStatsList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (final UsageStats usageStats : usageStatsList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!mySortedMap.isEmpty()) {
                runningProcesses.getRunningApps();
                for (Map.Entry<Long, UsageStats> entry : mySortedMap.entrySet()) {
                    String packageName = entry.getValue().getPackageName();
                    int pid = runningProcesses.getPIDfromName(packageName);
                    data.add(new AppData(pid, packageName, getName(packageName)));    
                    
                    //new ProcessData(new AppData(pid, packageName, getName(packageName)), MemData.createMemData(pid, activityManager));
                    
                }
                //UsageStats currentApp = mySortedMap.get(mySortedMap.lastKey());
                //String packageName = currentApp.getPackageName();
                //processData.getRunningApps();
                //int pid = processData.getPIDfromName(packageName);
                //data = new AppData(pid, packageName, getName(packageName));
            } else {
                //data = new AppData(-1, "???", "???");
                data.add(new AppData(-1, "???", "???"));
            }
        } else {
            //data = new AppData(-1, "???", "???");
            data.add(new AppData(-1, "???", "???"));
        }
        return data;
    }

    //foreground
    public ArrayList<ProcessData> getProcessData()
    {
        long now = System.currentTimeMillis();
        ArrayList<ProcessData> data = new ArrayList<>();
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000000, now);
        if(usageStatsList != null && usageStatsList.size() > 0) {
            runningProcesses.getRunningApps();
            for (UsageStats usageStats : usageStatsList) {
                String packageName = usageStats.getPackageName();
                String processName = getName(packageName);
                int pid = runningProcesses.getPIDfromName(packageName);
                //TODO ezt gyakorlatban nem tudom tesztelni, 23-as api kell hozzá minimum, de 23+ api kell hogy az emulátor tudjon wifi/mobilnetet emulálni; 26-on meg csak a saját appom PID-jét látom, ami nem használ internetet :D
                ProcessNetData processNetData = networkDataReader.getProcessNetData(pid);
                if(pid > -1) {
                    data.add(new ProcessData(new AppData(pid, packageName, processName), MemData.createMemData(pid, activityManager), processNetData));
                }
            }
        } else {
            data.add(ProcessData.returnBlank(-1));
        }
        return data;
    }

    String getName(String packageName){
        CharSequence appName;
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
         return applicationInfo == null ?  "???" : ((CharSequence)(packageManager.getApplicationLabel(applicationInfo))).toString();
    }





}
