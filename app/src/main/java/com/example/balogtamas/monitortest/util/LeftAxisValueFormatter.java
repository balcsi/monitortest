package com.example.balogtamas.monitortest.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class LeftAxisValueFormatter implements IAxisValueFormatter {

    private DecimalFormat decimalFormat;

    public LeftAxisValueFormatter() {
        decimalFormat = new DecimalFormat("#,###,###");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return decimalFormat.format(value) + " kB";
    }
}
