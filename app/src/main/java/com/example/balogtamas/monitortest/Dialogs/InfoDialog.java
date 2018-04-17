package com.example.balogtamas.monitortest.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.balogtamas.monitortest.R;

import java.util.ArrayList;

public abstract class InfoDialog extends DialogFragment {

    private static final String TAG = "InfoDialog";
    protected TextView description;
    protected int layoutResource;
    protected int descriptionID;

    protected AlertDialog init(int layoutResource, int descriptionID, String titleText)
    {
        this.layoutResource = layoutResource;
        this.descriptionID = descriptionID;
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialog = layoutInflater.inflate(layoutResource, null, false);
        description = dialog.findViewById(descriptionID);
        return returnAlertDialog(dialog, titleText);
    }

    AlertDialog returnAlertDialog(View dialog, String titleText)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(titleText)
                .setView(dialog)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return alertDialog;
    }

}
