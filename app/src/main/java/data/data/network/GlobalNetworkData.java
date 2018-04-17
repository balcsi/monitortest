package data.data.network;

import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;

public class GlobalNetworkData {
        long wifi_rxBytes;
        long wifi_rxPackets;
        long mobile_rxBytes;
        long mobile_rxPackets;
        WifiInfo wifiInfo;
        NetworkInfo networkInfo;


        public GlobalNetworkData(WifiInfo wifiInfo, NetworkInfo networkInfo, long wifi_rxBytes, long wifi_rxPackets,
                                 long mobile_rxBytes, long mobile_rxPackets) {
            this.wifiInfo = wifiInfo;
            this.networkInfo = networkInfo;
            this.wifi_rxBytes = wifi_rxBytes;
            this.wifi_rxPackets = wifi_rxPackets;
            this.mobile_rxBytes = mobile_rxBytes;
            this.mobile_rxPackets = mobile_rxPackets;
        }

        @Override
        public String toString() {
            return "GlobalNetworkData{" +
                    "wifi_rxBytes=" + wifi_rxBytes +
                    ", wifi_rxPackets=" + wifi_rxPackets +
                    ", mobile_rxBytes=" + mobile_rxBytes +
                    ", mobile_rxPackets=" + mobile_rxPackets +
                    ", wifiInfo=" + wifiInfo +
                    ", networkInfo=" + networkInfo +
                    '}';
        }
    }

