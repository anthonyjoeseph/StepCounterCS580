package com.example.cs580s.stepsensor;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

import static android.hardware.Sensor.*;
import static java.lang.Math.*;

public class StepActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private boolean activityIsRunning;
    private boolean stepWasFlagged;
    private SteppingState steppingState;
    private static final String steppingStateID = "steppingStateID";
    private TextView walkingStepCountView;
    private TextView runningStepCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepWasFlagged = false;

        setContentView(R.layout.activity_step);

        if(savedInstanceState == null){
            steppingState = new SteppingState();
        }else{
            steppingState = (SteppingState) savedInstanceState.getSerializable(StepActivity.steppingStateID);
        }
        walkingStepCountView = (TextView) findViewById(R.id.walkingStepCounter);
        runningStepCountView = (TextView) findViewById(R.id.runningStepCounter);
        updateWalkingStepCountView();
        updateRunningStepCountView();

        Button startButton = (Button) findViewById(R.id.startButton);
        Button stopButton = (Button) findViewById(R.id.stopButton);
        OnClickListener startClick = new OnClickListener(){
            @Override
            public void onClick(View view) {
                startCounting();
            }
        };
        OnClickListener stopClick = new OnClickListener(){
            @Override
            public void onClick(View view) {
                stopCounting();
            }
        };
        if (startButton != null) {
            startButton.setOnClickListener(startClick);
        }
        if (stopButton != null) {
            stopButton.setOnClickListener(stopClick);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(steppingStateID, steppingState);
        super.onSaveInstanceState(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume(){
        super.onResume();

        activityIsRunning = true;
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(TYPE_LINEAR_ACCELERATION);
        Sensor countSensor = sensorManager.getDefaultSensor(TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
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

    private void startCounting() {
        steppingState.reset();
        updateWalkingStepCountView();
        updateRunningStepCountView();
    }

    private void stopCounting(){
        steppingState.setCounting(false);
    }

    private void updateWalkingStepCountView(){
        String localizedStepString = String.format(Locale.getDefault(), "%d", steppingState.getWalkingSteps());
        walkingStepCountView.setText(localizedStepString);
    }

    private void updateRunningStepCountView(){
        String localizedStepString = String.format(Locale.getDefault(), "%d", steppingState.getRunningSteps());
        runningStepCountView.setText(localizedStepString);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(steppingState.isCounting()) {
            switch (sensorEvent.sensor.getType()) {
                case TYPE_STEP_COUNTER:
                    if (activityIsRunning) {
                        stepWasFlagged = true;
                    }
                    break;
                case TYPE_LINEAR_ACCELERATION:
                    if (stepWasFlagged) {
                        stepWasFlagged = false;
                        float X = sensorEvent.values[0];
                        float Y = sensorEvent.values[1];
                        float Z = sensorEvent.values[2];
                        double vectorLength = sqrt((X * X) + (Y * Y) + (Z * Z));
                        if (vectorLength > 10.0) {
                            steppingState.incrementRunningSteps();
                            updateRunningStepCountView();
                        } else {
                            System.out.println(vectorLength);
                            steppingState.incrementWalkingSteps();
                            updateWalkingStepCountView();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}