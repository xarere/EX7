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

public class AmbientActivity extends AppCompatActivity implements SensorEventListener {
    @BindView(R.id.lc_ambient_temp)
    LineChart lc_ambient_temp;
    private int timeIndex=0;
    private SensorManager sensorManager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambient);
        ButterKnife.bind(this);
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        lc_ambient_temp.setTouchEnabled(false);
        lc_ambient_temp.getXAxis().setEnabled(true);
        lc_ambient_temp.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lc_ambient_temp.getAxisLeft().setEnabled(true);
        lc_ambient_temp.getAxisRight().setEnabled(false);
        lc_ambient_temp.getAxisLeft().setAxisMaximum(50);
        lc_ambient_temp.getAxisLeft().setAxisMinimum(-20);
        List<Entry> XEntries=new ArrayList<>();
        LineDataSet XDataSet=new LineDataSet(XEntries,"TEMP");

        LineData lineData=null;
        XDataSet.setColors(Color.rgb(255,0,0));

        XDataSet.setDrawCircles(false);

        lineData=new LineData();
        lineData.addDataSet(XDataSet);

        lc_ambient_temp.setData(lineData);
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
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
        LineData lineData=lc_ambient_temp.getLineData();
        if(lineData!=null){
            LineDataSet XDataSet= (LineDataSet) lineData.getDataSetByLabel("TEMP",true);


                if(XDataSet.getEntryCount()<200) {

                    XDataSet.addEntry(new Entry(timeIndex++, values[0]));

                    lc_ambient_temp.getXAxis().setAxisMaximum(200);
                }else{
                    XDataSet.removeFirst();
                    lc_ambient_temp.getXAxis().setAxisMaximum(XDataSet.getXMax());
                }
            lc_ambient_temp.getAxisLeft().setAxisMinimum(XDataSet.getYMin());
                lc_ambient_temp.getAxisLeft().setAxisMaximum(XDataSet.getYMax());
            lc_ambient_temp.getXAxis().setAxisMinimum(XDataSet.getXMin());
            lc_ambient_temp.notifyDataSetChanged();
            lc_ambient_temp.invalidate();
            }

    }
    @Override
public void onAccuracyChanged(Sensor sensor, int i) {

    }
}