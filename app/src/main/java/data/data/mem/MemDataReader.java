package data.data.mem;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug.MemoryInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by balog.tamas on 2018.04.02..
 */

public class MemDataReader {

    private static final String TAG = "MemDataReader";
    
    ActivityManager activityManager;
    MemData memData;
    GlobalMemData globalMemData;
    Context context;

    private static final String mFile = "/proc/meminfo";


    private void getGlobalMem() {

        String s;
        boolean header = true;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(mFile));
            s = bufferedReader.readLine();
            globalMemData = new GlobalMemData(context);
            while(s != null) {
                if(header && s.startsWith("MemTotal:")) {
                    globalMemData.setMemTotal(Integer.parseInt(s.split("[ ]+", 3)[1]));
                    header = false;
                } else if (s.startsWith("MemFree:")) {
                    globalMemData.setMemFree(Integer.parseInt(s.split("[ ]+",3)[1]));
                } else if (s.startsWith("Buffers")) {
                    globalMemData.setMemBuffers(Integer.parseInt(s.split("[ ]+",3)[1]));
                } else if (s.startsWith("Cached:")) {
                    globalMemData.setMemCached(Integer.parseInt(s.split("[ ]+", 3)[1]));
                } else if (s.startsWith("SReclaimable:")) {
                    globalMemData.setMemSReclaimable(Integer.parseInt(s.split("[ ]+",3)[1]));
                    break;
                }
                s = bufferedReader.readLine();
            }
            globalMemData.doCompute();
            bufferedReader.close();
        } catch (IOException e) {
            Log.e(TAG, "parseFile: " +  e.toString());
            globalMemData = null;
        }
    }
    
    
    

    public MemDataReader(Context context) {
        this.context = context;
        activityManager = (ActivityManager) this.context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public void update(int id) {
        if(id > 0) {
            MemoryInfo mi = activityManager.getProcessMemoryInfo(new int[]{id})[0];
            memData = new MemData(id, mi);
        } else {
            memData = MemData.returnBlank(id);
        }
    }

    public MemData getData(int id)
    {
        MemData memData;
        if(id >0) {
            MemoryInfo mi = activityManager.getProcessMemoryInfo(new int[]{id})[0];
            memData = new MemData(id, mi);
        } else {
            memData = MemData.returnBlank(id);
        }
        return memData;
    }

    public ArrayList<MemData> updateAll() {
        ArrayList<Integer> pids = new ArrayList<>();
        for (ActivityManager.RunningAppProcessInfo info : activityManager.getRunningAppProcesses()) {
            pids.add(info.pid);
        }
        ArrayList<MemData> retval = new ArrayList<>();
        for (int pid : pids) {
            retval.add(getData(pid));
        }
        return retval;
    }

    public MemData getMemData() {

        return memData;
    }

    public GlobalMemData getTotalMemData() {
            getGlobalMem();
            if(globalMemData != null) {
                return globalMemData;
            }
            return GlobalMemData.getBlankMemData();


    }


}
