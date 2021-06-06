package org.firstinspires.ftc.teamcode.recorder;

public class PointSnapshot {
    private double x;
    private double y;
    private double z;

    public PointSnapshot() {
        this(0, 0, 0);
    }

    public PointSnapshot(double x,
                         double y,
                         double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.x;
    }

    public double getZ() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
