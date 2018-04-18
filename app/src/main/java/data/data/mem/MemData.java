package data.data.mem;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by balog.tamas on 2018.04.02..
 */

public class MemData
{

    private static final String TAG = "MemData";

    private final int dalvikPrivateClean;
    private final int dalvikPrivateDirty;
    private final int dalvikPss;
    private final int dalvikSharedClean;
    private final int dalvikSharedDirty;
    private final int dalvikSwappablePss;
    private final int dalvikSwappedOut;
    private final int nativePrivateClean;
    private final int nativePrivateDirty;
    private final int nativePss;
    private final int nativeSharedClean;
    private final int nativeSharedDirty;
    private final int nativeSwappablePss;
    private final int nativeSwappedOut;
    private final int otherPrivateClean;
    private final int otherPrivateDirty;
    private final int otherPss;
    private final int otherSharedClean;
    private final int otherSharedDirty;
    private final int otherSwappablePss;
    private final int otherSwappedOut;

    public int getDalvikPrivateClean() {
        return dalvikPrivateClean;
    }

    public int getDalvikPrivateDirty() {
        return dalvikPrivateDirty;
    }

    public int getDalvikPss() {
        return dalvikPss;
    }

    public int getDalvikSharedClean() {
        return dalvikSharedClean;
    }

    public int getDalvikSharedDirty() {
        return dalvikSharedDirty;
    }

    public int getDalvikSwappablePss() {
        return dalvikSwappablePss;
    }

    public int getDalvikSwappedOut() {
        return dalvikSwappedOut;
    }

    public int getNativePrivateClean() {
        return nativePrivateClean;
    }

    public int getNativePrivateDirty() {
        return nativePrivateDirty;
    }

    public int getNativePss() {
        return nativePss;
    }

    public int getNativeSharedClean() {
        return nativeSharedClean;
    }

    public int getNativeSharedDirty() {
        return nativeSharedDirty;
    }

    public int getNativeSwappablePss() {
        return nativeSwappablePss;
    }

    public int getNativeSwappedOut() {
        return nativeSwappedOut;
    }

    public int getOtherPrivateClean() {
        return otherPrivateClean;
    }

    public int getOtherPrivateDirty() {
        return otherPrivateDirty;
    }

    public int getOtherPss() {
        return otherPss;
    }

    public int getOtherSharedClean() {
        return otherSharedClean;
    }

    public int getOtherSharedDirty() {
        return otherSharedDirty;
    }

   public int getOtherSwappablePss() {
        return otherSwappablePss;
    }

    public int getOtherSwappedOut() {
        return otherSwappedOut;
    }

    private final int pid;

    private final static Set<String> sReflectionErrorKeys = new HashSet<>();

    public static MemData createMemData(int pid, ActivityManager activityManager) {
        Debug.MemoryInfo mi = activityManager.getProcessMemoryInfo(new int[]{pid})[0];
        MemData memdata = new MemData(pid, mi);
        return memdata;
    }

    public MemData(final int pid, final Debug.MemoryInfo mi) {
        this.pid = pid;

        dalvikPrivateDirty = mi.dalvikPrivateDirty;
        dalvikPss = mi.dalvikPss;
        dalvikSharedDirty = mi.dalvikSharedDirty;

        nativePrivateDirty = mi.nativePrivateDirty;
        nativePss = mi.nativePss;
        nativeSharedDirty = mi.nativeSharedDirty;

        otherPrivateDirty = mi.otherPrivateDirty;
        otherPss = mi.otherPss;
        otherSharedDirty = mi.otherSharedDirty;


        dalvikPrivateClean = getIntReflectively(mi, "dalvikPrivateClean");
        dalvikSharedClean = getIntReflectively(mi, "dalvikSharedClean");
        dalvikSwappablePss = getIntReflectively(mi, "dalvikSwappablePss");
        dalvikSwappedOut = getIntReflectively(mi, "dalvikSwappedOut");

        nativePrivateClean = getIntReflectively(mi, "nativePrivateClean");
        nativeSharedClean = getIntReflectively(mi, "nativeSharedClean");
        nativeSwappablePss = getIntReflectively(mi, "nativeSwappablePss");
        nativeSwappedOut = getIntReflectively(mi, "nativeSwappedOut");

        otherPrivateClean = getIntReflectively(mi, "otherPrivateClean");
        otherSharedClean = getIntReflectively(mi, "otherSharedClean");
        otherSwappablePss = getIntReflectively(mi, "otherSwappablePss");
        otherSwappedOut = getIntReflectively(mi, "otherSwappedOut");

    }

   private static int getIntReflectively(final Debug.MemoryInfo mi, final String name) {
        if (!sReflectionErrorKeys.contains(name)) {
            if (mi != null) {
                final Class<?> clazz = mi.getClass();
                final Field field;
                try {
                    field = clazz.getField(name);
                    return field.getInt(mi);
                } catch (final NoSuchFieldException e) {
                    sReflectionErrorKeys.add(name);
                    e.printStackTrace();
                } catch (final IllegalAccessException e) {
                    sReflectionErrorKeys.add(name);
                    e.printStackTrace();
                } catch (final IllegalArgumentException e) {
                    sReflectionErrorKeys.add(name);
                    e.printStackTrace();
                } catch (final NullPointerException e) {
                    sReflectionErrorKeys.add(name);
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    private MemData(int pid) {
        this.pid = pid;
        dalvikPrivateClean = 0;
        dalvikPrivateDirty = 0;
        dalvikPss = 0;
        dalvikSharedClean = 0;
        dalvikSharedDirty = 0;
        dalvikSwappablePss = 0;
        dalvikSwappedOut = 0;

        nativePrivateClean = 0;
        nativePrivateDirty = 0;
        nativePss = 0;
        nativeSharedClean = 0;
        nativeSharedDirty = 0;
        nativeSwappablePss = 0;
        nativeSwappedOut = 0;

        otherPrivateClean = 0;
        otherPrivateDirty = 0;
        otherPss = 0;
        otherSharedClean = 0;
        otherSharedDirty = 0;
        otherSwappablePss = 0;
        otherSwappedOut = 0;
    }


    public static MemData returnBlank(final int pid) {
        return new MemData(pid);
    }

    //TODO: 27-es API-n valahogy máshogy kell, igazából lényegtelen
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
                            + String.valueOf(oo) + " kB" +"\n");
                }
        } catch (Throwable e) {
            System.err.println(e);
        }
        return sb.toString();
    }*/

    @Override
    public String toString() {
        return  "memory usage: " + '\n' +
                '\t' + "dalvikPrivateClean: " + dalvikPrivateClean + " kB" + '\n' +
                '\t' + "dalvikPrivateDirty: " + dalvikPrivateDirty + " kB" + '\n' +
                '\t' + "dalvikPss: " + dalvikPss + " kB" + '\n' +
                '\t' + "dalvikSharedClean: " + dalvikSharedClean + " kB" + '\n' +
                '\t' + "dalvikSharedDirty: " + dalvikSharedDirty + " kB" + '\n' +
                '\t' + "dalvikSwappablePss: " + dalvikSwappablePss + " kB" + '\n' +
                '\t' + "dalvikSwappedOut: " + dalvikSwappedOut + " kB" + '\n' +
                '\t' + "nativePrivateClean: " + nativePrivateClean + " kB" + '\n' +
                '\t' + "nativePrivateDirty: " + nativePrivateDirty + " kB" + '\n' +
                '\t' + "nativePss: " + nativePss + " kB" + '\n' +
                '\t' + "nativeSharedClean: " + nativeSharedClean + " kB" + '\n' +
                '\t' + "nativeSharedDirty: " + nativeSharedDirty + " kB" + '\n' +
                '\t' + "nativeSwappablePss: " + nativeSwappablePss + " kB" + '\n' +
                '\t' + "nativeSwappedOut: " + nativeSwappedOut + " kB" + '\n' +
                '\t' + "otherPrivateClean: " + otherPrivateClean + " kB" + '\n' +
                '\t' + "otherPrivateDirty: " + otherPrivateDirty + " kB" + '\n' +
                '\t' + "otherPss: " + otherPss + " kB" + '\n' +
                '\t' + "otherSharedClean: " + otherSharedClean + " kB" + '\n' +
                '\t' + "otherSharedDirty: " + otherSharedDirty + " kB" + '\n' +
                '\t' + "otherSwappablePss: " + otherSwappablePss + " kB" + '\n' +
                '\t' + "otherSwappedOut: " + otherSwappedOut + " kB"
                ;
    }
}