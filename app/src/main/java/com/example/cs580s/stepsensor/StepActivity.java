package com.example.cs580s.stepsensor;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Button;

import java.util.Locale;

public class StepActivity extends AppCompatActivity {

    private int stepCount = 0;
    private TextView stepCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void incrementStepCount() {
        stepCount++;
        String localizedStepString = String.format(Locale.getDefault(), "%d", stepCount);
        stepCountView.setText(localizedStepString);
    }
}