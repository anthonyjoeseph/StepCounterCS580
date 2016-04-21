package com.example.cs580s.stepsensor;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

import static android.hardware.Sensor.*;

public class StepActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private boolean activityIsRunning;
    private int stepCount = 0;
    private TextView stepCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        setContentView(R.layout.activity_step);

        stepCountView = (TextView) findViewById(R.id.stepCounter);
        String localizedStepString = String.format(Locale.getDefault(), "%d", stepCount);
        stepCountView.setText(localizedStepString);

        Button stepButton = (Button) findViewById(R.id.stepButton);
        OnClickListener stepClick = new OnClickListener(){
            @Override
            public void onClick(View view) {
                incrementStepCount();
            }
        };
        if (stepButton != null) {
            stepButton.setOnClickListener(stepClick);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume(){
        super.onResume();
        activityIsRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        activityIsRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
        //sensorManager.unregisterListener(this);
    }

    private void incrementStepCount() {
        stepCount++;
        String localizedStepString = String.format(Locale.getDefault(), "%d", stepCount);
        stepCountView.setText(localizedStepString);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(activityIsRunning){
            incrementStepCount();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}