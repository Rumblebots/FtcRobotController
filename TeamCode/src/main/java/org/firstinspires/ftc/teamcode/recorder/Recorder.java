package org.firstinspires.ftc.teamcode.recorder;

import me.wobblyyyy.pathfinder.api.Pathfinder;

import java.util.function.Supplier;

public class Recorder extends PathfinderRecorder {
    private final String name;
    private final Exporter exporter;

    public Recorder(Pathfinder pathfinder,
                    String name) {
        super(pathfinder);
        System.out.println("RECORDER CONST");
        this.name = name;
        this.exporter = new Exporter(name);
    }

    public void start(Supplier<Boolean> shouldRun) {
        System.out.println("SHOULDRUN: " + shouldRun.get());
        super.start();
//        (new Thread(() -> {
//            this.start();
//            while (true);
//            this.stop();
//            this.export();
//        })).start();
    }

    public void record() {
        record.record();
    }

    public void export() {
        System.out.println("EXPORTING");
        this.export(this.exporter::write);
    }
}
