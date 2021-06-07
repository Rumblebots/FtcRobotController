package org.firstinspires.ftc.teamcode.recorder;

public class PointSnapshot {
    private int x;
    private int y;
    private int z;

    public PointSnapshot() {
        this(0, 0, 0);
    }

    public PointSnapshot(double x,
                         double y,
                         double z) {
        this.x = (int) Math.round(x);
        this.y = (int) Math.round(y);
        this.z = (int) Math.round(z);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public void setX(double x) {
        this.x = (int) Math.round(x);
    }

    public void setY(double y) {
        this.y = (int) Math.round(y);
    }

    public void setZ(double z) {
        this.z = (int) Math.round(z);
    }
}
