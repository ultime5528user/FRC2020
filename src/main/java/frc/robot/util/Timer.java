package frc.robot.util;

/**
 * Timer
 */
public class Timer extends edu.wpi.first.wpilibj.Timer {

    private boolean isRunning;

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public synchronized void start() {
        super.start();
        isRunning = true;
    }

    @Override
    public synchronized void stop() {
        super.stop();
        isRunning = false;
    }

}