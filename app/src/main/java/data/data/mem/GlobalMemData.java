package data.data.mem;

import android.app.ActivityManager;
import android.content.Context;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.TreeMap;

public class GlobalMemData {

    private long memTotal, memFree,
            memBuffers,  memCached,  memSReclaimable;

    private long memUsed, memAvail, memThreshold;

    private ActivityManager activityManager;
    private ActivityManager.MemoryInfo memoryInfo;


    public GlobalMemData(Context context) {
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        memoryInfo = new ActivityManager.MemoryInfo();
    }

    public GlobalMemData() {
        this.memTotal = 0;
        this.memFree = 0;
        this.memBuffers = 0;
        this.memCached = 0;
        this.memSReclaimable = 0;
        this.memUsed = 0;
        this.memAvail = 0;
        this.memThreshold = 0;
    }

    public void doCompute() {
        memUsed = memTotal - memFree -(memCached+memBuffers+memSReclaimable)-memBuffers;
        activityManager.getMemoryInfo(memoryInfo);
        memAvail = memoryInfo.availMem/1024;
        memThreshold = memoryInfo.threshold/1024;
    }

    public HashMap<String, Long> getData()
    {
        HashMap<String, Long> retVal = new HashMap<>();
            retVal.put("memTotal", memTotal);
            retVal.put("memFree", memFree);
            retVal.put("memBuffers", memBuffers);
            retVal.put("memCached", memCached);
            retVal.put("memSReclaimable", memSReclaimable);
            retVal.put("memUsed", memUsed);
            retVal.put("memAvail", memAvail);
            retVal.put("memThreshold", memThreshold);
        return retVal;
        /*return new TreeMap<String, Long>() {
                memTotal, memFree, memBuffers,
            memCached, memSReclaimable, memUsed,
        memAvail, memThreshold
    };*/


    }

    public long getMemAvail() {return memAvail;}

    public long getMemTotal() {
        return memTotal;
    }

    public long getMemFree() {
        return memFree;
    }

    public long getMemBuffers() {
        return memBuffers;
    }

    public long getMemCached() {
        return memCached;
    }

    public long getMemSReclaimable() {
        return memSReclaimable;
    }

    public long getMemUsed() {
        return memUsed;
    }

    public void setMemTotal(long memTotal) {
        this.memTotal = memTotal;
    }

    public void setMemFree(long memFree) {
        this.memFree = memFree;
    }

    public void setMemBuffers(long memBuffers) {
        this.memBuffers = memBuffers;
    }

    public void setMemCached(long memCached) {
        this.memCached = memCached;
    }

    public void setMemSReclaimable(long memSReclaimable) {
        this.memSReclaimable = memSReclaimable;
    }

    public static GlobalMemData getBlankMemData()
    {
        return new GlobalMemData();
    }


    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            Class c = Class.forName(this.getClass().getName());
            Method m[] = c.getDeclaredMethods();
            Object oo;
            sb.append("\n");
            for (int i = 0; i < m.length; i++)
                if (m[i].getName().startsWith("get")) {
                    //Log.i(TAG, "method: " + m[i].getName());
                    oo = m[i].invoke(this, null);
                    sb.append(m[i].getName().substring(3) + ":"
                            + String.valueOf(oo) + "\n");
                }
        } catch (Throwable e) {
            System.err.println(e);
        }
        return sb.toString();
    }*/

    @Override
    public String toString() {
        return "GlobalMemData{" +
                "memTotal=" + memTotal +
                ", memFree=" + memFree +
                ", memBuffers=" + memBuffers +
                ", memCached=" + memCached +
                ", memSReclaimable=" + memSReclaimable +
                ", memUsed=" + memUsed +
                ", memAvail=" + memAvail +
                ", memThreshold=" + memThreshold +
                '}';
    }
}

