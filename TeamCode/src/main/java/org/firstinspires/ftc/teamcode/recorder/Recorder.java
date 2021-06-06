package org.firstinspires.ftc.teamcode.recorder;

import me.wobblyyyy.pathfinder.api.Pathfinder;

import java.util.function.Supplier;

public class Recorder extends PathfinderRecorder {
    private final String name;
    private final Exporter exporter;

    public Recorder(Pathfinder pathfinder,
                    String name) {
        super(pathfinder);
        this.name = name;
        this.exporter = new Exporter(name);
    }

    public void start(Supplier<Boolean> shouldRun) {
        (new Thread(() -> {
            this.start();
            while (shouldRun.get());
            this.stop();
            this.export();
        })).start();
    }

    public void export() {
        this.export(this.exporter::write);
    }
}
