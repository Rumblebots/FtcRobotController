package org.firstinspires.ftc.teamcode.recorder;

import com.qualcomm.robotcore.util.ReadWriteFile;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

public class Exporter {
    private final String name;

    public Exporter(String name) {
        this.name = name;
    }

    public void write(String data) {
        File file = AppUtil.getInstance().getSettingsFile(name);
        System.out.printf("file path: " + file.getPath());
        ReadWriteFile.writeFile(file, data);
    }
}
