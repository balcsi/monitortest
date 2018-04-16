package data.data.apps;

public class AppData {

    private int mPID;
    private String mPackage;
    private String mName;

    public AppData(int mPID, String mPackage, String mName) {
        this.mPID = mPID;
        this.mPackage = mPackage;
        this.mName = mName;
    }

    public int getmPID() {
        return mPID;
    }

    public String getmPackage() {
        return mPackage;
    }

    public String getmName() {
        return mName;
    }


    @Override
    public String toString() {
        return "AppData{" +
                "mPID=" + mPID +
                ", mPackage='" + mPackage + '\'' +
                ", mName='" + mName + '\'' +
                '}'+ '\n';
        //}

        //return retVal;
    }

    public String toStringView() {
         return mPID + " " +'\'' + mName + " " + '\'' + mPackage;
    }
}


