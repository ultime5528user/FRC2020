package com.ultime5528.util;

import java.util.ArrayList;

/**
 * SimpleOrientationHistory
 */
public class SimpleOrientationHistory {

    private TimestampedAngle[] history;

    private int head;

    public SimpleOrientationHistory(int maxSamplesLength){        
        history = new TimestampedAngle[maxSamplesLength];
    }

    public SimpleOrientationHistory(){
        this(1000);
    }

    public void addAngle(TimestampedAngle angle) {
        head ++;
        if (head == history.length) {
            head = 0;
        }

        history[head] = angle;
    }

    public double getAngleAtTimestamp(long timestamp) {
        //Find nearest value under timestamp

        if(timestamp >= history[head].timestamp) return history[head].angle;

        if(history[head+1] != null && timestamp <= history[head + 1].timestamp) return history[head + 1].angle;

        int nearestIdx = 0;
        for (int i = head; i > 0; i--) {
            TimestampedAngle current = history[i];

            if(i == 0) i = history.length - 1;

            if(current.timestamp < timestamp){
                nearestIdx = i;
                break;
            }else if(current.timestamp == timestamp){
                return current.angle;
            }
        }

        //Interpolate if the exact timestamp has not been found
        TimestampedAngle lower = history[nearestIdx];
        TimestampedAngle greater = history[history[nearestIdx + 1] != null ? nearestIdx + 1 : nearestIdx];
        double t = (timestamp - lower.timestamp) / (greater.timestamp - lower.timestamp);
        
        return (1 - t) * lower.angle + t * greater.angle;
    }

    public static class TimestampedAngle {
        public long timestamp;
        public double angle;
    
        public TimestampedAngle(long timestamp, double angle) {
            this.timestamp = timestamp;
            this.angle = angle;
        }
    }
}

