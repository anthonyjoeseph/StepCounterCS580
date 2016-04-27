package com.example.cs580s.stepsensor;

import java.io.Serializable;

/**
 * Created by anthonygabriele on 4/27/16.
 * Holds the full state of how many steps the user has taken.
 */
public class SteppingState implements Serializable{

    private int walkingSteps;
    private int runningSteps;
    private boolean isCounting;

    public SteppingState(){
        reset();
    }

    public void reset(){
        walkingSteps = 0;
        runningSteps = 0;
        isCounting = false;
    }
    public int getWalkingSteps() {
        return walkingSteps;
    }

    public void setWalkingSteps(int walkingSteps) {
        this.walkingSteps = walkingSteps;
    }
    public void incrementWalkingSteps() {
        this.walkingSteps += 1;
    }

    public int getRunningSteps() {
        return runningSteps;
    }

    public void setRunningSteps(int runningSteps) {
        this.runningSteps = runningSteps;
    }
    public void incrementRunningSteps(){
        this.runningSteps += 1;
    }

    public boolean isCounting() {
        return isCounting;
    }

    public void setCounting(boolean counting) {
        isCounting = counting;
    }
}
