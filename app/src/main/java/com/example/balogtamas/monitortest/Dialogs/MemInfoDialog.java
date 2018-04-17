package com.example.balogtamas.monitortest.Dialogs;

import android.app.Dialog;
import android.os.Bundle;

import com.example.balogtamas.monitortest.R;

public class MemInfoDialog extends InfoDialog {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        return init(R.layout.dialog_mem_info, R.id.dialog_mem_info_description, getString(R.string.dialog_meminfo_title_text));
    }
}
