package com.example.ex7demo;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

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

public class LinearAccel extends AppCompatActivity implements SensorEventListener {
    @BindView(R.id.lc_linear_accel)
    LineChart lc_linear_accel;
    private int timeIndex=0;
    private SensorManager sensorManager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_accel);
        ButterKnife.bind(this);
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        lc_linear_accel.setTouchEnabled(false);
        lc_linear_accel.getXAxis().setEnabled(true);
        lc_linear_accel.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lc_linear_accel.getAxisLeft().setEnabled(true);
        lc_linear_accel.getAxisRight().setEnabled(false);

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
        lc_linear_accel.setData(lineData);
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
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
        LineData lineData=lc_linear_accel.getLineData();
        if(lineData!=null){
            LineDataSet XDataSet= (LineDataSet) lineData.getDataSetByLabel("X",true);
            LineDataSet YDataSet=(LineDataSet) lineData.getDataSetByLabel("Y",true);
            LineDataSet ZDataSet=(LineDataSet) lineData.getDataSetByLabel("Z",true);

                XDataSet.addEntry(new Entry(timeIndex++,values[0]));
                YDataSet.addEntry(new Entry(timeIndex++,values[1]));
                ZDataSet.addEntry(new Entry(timeIndex++,values[2]));
            if(XDataSet.getEntryCount()<200){
                lc_linear_accel.getXAxis().setAxisMaximum(200);
            }else{
                XDataSet.removeFirst();
                YDataSet.removeFirst();
                ZDataSet.removeFirst();
                lc_linear_accel.getXAxis().setAxisMaximum(XDataSet.getXMax());
            }
            lc_linear_accel.getAxisLeft().setAxisMaximum(XDataSet.getYMax());
            lc_linear_accel.getAxisLeft().setAxisMinimum(XDataSet.getYMin());
            lc_linear_accel.getXAxis().setAxisMinimum(XDataSet.getXMin());
            lc_linear_accel.notifyDataSetChanged();
            lc_linear_accel.invalidate();
        }

    }
    @Override
public void onAccuracyChanged(Sensor sensor, int i) {

    }
}