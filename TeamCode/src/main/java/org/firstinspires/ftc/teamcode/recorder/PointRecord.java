package org.firstinspires.ftc.teamcode.recorder;

import com.google.gson.Gson;
import me.wobblyyyy.pathfinder.math.GearRatio;

import java.util.HashMap;
import java.util.function.Supplier;

public class PointRecord extends HashMap<Double, PointSnapshot> {
    private final Supplier<Double> time;
    private final Supplier<PointSnapshot> point;

    public PointRecord(Supplier<Double> time,
                       Supplier<PointSnapshot> point) {
        this.time = time;
        this.point = point;
    }

    public void record() {
        this.put(
                time.get(),
                point.get()
        );
    }

    public String json() {
        return new Gson().toJson(this);
    }
}
