package org.firstinspires.ftc.teamcode.recorder;

import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PathfinderRecorder {
    private Pathfinder pathfinder;
    private TimedRecord record;
    private Thread recorderThread;
    private boolean isRecording;
    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

    public PathfinderRecorder(Pathfinder pathfinder) {
        this.pathfinder = pathfinder;

        record = new TimedRecord(this::snapshot);
        exec.scheduleAtFixedRate(() -> {
            record.record();
        }, 0, 100, TimeUnit.MILLISECONDS);
//        recorderThread = new Thread(() -> {
//            while (this.isRecording) {
//                System.out.println("Recording...");
//                record.record();
//            }
//        });
        this.isRecording = false;
    }

    public void start() {
        reset();
        if (!isRecording) {
            this.isRecording = true;
//            recorderThread.start();
        }
    }

    public void stop() {
        this.isRecording = false;
        exec.shutdown();
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
