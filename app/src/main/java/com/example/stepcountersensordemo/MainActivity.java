package com.example.stepcountersensordemo;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.TextView;

import com.example.stepcountersensordemo.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static final String TAG = "MainActivity";
    private PowerManager.WakeLock wakeLock;
    private com.example.stepcountersensordemo.databinding.ActivityMainBinding binding;
    private boolean firstStepData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.bind(findViewById(R.id.root_view));
        binding.startTimeText.setText(new Date().toString());
        initStepCounterSensor();
    }

    private void initStepCounterSensor() {
        if (Build.VERSION.SDK_INT < 19)
            return;
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager == null)
            return;
        Sensor stepCounter;
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCounter == null) {
            Log.i(TAG, "no step counter sensor");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "isWakeUpSensor=" + stepCounter.isWakeUpSensor());
        }
        sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_FASTEST);
        Log.d(TAG, "has register listener");

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "demo:sensor");
        wakeLock.acquire();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        StepCounterSensorEvent counterSensorEvent = new StepCounterSensorEvent(sensorEvent);
        TextView textView;
        if (firstStepData) {
            firstStepData = false;
            textView = binding.initialStepText;
        } else {
            textView = binding.currentStepText;
        }
        textView.setText("steps:" + counterSensorEvent.steps + ", time:" + new Date(counterSensorEvent.timestampInMillis).toString());
        Log.d(TAG, counterSensorEvent.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}