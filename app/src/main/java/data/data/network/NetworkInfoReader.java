package data.data.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkInfoReader {
    private ConnectivityManager connectivityManager;
    private Context context;
    private WifiManager wifiManager;

    public NetworkInfoReader(Context context) {
        this.context = context.getApplicationContext();
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public WifiInfo getWifiInfo() {
        if(wifiManager != null) {
            return wifiManager.getConnectionInfo();
        }
        return null;
    }

    public NetworkInfo getNetWorkInfo(){
        if(connectivityManager != null) {
            return connectivityManager.getActiveNetworkInfo();
        }
        return null;
    }
}
