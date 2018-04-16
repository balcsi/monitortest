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

public class WarningDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialog = layoutInflater.inflate(R.layout.dialog_warning, null, false);
        TextView desc = dialog.findViewById(R.id.dialog_warning_desc);

        //TODO::nincs warningdialog.Builder ?
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.dialog_warning_title_text))
                .setView(dialog)
                .setNeutralButton("Értem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return alertDialog;
    }
}
