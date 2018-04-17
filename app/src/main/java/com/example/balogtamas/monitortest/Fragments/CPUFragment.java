package com.example.balogtamas.monitortest.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.balogtamas.monitortest.Activities.DataActivity;
import com.example.balogtamas.monitortest.Interfaces.ICpuDataSender;
import com.example.balogtamas.monitortest.R;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


public class CPUFragment extends Fragment {


    private static final String TAG = "CPUFragment";

    LineChart cubicChart;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    //debug
    private Thread thread;

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (CPUReaderListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement CPUReaderListener");
        }
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cpu, container, false);
        view.setId(getContext().getResources().getInteger(R.integer.CPUFragment_id));
        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        cubicChart = view.findViewById(R.id.fragment_cpu_chart1);
        setupLineChart(cubicChart);
        //feedMultiple();
        return view;
    }

    ICpuDataSender cpuDataSender = new ICpuDataSender() {
        @Override
        public void drawCpuGraph(int cpuUsage) {
            drawGraph(cpuUsage);
        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() instanceof DataActivity) {
            ((DataActivity)getActivity()).setCpuDataSender(cpuDataSender);
            Log.i(TAG, "onActivityCreated: setCpuDataSender called.");
        }
    }

    private void drawGraph(int cpuUsage) {
        Log.i(TAG, "cpu drawGraph: called, usage: " + cpuUsage);
        LineData data = cubicChart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            //ILineDataSet botSet = data.getDataSetByIndex(1);
            if (set == null) {
                set = createSet();
                //if(botSet == null) {
                    //createBotSet();
                //}
                data.addDataSet(set);
                //data.addDataSet(botSet);
                //cubicChart.setData(data);
            }
            data.addEntry(new Entry(set.getEntryCount(), (float) cpuUsage), 0);
            //data.addEntry(new Entry(botSet.getEntryCount(), (float) 0), 1);
            data.notifyDataChanged();
            cubicChart.notifyDataSetChanged();
            cubicChart.setVisibleXRangeMaximum(120);
            cubicChart.moveViewToX(data.getEntryCount());
        }
    }


    private void setupLineChart(LineChart cubicChart)
    {
        cubicChart.setViewPortOffsets(0, 0, 0, 0);
        cubicChart.getDescription().setEnabled(true);
        Description desc = new Description();
            desc.setText(getActivity().getString(R.string.graph_data_real_time));
        cubicChart.setDescription(desc);
        cubicChart.setDrawBorders(true);
        // enable touch gestures
        //cubicChart.setTouchEnabled(true);

        cubicChart.setDragEnabled(true);
        cubicChart.setScaleEnabled(true);
        cubicChart.setDrawGridBackground(false);

        cubicChart.setPinchZoom(true);
        cubicChart.setMaxHighlightDistance(300);

        //cubicChart.setBackgroundColor(getActivity().getColor(R.color.colorGrey));

        LineData data = new LineData();

        cubicChart.setData(data);
        Legend l = cubicChart.getLegend();
            l.setForm(Legend.LegendForm.LINE);
            l.setTypeface(mTfLight);
            l.setTextColor(getActivity().getColor(R.color.colorPrimaryDark));

        XAxis xl = cubicChart.getXAxis();
            xl.setTypeface(mTfLight);
            xl.setTextColor(Color.BLACK);
            xl.setDrawGridLines(false);
            xl.setAvoidFirstLastClipping(true);
            xl.setEnabled(true);

        YAxis leftAxis = cubicChart.getAxisLeft();
            leftAxis.setTypeface(mTfLight);
            leftAxis.setLabelCount(6, false);
            leftAxis.setTextColor(Color.BLACK);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
            leftAxis.setDrawGridLines(false);
            leftAxis.setAxisLineColor(Color.BLACK);
    }
    //DEBUG
    private void addEntry(LineChart cubicChart) {
        LineData data = cubicChart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            //ILineDataSet bot_Set = data.getDataSetByIndex(1);
            if (set == null) {
                set = createSet();
                //if(bot_Set == null) {
                    //bot_Set = createBotSet();
                //}
                data.addDataSet(set);
                //data.addDataSet(bot_Set);
            }
            data.addEntry(new Entry( set.getEntryCount(), (float) (Math.random() * 40) + 30f, 10000),  0);
            data.notifyDataChanged();
            cubicChart.notifyDataSetChanged();
            cubicChart.setVisibleXRangeMaximum(120);
            cubicChart.moveViewToX(data.getEntryCount());
        }
    }
    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "cpu usage");
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        set.setDrawCircles(false);
        set.setLineWidth(1.8f);
        set.setCircleRadius(4f);
        set.setCircleColor(getActivity().getColor(R.color.colorPrimaryDark));
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setColor(getActivity().getColor(R.color.colorPrimaryDark));
        set.setFillColor(getActivity().getColor(R.color.colorPrimaryDark));
        set.setFillAlpha(100);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setDrawValues(false);
        set.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });
        return set;
    }

   /* private LineDataSet createBotSet() {
        LineDataSet set = new LineDataSet(null, "");
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        set.setDrawCircles(false);
        set.setLineWidth(1.8f);
        set.setCircleRadius(4f);
        set.setFillAlpha(0);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });
        return set;
    }*/

    //DEBUG
    private void feedMultiple() {
        if (thread != null) {
            thread.interrupt();
        }
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                addEntry(cubicChart);
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    // Don't generate garbage runnables inside the loop.
                    getActivity().runOnUiThread(runnable);
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }










}
