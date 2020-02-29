package com.ultime5528.util;

import java.util.ArrayList;

/**
 * SimpleOrientationHistory
 */
public class SimpleOrientationHistory {

    private TimestampedAngle[] history;

    private int head = -1;

    public SimpleOrientationHistory(int maxSamplesLength){        
        history = new TimestampedAngle[maxSamplesLength];
    }

    public SimpleOrientationHistory(){
        this(1000);
    }

    public void addAngle(long timestamp, double angle) {
        this.addAngle(new TimestampedAngle(timestamp, angle));
    }

    private void addAngle(TimestampedAngle angle) {
        head ++;

        if (head == history.length) {
            head = 0;
        }
        

        history[head] = angle;
    }

    public double getAngleAtTimestamp(long timestamp) {
        //Find nearest value under timestamp

        if(timestamp >= history[head].timestamp) return history[head].angle;

        // if(history[head+1] != null && timestamp <= history[head + 1].timestamp) return history[head + 1].angle;

        int i = head;

        do {
            TimestampedAngle current = history[i];

            if (current == null) {
                int lastIdx = i + 1;
                if (lastIdx == history.length)
                    lastIdx = 0;
                return history[lastIdx].angle;
            }
            
            if(current.timestamp < timestamp){
                break;
            }else if(current.timestamp == timestamp){
                return current.angle;
            }

            i--;

            if(i <= -1)
                i = history.length - 1;

        } while(i != head);

        // for (int i = head; true; i--) {

            

        // }

        if (i == head) {
            if(head + 1 >= history.length)
                return history[0].angle;

            return history[head + 1].angle;
        }

        //Interpolate if the exact timestamp has not been found
        TimestampedAngle lower = history[i];
        TimestampedAngle greater = history[i + 1 >= history.length ? 0 : i+1];
        double t = (timestamp - lower.timestamp) / (double) (greater.timestamp - lower.timestamp);
        
        return (1 - t) * lower.angle + t * greater.angle;
    }

    @Override
    public String toString(){
        String res = "SimpleOrientationHistory[";
        for (TimestampedAngle timestampedAngle : history) {
            res += "\n\t" + timestampedAngle.toString();
        }
        res += "\n]";

        return res;
    }

    public static class TimestampedAngle {
        public long timestamp;
        public double angle;
    
        public TimestampedAngle(long timestamp, double angle) {
            this.timestamp = timestamp;
            this.angle = angle;
        }

        @Override
        public String toString() {
            return String.format("TimestampedAngle[t=%d, a=%.5f]", timestamp, angle);
        }
    }
}

