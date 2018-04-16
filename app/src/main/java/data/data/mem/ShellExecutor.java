package data.data.mem;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ShellExecutor {

    private static final String TAG = "ShellExecutor";
    public String execute(String cmd) {
        StringBuffer sb = new StringBuffer();
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            Log.i(TAG, "execute: command" + cmd);
            while ((line = reader.readLine())!= null) {
                //Log.i(TAG, "line: " + line );
                sb.append(line + "\n");
            }

        } catch (Exception e) {
                e.printStackTrace();
        }
        return sb.toString();
    }

    public ArrayList<String> executeOutPutArray(String cmd) {
        StringBuffer sb = new StringBuffer();
        Process p;
        ArrayList<String> retval = new ArrayList<>();
        try {
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            Log.i(TAG, "execute: command" + cmd);
            while ((line = reader.readLine())!= null) {
                //Log.i(TAG, "line: " + line );
                retval.add(line);
                //sb.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval; //sb.toString();
    }

    public ShellExecutor() {
    }


}
