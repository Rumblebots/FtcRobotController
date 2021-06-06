package org.firstinspires.ftc.teamcode.recorder;

import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

import java.util.function.Consumer;

public class PathfinderRecorder {
    private Pathfinder pathfinder;
    private TimedRecord record;
    private Thread recorderThread;
    private boolean isRecording;

    public PathfinderRecorder(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;

        record = new TimedRecord(this::snapshot);
        recorderThread = new Thread(() -> {
            while (this.isRecording) {
                record.record();
            }
        });
        this.isRecording = false;
    }

    public void start() {
        reset();
        if (!isRecording) {
            this.isRecording = true;
            recorderThread.start();
        }
    }

    public void stop() {
        this.isRecording = false;
    }

    public void reset() {
        // todo reset if needed
    }

    public PointSnapshot snapshot() {
        HeadingPoint point = pathfinder.getPosition();

        return new PointSnapshot(
                point.getX(),
                point.getY(),
                point.getHeading()
        );
    }

    public String json() {
        return this.record.json();
    }

    public void export(Consumer<String> consumer) {
        consumer.accept(this.json());
    }
}
