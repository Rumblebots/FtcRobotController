package org.firstinspires.ftc.teamcode.recorder;

public class ElapsedTime {
    private static double ms() {
        return System.currentTimeMillis();
    }

    private final double start = ms();

    public double elapsed() {
        return ms() - start;
    }
}
