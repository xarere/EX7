package com.example.ex7demo;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GravityActivity extends AppCompatActivity implements SensorEventListener {
    @BindView(R.id.lc_gravity)
    LineChart lc_main_gravity;
    private int timeIndex=0;
    private SensorManager sensorManager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        ButterKnife.bind(this);
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        lc_main_gravity.setTouchEnabled(false);
        lc_main_gravity.getXAxis().setEnabled(true);
        lc_main_gravity.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lc_main_gravity.getAxisLeft().setEnabled(true);
        lc_main_gravity.getAxisRight().setEnabled(false);
        lc_main_gravity.getAxisLeft().setAxisMaximum(10);
        lc_main_gravity.getAxisLeft().setAxisMinimum(-10);
        List<Entry> XEntries=new ArrayList<>();
        LineDataSet XDataSet=new LineDataSet(XEntries,"X");
        List<Entry> YEntries=new ArrayList<>();
        LineDataSet YDataSet=new LineDataSet(YEntries,"Y");
        List<Entry> ZEntries=new ArrayList<>();
        LineDataSet ZDataSet=new LineDataSet(ZEntries,"Z");
        LineData lineData=null;
        XDataSet.setColors(Color.rgb(255,0,0));
        YDataSet.setColors(Color.rgb(0,255,0));
        ZDataSet.setColors(Color.rgb(0,0,255));
        XDataSet.setDrawCircles(false);
        YDataSet.setDrawCircles(false);
        ZDataSet.setDrawCircles(false);
        lineData=new LineData();
        lineData.addDataSet(XDataSet);
        lineData.addDataSet(YDataSet);
        lineData.addDataSet(ZDataSet);
        lc_main_gravity.setData(lineData);
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values= sensorEvent.values;
        LineData lineData=lc_main_gravity.getLineData();
        if(lineData!=null){
            LineDataSet XDataSet= (LineDataSet) lineData.getDataSetByLabel("X",true);
            LineDataSet YDataSet=(LineDataSet) lineData.getDataSetByLabel("Y",true);
            LineDataSet ZDataSet=(LineDataSet) lineData.getDataSetByLabel("Z",true);
            if(XDataSet!=null&&YDataSet!=null&&ZDataSet!=null){
                if(XDataSet.getEntryCount()>200){ XDataSet.removeFirst();
                YDataSet.removeFirst(); ZDataSet.removeFirst();
                }
                XDataSet.addEntry(new Entry(timeIndex++,values[0]));
                YDataSet.addEntry(new Entry(timeIndex++,values[1]));
                ZDataSet.addEntry(new Entry(timeIndex++,values[2]));
                lc_main_gravity.getXAxis().setAxisMinimum(XDataSet.getXMin());
                lc_main_gravity.getXAxis().setAxisMaximum(XDataSet.getXMax());
            }
        }
        lc_main_gravity.notifyDataSetChanged();
        lc_main_gravity.invalidate();
    }
    @Override
public void onAccuracyChanged(Sensor sensor, int i) {

    }
}