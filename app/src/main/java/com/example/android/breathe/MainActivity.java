package com.example.android.breathe;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private static final int DATA_SIZE = 101;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView gyroText;
    private GraphView graph;
    LineGraphSeries<DataPoint> series;
    private DataPoint[] data = new DataPoint[101];
    private int i = 0;

    // Change this value to increase or decrease the sensitivity (This is multiplying factor)
    private static final double SCALER = (180.0/3.14);

    // Sampling Time
    private static final int SAMPLING_TIME = 150; //milliseconds



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i=0;i<data.length; i++){
            data[i] = new DataPoint(0,0);
        }

        // Create instances of Sensor and SensorManager to work with sensors.Sensor is initialized with Gyroscope Sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // If Sensor is not found or object is not created
        if(mSensor == null){
            Toast.makeText(this,"Error accessing Gyroscope Sensor",Toast.LENGTH_LONG).show();
        }

        // Register a Listener to record sensor values whenever they change
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);



        gyroText = (TextView) findViewById(R.id.gyro_text);
        graph = (GraphView) findViewById(R.id.graph);

        graph.setTitle("Breathing Motions");
        graph.setTitleTextSize(30);

        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);


        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-20);
        graph.getViewport().setMaxY(20);


        // Timer that Runs the Code after specified number of milliseconds
        final Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                graph.removeAllSeries();
                series = new LineGraphSeries<DataPoint>(data);
                graph.addSeries(series);
                handler.postDelayed(this,SAMPLING_TIME);
            }
        };
        handler.post(run);

        Button record_button = (Button) findViewById(R.id.record_data);
        record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }



    @Override
    public void onSensorChanged(final SensorEvent sensorEvent) {

        double scaled_value = SCALER * sensorEvent.values[0];
        double roundOffX = Math.round(scaled_value * 100.0) / 100.0;

        if( scaled_value < 1.5 && scaled_value > -1.5){
            scaled_value = 0;
        }

        // Update the UI with the results from gyroscope
        gyroText.setText("X :" + sensorEvent.values[0] + "\n\nY : " + sensorEvent.values[1] + "\n\nZ : " + sensorEvent.values[2]);
        if (scaled_value < 20 && scaled_value > -20){
            data[i] = new DataPoint(i, roundOffX);
            i++;
            Log.d("onSensorChanged",String.valueOf(scaled_value));
            if(i > data.length - 1){
                i = 0;
                for(int j =0 ;j < data.length ; j++){
                    data[j] = new DataPoint(0,0);
                }
            }
        }



            // Writing to a file

        FileWriter writer = null;
        try {
            writer = new FileWriter("myFile.csv",false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.append(String.valueOf(roundOffX));
            writer.append(",");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // no Changes needed.
    }
}