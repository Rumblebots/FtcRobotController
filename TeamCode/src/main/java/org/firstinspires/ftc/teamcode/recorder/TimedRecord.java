package org.firstinspires.ftc.teamcode.recorder;

import java.util.function.Supplier;

public class TimedRecord extends PointRecord {
    public TimedRecord(Supplier<PointSnapshot> point) {
        super(new ElapsedTime()::elapsed, point);
    }
}
