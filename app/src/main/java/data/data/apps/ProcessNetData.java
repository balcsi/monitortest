package data.data.apps;

public class ProcessNetData{
    long wifi_rxBytes;
    long wifi_rxPackets;
    long mobile_rxBytes;
    long mobile_rxPackets;

    public ProcessNetData(long wifi_rxBytes, long wifi_rxPackets, long mobile_rxBytes, long mobile_rxPackets) {
        this.wifi_rxBytes = wifi_rxBytes;
        this.wifi_rxPackets = wifi_rxPackets;
        this.mobile_rxBytes = mobile_rxBytes;
        this.mobile_rxPackets = mobile_rxPackets;
    }

    boolean isUsingWifi()
    {
        return (wifi_rxBytes!=0 && wifi_rxPackets!=0);
    }

    boolean isUsingMobile()
    {
        return  (mobile_rxBytes!=0 && mobile_rxPackets!=0);
    }

    public String toStringNotEmpty()
    {
        String retVal = "";
        if(isUsingWifi() || isUsingMobile()) {
            retVal = toString();
        }
        return retVal;
    }

    @Override
    public String toString() {
        return  "wifi usage: " + '\n' +
                + '\t' + "wifi_rxBytes=" + wifi_rxBytes +
                + '\t' + ", wifi_rxPackets=" + wifi_rxPackets +
                "mobile usage: " + '\n' +
                + '\t' + ", mobile_rxBytes=" + mobile_rxBytes +
                + '\t' + ", mobile_rxPackets=" + mobile_rxPackets + '\n';
    }

    public long getWifi_rxBytes() {
        return wifi_rxBytes;
    }

    public long getWifi_rxPackets() {
        return wifi_rxPackets;
    }

    public long getMobile_rxBytes() {
        return mobile_rxBytes;
    }

    public long getMobile_rxPackets() {
        return mobile_rxPackets;
    }


    public void setWifi_rxBytes(long wifi_rxBytes) {
        this.wifi_rxBytes = wifi_rxBytes;
    }

    public void setWifi_rxPackets(long wifi_rxPackets) {
        this.wifi_rxPackets = wifi_rxPackets;
    }

    public void setMobile_rxBytes(long mobile_rxBytes) {
        this.mobile_rxBytes = mobile_rxBytes;
    }

    public void setMobile_rxPackets(long mobile_rxPackets) {
        this.mobile_rxPackets = mobile_rxPackets;
    }
}
