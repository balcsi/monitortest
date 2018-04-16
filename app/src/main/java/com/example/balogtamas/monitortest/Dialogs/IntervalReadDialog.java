package com.example.balogtamas.monitortest.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
//import android.widget.SeekBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.balogtamas.monitortest.R;
import com.example.balogtamas.monitortest.util.FloatSeekBar;

import static android.content.Context.MODE_PRIVATE;


public class IntervalReadDialog extends DialogFragment {


    private static final String TAG = "IntervalReadDialog";
    TextView label;
    int progress;
    final int _progress = 500;
    SharedPreferences sharedPreferences;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialog = layoutInflater.inflate(R.layout.dialog_readinterval, null, false);
        label = dialog.findViewById(R.id.dialog_readinterval_seekbar_label);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.sharedpreference_name_key), MODE_PRIVATE);
        progress = sharedPreferences.getInt(getString(R.string.readInterval), getActivity().getResources().getInteger(R.integer.default_readInterval));
        label.setText(Integer.toString(progress) + " ms");
        AppCompatSeekBar seekBar = dialog.findViewById(R.id.dialog_readinterval_seekbar);
            seekBar.setProgress((progress/_progress)-1);
            seekBar.setMax(9);
            //seekBar.setKeyProgressIncrement(500);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                label.setText(Integer.toString((i+1)*_progress) + " ms");
                Log.d(TAG, "sharedpref " + sharedPreferences.getInt(getString(R.string.readInterval), R.integer.default_readInterval));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int i = seekBar.getProgress();
                if(i > 0 &&  i != (progress/_progress)) {
                    progress = (i+1)*_progress;
                } else {
                    //i=1;
                    progress = 500;
                }
                Log.d(TAG, "onStopTrackingTouch: i = " + i);
                Log.d(TAG, "onStopTrackingTouch: progress = " + progress);
            }
        });
        //TODO::nincs warningdialog.Builder ?
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.dialog_readInterval_title_text))
                .setView(dialog)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPreferences.edit().putInt(getString(R.string.readInterval), progress).apply();
                        Log.d(TAG, "onClick: " + sharedPreferences.getInt(getString(R.string.readInterval), getResources().getInteger(R.integer.default_readInterval)));
                        //Log.d(TAG, "onClick: " + getString(R.string.readInterval));
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return alertDialog;
    }
}
