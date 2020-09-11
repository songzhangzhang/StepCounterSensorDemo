package com.example.stepcountersensordemo;

import android.hardware.SensorEvent;

import java.util.concurrent.TimeUnit;

public class StepCounterSensorEvent {
    /**
     * sensor step value
     * represent the accumulative steps since device boot
     */
    public final int steps;
    /**
     * sensor timestamp value
     * represent the steps happened time since device boot
     * unit: millis(raw)
     */
    public final long timestampInMillis;

    public StepCounterSensorEvent(SensorEvent sensorEvent) {
        this.steps = (int) sensorEvent.values[0];
        this.timestampInMillis = TimeUnit.NANOSECONDS.toMillis(sensorEvent.timestamp);
    }

    @Override
    public String toString() {
        return "StepCounterSensorEvent{" +
                "steps=" + steps +
                ", timestampInMillis=" + timestampInMillis +
                '}';
    }
}
