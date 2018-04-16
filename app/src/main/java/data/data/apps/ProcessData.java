package data.data.apps;

import data.data.mem.MemData;

public class ProcessData {

    private AppData appData;
    private MemData memData;

    public ProcessData(AppData appData, MemData memData) {
        this.appData = appData;
        this.memData = memData;
    }

    public AppData getAppData() {
        return appData;
    }

    public MemData getMemData() {
        return memData;
    }

    public static ProcessData returnBlank(final int pid) {
        AppData appData = new AppData(-1, "???", "???");
        MemData memData = MemData.returnBlank(pid);
        return new ProcessData(appData, memData);
    }

    @Override
    public String toString() {
        return "ProcessData{" +
                "appData=" + appData +
                ", memData=" + memData +
                '}';
    }


}
