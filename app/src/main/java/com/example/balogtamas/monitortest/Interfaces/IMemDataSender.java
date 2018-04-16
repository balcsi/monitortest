package com.example.balogtamas.monitortest.Interfaces;

import java.util.ArrayList;

import data.data.apps.ProcessData;
import data.data.mem.GlobalMemData;
import data.data.mem.MemData;

public interface IMemDataSender {

    void drawMemGraph(MemData memData);
    void drawMemPieChart(GlobalMemData globalMemData);
    void drawMemBarChart(GlobalMemData globalMemData);
    //void displayProcesses(ArrayList<ProcessData> processDataArrayList);
}
