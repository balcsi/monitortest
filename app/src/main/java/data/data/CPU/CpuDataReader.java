package data.data.CPU;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import java.util.concurrent.atomic.AtomicBoolean;

public class CpuDataReader {
    private static final String TAG = "CpuDataReader";
    private static final String mFile = "/proc/stat";

    private RandomAccessFile statFile;
    private CpuData total;
    private AtomicBoolean mFileOpenedOk = new AtomicBoolean();

    public CpuDataReader() {
        update();
    }





    private void openFile() throws FileNotFoundException {
        statFile = new RandomAccessFile(mFile, "r");
        mFileOpenedOk.set(true);
    }

    private void parseFile() {
        if (statFile != null) {
            try {
                statFile.seek(0);
                String cpuLine = "";
                cpuLine = statFile.readLine();
                String[] parts = cpuLine.split("[ ]+");
                createCpuData(parts);
            } catch (final IOException e) {
                Log.e(TAG, "Error parsing file: " + e);
            }
        }
    }

    private void createCpuData(String[] parts) {
        if (total == null) {
            total = new CpuData();
        }
        total.update(parts);
    }

    private void closeFile() throws IOException {
        if (statFile != null) {
            statFile.close();
        }
    }

    public int getCpuUsage() {
        int usage = 0;
        if (total != null) {
            usage = total.getUsage();
        }
        return usage;
    }

    public void update() {
        try {
            openFile();
            parseFile();
            closeFile();
        } catch (final FileNotFoundException e) {
            mFileOpenedOk.set(false);
            statFile = null;
            Log.e(TAG, "cannot open " + mFile + ":" + e);
        } catch (final IOException e) {
            Log.e(TAG, "cannot close " + mFile + ":" + e);
        }
    }


    class CpuData {

        private static final String TAG = "CpuData";

        private int mUsage;
        private long mLastTotal;
        private long mLastIdle;

        public CpuData() {
            mUsage = 0;
            mLastTotal = 0;
            mLastIdle = 0;
        }

        public int getUsage() {
            return mUsage;
        }

        public void update(final String[] statfile) {

            final long idle = Long.parseLong(statfile[4], 10);
            long total = 0;
            boolean first = true;
            for (final String part : statfile) {
                if (first) {
                    first = false;
                    continue;
                }
                total += Long.parseLong(part, 10);
            }
            final long deltaIdle = idle - mLastIdle;
            final long deltaTotal = total - mLastTotal;
            mUsage = (int) ((float) (deltaTotal - deltaIdle) / deltaTotal * 100);
            mLastTotal = total;
            mLastIdle = idle;

            Log.i(TAG, "CPU total=" + total + "; idle=" + idle + "; usage=" + mUsage);
        }
    }


}
