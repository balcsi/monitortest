package com.example.balogtamas.monitortest.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import com.example.balogtamas.monitortest.R;

public class CpuInfoDialog extends InfoDialog {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        return init(R.layout.dialog_cpu_info, R.id.dialog_cpu_info_description, getString(R.string.dialog_cpuinfo_title_text));//, 0);
    }
}
