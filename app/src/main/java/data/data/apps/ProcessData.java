package data.data.apps;

import data.data.mem.MemData;

public class ProcessData {

    private AppData appData;
    private MemData memData;
    private ProcessNetData processNetData;

    public ProcessData(AppData appData, MemData memData, ProcessNetData processNetData) {
        this.appData = appData;
        this.memData = memData;
        this.processNetData = processNetData;
    }

    public AppData getAppData() {
        return appData;
    }

    public MemData getMemData() {
        return memData;
    }

    public ProcessNetData getProcessNetData() {
        return processNetData;
    }

    public static ProcessData returnBlank(final int pid) {
        AppData appData = new AppData(-1, "???", "???");
        MemData memData = MemData.returnBlank(pid);
        ProcessNetData processNetData = new ProcessNetData(0,0,0,0);
        return new ProcessData(appData, memData, processNetData);
    }

    @Override
    public String toString() {
        return "ProcessData{" +
                "appData=" + appData +
                ", memData=" + memData +
                ", processNetData=" + processNetData +
                '}';
    }
}
