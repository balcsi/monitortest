package data.data.apps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import data.data.mem.ShellExecutor;

public class RunningProcesses {

    private Map<Integer,String> PidToName = new HashMap<>();
    private Map<String,Integer> NameToPid = new HashMap<>();
    ShellExecutor cmd = new ShellExecutor();

    public void getRunningApps()
    {
        //PidToName.clear();
        NameToPid.clear();
        String[] temp;
        boolean first = true;

        ArrayList<String> output =  cmd.executeOutPutArray("ps");
        int pid; String name;
        for (String line : output) {
            if(first) {
                first = false;
            } else {
                temp = Pattern.compile("\\s+").split(line);
                pid = Integer.valueOf(temp[1]);
                name = temp[temp.length - 1].split(":")[0];
                //name = temp[temp.length - 1];// ld.: googlequicksearchbox:interactor <--> googlequicksearchbox:search
                //PidToName.put(pid, name);
                NameToPid.put(name, pid);
            }
        }
    }
    public int getPIDfromName(String name)
    {
        //int nem lehet null
        Integer retval = NameToPid.get(name);
        if(retval != null) {
            return retval;
        } else {
            return -1;
        }

    }
}
