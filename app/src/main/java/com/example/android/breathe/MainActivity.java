package com.example.android.breathe;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mSensor;
    TextView gyroText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create instances of Sensor and SensorManager to work with sensors.Sensor is initialized with Gyroscope Sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Register a Listener to record sensor values whenever they change
        mSensorManager.registerListener(this, mSensor , SensorManager.SENSOR_DELAY_NORMAL);


        gyroText = (TextView) findViewById(R.id.gyro_text);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Update the UI with the results from gyroscope
        gyroText.setText("X :" + sensorEvent.values[0] + "\n\nY : " + sensorEvent.values[1] + "\n\nZ : " + sensorEvent.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // no Changes needed.
    }
}