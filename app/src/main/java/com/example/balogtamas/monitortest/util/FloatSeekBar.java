package com.example.balogtamas.monitortest.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatSeekBar;

import com.example.balogtamas.monitortest.R;


//végül osztogattam inkább @see dialogs/intervalreaddialog
public class FloatSeekBar extends AppCompatSeekBar {

    private float max = 5.0f;
    private float min = 0.5f;

    public FloatSeekBar(Context context) {
        super(context);
    }

    public FloatSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttrs(attrs);
    }

    public FloatSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyAttrs(attrs);
    }

    public float getValue() {
        return (max - min) * ((float) getProgress() / (float) getMax()) + min;
    }

    public void setValue(float value) {
        setProgress((int) ((value - min) / (max - min) * getMax()));
    }

    private void applyAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AppCompatSeekBar);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.FloatSeekBar_floatMax:
                    this.max = a.getFloat(attr, 1.0f);
                    break;
                case R.styleable.FloatSeekBar_floatMin:
                    this.min = a.getFloat(attr, 0.0f);
                    break;
            }
        }
        a.recycle();
    }


}
