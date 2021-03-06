/****
 * Made by Tejas Mehta
 * Made on Tuesday, December 01, 2020
 * File Name: OdometryThread
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.shared.subystems*/
package org.firstinspires.ftc.teamcode.ultimategoal.util;

//import android.os.Build;
//import androidx.annotation.RequiresApi;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.tejasmehta.OdometryCore.OdometryCore;
import com.tejasmehta.OdometryCore.localization.EncoderPositions;
import com.tejasmehta.OdometryCore.localization.HeadingUnit;
import com.tejasmehta.OdometryCore.localization.OdometryPosition;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class OdometryThread extends Thread{

    private final DcMotor encoderLeft;
    private final DcMotor encoderRight;
    private final DcMotor encoderBack;
    private final double offset;
    private OdometryPosition currentPosition;
    private boolean running = true;
    private static OdometryThread currentInstance;
    private static Supplier<Boolean> activeChecker;
    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

    public static void initialize(double offset, DcMotor encoderLeft, DcMotor encoderRight, DcMotor encoderBack, Supplier<Boolean> isActive) {
        System.out.println("INITING");
        activeChecker = isActive;
        currentInstance = new OdometryThread(offset, encoderLeft, encoderRight, encoderBack);
        currentInstance.startThread();
    }

    public static OdometryThread getInstance() {
        if (currentInstance == null) {
            throw new IllegalArgumentException("Please use OdometryThread.initialize first");
        }
        return currentInstance;
    }

    public void stopThread() {
        running = false;
        exec.shutdown();
    }

    public void startThread() {
        running = true;
        currentInstance.start();
        System.out.println("STARTED");
    }

    private OdometryThread(double offset, DcMotor encoderLeft, DcMotor encoderRight, DcMotor encoderBack) {
        currentPosition = new OdometryPosition(offset, offset, 0, HeadingUnit.RADIANS);
        this.offset = offset;
        this.encoderLeft = encoderLeft;
        this.encoderRight = encoderRight;
        this.encoderBack = encoderBack;
        OdometryCore.initialize(1440, 1.5, 7.83, 7.83, 1);
    }

    public OdometryPosition getCurrentPosition() {
        return currentPosition;
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        System.out.println("STARTING");
        exec.scheduleAtFixedRate(() -> {
            if (!activeChecker.get()) {
                exec.shutdown();
            }
            OdometryPosition rawPos = OdometryCore.getInstance().getCurrentPosition(new EncoderPositions(-encoderLeft.getCurrentPosition(), encoderRight.getCurrentPosition(), encoderBack.getCurrentPosition()));
            currentPosition = new OdometryPosition(rawPos.getX() + offset, rawPos.getY() + offset, rawPos.getHeadingRadians(), HeadingUnit.RADIANS);
        }, 0, 10, TimeUnit.MILLISECONDS);
//        while (running) {
//            OdometryPosition rawPos = OdometryCore.getInstance().getCurrentPosition(new EncoderPositions(-encoderLeft.getCurrentPosition(), encoderRight.getCurrentPosition(), encoderBack.getCurrentPosition()));
//            currentPosition = new OdometryPosition(rawPos.getX() + offset, rawPos.getY(), rawPos.getHeadingRadians(), HeadingUnit.RADIANS);
//            Telemetry.addData("ODOM_POS", "Odometry Pos", ":", "x: " + currentPosition.getX() + ", y: " + currentPosition.getY() + ", heading: " + currentPosition.getHeadingDegrees());
//        }
    }
}
