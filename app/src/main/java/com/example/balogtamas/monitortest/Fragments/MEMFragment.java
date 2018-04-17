package com.example.balogtamas.monitortest.Fragments;



import android.graphics.Color;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.example.balogtamas.monitortest.Activities.DataActivity;
import com.example.balogtamas.monitortest.Interfaces.IMemDataSender;

import com.example.balogtamas.monitortest.R;
import com.example.balogtamas.monitortest.util.LeftAxisValueFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.data.mem.GlobalMemData;
import data.data.mem.MemData;

public class MEMFragment extends Fragment {

    private static final String TAG = "MEMFragment";

    private PieChart pieChart;
    private BarChart barChart;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    /*private ProcessListAdapter adapter;
    List<ProcessData> processDataList = new ArrayList<>();
    ListView processListView;*/

    IMemDataSender memDataSender = new IMemDataSender() {
        @Override
        public void drawMemGraph(MemData memData) {
            drawGraph(memData);
        }

        @Override
        public void drawMemPieChart(GlobalMemData globalMemData) {
            drawPieChart(globalMemData);
        }

       /* @Override
        public void displayProcesses(ArrayList<ProcessData> processDataArrayList) {
            processDataList.clear();
            processDataList = processDataArrayList;
            setAdapter();
        }
*/
        @Override
        public void drawMemBarChart(GlobalMemData globalMemData) {
            setBarData(globalMemData);
        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mem, container, false);
        //TODO: ez lehet nem kell, illetve ha kell, tuti nem így adunk neki uid-t
        view.setId(getContext().getResources().getInteger(R.integer.MEMFragment_id));
        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        pieChart = view.findViewById(R.id.fragment_mem_pie_chart);
        barChart = view.findViewById(R.id.fragment_mem_bar_chart);
        setupPieChart();
        setupBarChart();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() instanceof DataActivity) {
            ((DataActivity)getActivity()).setMemDataSender(memDataSender);
            Log.d(TAG, "onActivityCreated: setMemDataSender called.");
        }
    }
    //TODO zoom out?
    void setupBarChart()
    {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        pieChart.setBackgroundColor(Color.WHITE);
        barChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.setDoubleTapToZoomEnabled(false);

        //XAxis xAxis = barChart.getXAxis();
        //xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        //xAxis.setDrawGridLines(false);
        //xAxis.setGranularity(1f); // only intervals of 1 day
        //xAxis.setLabelCount(7);
        //xAxis.setValueFormatter(xAxisFormatter);

        LeftAxisValueFormatter leftAxisValueFormatter = new LeftAxisValueFormatter();
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setValueFormatter(leftAxisValueFormatter);// this replaces setStartAtZero(true)

        barChart.getAxisRight().setEnabled(false);

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setEnabled(false);
        //l.setForm(Legend.LegendForm.SQUARE);
        //l.setFormSize(15f);
        //l.setTextSize(12f);
        //l.setXEntrySpace(1f);
        //l.setExtra(getColors(), getLabels());
    }

    void setBarData(GlobalMemData memdata)
    {
        ArrayList<BarEntry> values = new ArrayList();
        HashMap<String, Long> memvalues = memdata.getData();
        int i = 1;
        for (Map.Entry<String, Long> entry : memvalues.entrySet()) {
            if (!(entry.getKey() == "memTotal")) {
                values.add(new BarEntry(i++, entry.getValue()));
            }
        }

        BarDataSet set1;
        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
                    set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
                    set1.setValues(values);
                    set1.setColors(getColors());
                    barChart.getData().notifyDataChanged();
                    barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "" ); //"/proc/meminfo"
            set1.setDrawIcons(false);
            set1.setColors(getColors());

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);
            barChart.setData(data);
        }
    }

    int[] getColors()
    {
        return new int[] {
                getActivity().getColor(R.color.memTotal),
                getActivity().getColor(R.color.memFree),
                getActivity().getColor(R.color.memBuffers),
                getActivity().getColor(R.color.memCached),
                getActivity().getColor(R.color.memSReclaimable),
                getActivity().getColor(R.color.memUsed),
                getActivity().getColor(R.color.memAvail),
                getActivity().getColor(R.color.memThreshold)
        };
    }

    String[] getLabels () {
        return new String[]{
                "memTotal", "memFree", "memBuffers",
                "memCached", "memSReclaimable", "memUsed",
                "memAvail", "memThreshold"
        };
    }

    void setupPieChart()
    {
        pieChart.setBackgroundColor(Color.WHITE);
        moveOffScreen();

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(true);

        Description d = new Description();
        d.setText(getActivity().getString(R.string.graph_data_real_time));

        pieChart.setDescription(d);
        pieChart.setCenterTextTypeface(mTfLight);
        pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setMaxAngle(360f); // HALF CHART
        pieChart.setRotationAngle(360f);
        pieChart.setCenterTextOffset(0, -20);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);
        l.setXEntrySpace(3f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void moveOffScreen() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();  // deprecated
        int offset = (int)(height * 0.65); /* percent to move */

        RelativeLayout.LayoutParams rlParams =
                (RelativeLayout.LayoutParams)pieChart.getLayoutParams();
        rlParams.setMargins(0, 0, 0, -offset);
        pieChart.setLayoutParams(rlParams);
    }

    private void setPieData(GlobalMemData memdata) {
        ArrayList<PieEntry> values = new ArrayList<PieEntry>();
        HashMap<String, Long> memvalues = memdata.getData();
        for (Map.Entry<String, Long> entry : memvalues.entrySet()) {
            if (!(entry.getKey() == "memTotal")) {
                values.add(new PieEntry(entry.getValue(), entry.getKey()));
            }
        }

        PieDataSet dataSet = new PieDataSet(values, "");//"procfs: /proc/meminfo");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(getColors());

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("procfs data\nread by a BufferedReader"); //37
        //TODO spannablestring
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 11, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 11, s.length() - 14, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 11, s.length() - 14, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 11, s.length() - 14, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 11, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), s.length() - 14, s.length(), 0);
        return s;
    }

    private void drawGraph(MemData memData) {
        Log.i(TAG, "mem drawGraph: called, usage: " + 0);
    }

    private void drawPieChart(GlobalMemData memdata) {
        setPieData(memdata);
    }
}
