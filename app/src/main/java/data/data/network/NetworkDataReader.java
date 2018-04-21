package data.data.network;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.balogtamas.monitortest.util.NetworkPermissionHelper;


import data.data.apps.ProcessNetData;

import static android.Manifest.permission.READ_PHONE_STATE;

public class NetworkDataReader {

    private static final String TAG = "NetworkDataReader";

    Context context;
    NetworkPermissionHelper networkPermissionHelper;

    GlobalNetworkData networkData;
        NetworkInfoReader networkInfoReader;
        NetworkStatsManager networkStatsManager;
        long wifi_rxBytes;
        long wifi_rxPackets;
        long mobile_rxBytes;
        long mobile_rxPackets;

    ProcessNetData processNetData = new ProcessNetData(0,0,0,0);


    public NetworkDataReader(Context context) {
        this.context = context;
        networkInfoReader = new NetworkInfoReader(this.context);
        networkInfoReader = new NetworkInfoReader(this.context);
        networkStatsManager = (NetworkStatsManager) this.context.getSystemService(Context.NETWORK_STATS_SERVICE);
        networkPermissionHelper = new NetworkPermissionHelper(context);
        update();
    }

    public void update()
    {
        doComputeGlobal();
    }

    public ProcessNetData getProcessNetData(int pid) {
        if(getProcessMobileUsage(pid) && getProcessWifiUsage(pid)) {
           return processNetData;
        }
        return new ProcessNetData(0,0,0,0);
    }

    boolean getProcessWifiUsage(int pid)
    {
        boolean success = false;
        if(networkPermissionHelper.hasPermission(READ_PHONE_STATE)) {
            NetworkStats networkStats = null;
            try {
                networkStats = networkStatsManager.queryDetailsForUid(
                        ConnectivityManager.TYPE_WIFI,
                        "",
                        0,
                        System.currentTimeMillis(),
                        pid);
                NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                networkStats.getNextBucket(bucket);
                this.processNetData.setWifi_rxBytes(bucket.getRxBytes());
                this.processNetData.setWifi_rxPackets(bucket.getRxPackets());
                success = true;
                networkStats.close();
            } catch (RemoteException e) {
                networkStats.close();
                return success;
            }
        }
        return success;
    }

    boolean getProcessMobileUsage(int pid)
    {
        boolean success = false;
        if(networkPermissionHelper.hasPermission(READ_PHONE_STATE)) {
            NetworkStats networkStats = null;
            try {
                networkStats = networkStatsManager.queryDetailsForUid(
                        ConnectivityManager.TYPE_MOBILE,
                        getSubID(ConnectivityManager.TYPE_MOBILE),
                        0,
                        System.currentTimeMillis(),
                        pid);
                NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                networkStats.getNextBucket(bucket);
                this.processNetData.setMobile_rxBytes(bucket.getRxBytes());
                this.processNetData.setMobile_rxPackets(bucket.getRxPackets());
                success = true;
                networkStats.close();
            } catch (RemoteException e) {
                networkStats.close();
                return success;
            }
        }
        return success;
    }



    void doComputeGlobal()
    {
        NetworkInfo networkInfo = networkInfoReader.getNetWorkInfo();
        WifiInfo wifiInfo = networkInfoReader.getWifiInfo();
        getGlobalWifiUsage();
        getGlobalMobileUsage();
        networkData = new GlobalNetworkData(wifiInfo,networkInfo,wifi_rxBytes,wifi_rxPackets, mobile_rxBytes, mobile_rxPackets);
    }

    void getGlobalWifiUsage()
    {
        if(networkPermissionHelper.hasPermission(READ_PHONE_STATE)) {
            NetworkStats.Bucket bucket = null;
            long now = System.currentTimeMillis();
            try {
                bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, "", 0, now);
                wifi_rxBytes = bucket.getRxBytes();
                wifi_rxPackets = bucket.getRxPackets();

            } catch (RemoteException e) {
                Log.e(TAG, "getGlobalWifiUsage: " + e.getMessage());
                wifi_rxBytes = 0;
                wifi_rxPackets = 0;
            } catch (SecurityException e) {
                Log.e(TAG, "getGlobalWifiUsage:" +e.getMessage());
            }
        }
    }

    private void getGlobalMobileUsage()
    {
        if(networkPermissionHelper.hasPermission(READ_PHONE_STATE))
        {
            NetworkStats.Bucket bucket = null;
            long now = System.currentTimeMillis();
            try {
                bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, getSubID(ConnectivityManager.TYPE_MOBILE), 0, now);
                mobile_rxPackets = bucket.getRxPackets();
                mobile_rxBytes = bucket.getRxBytes();
            } catch (RemoteException e) {
                mobile_rxPackets = 0;
                mobile_rxBytes = 0;
                Log.e(TAG, "getGlobalMobileUsage: " + e.getMessage() );
            }
            catch (SecurityException e){
                mobile_rxPackets = 0;
                mobile_rxBytes = 0;
                Log.e(TAG, "getGlobalMobileUsage: " + e.getMessage() );
            }
        } else {
            mobile_rxPackets = 0;
            mobile_rxBytes = 0;
        }

    }
    //fenti metódus le se fut, ha nincs meg a jogosultság
    @SuppressWarnings({"MissingPermission"})
    private String getSubID(int networkType)
    {
        if(ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                return tm.getSubscriberId();
        }
        return "";
    }



    public GlobalNetworkData getNetworkData() {
        return networkData;
    }
}
