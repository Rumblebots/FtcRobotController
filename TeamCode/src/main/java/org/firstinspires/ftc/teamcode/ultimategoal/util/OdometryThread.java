/****
 * Made by Tejas Mehta
 * Made on Tuesday, December 01, 2020
 * File Name: OdometryThread
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.shared.subystems*/
package org.firstinspires.ftc.teamcode.ultimategoal.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.tejasmehta.OdometryCore.OdometryCore;
import com.tejasmehta.OdometryCore.localization.EncoderPositions;
import com.tejasmehta.OdometryCore.localization.HeadingUnit;
import com.tejasmehta.OdometryCore.localization.OdometryPosition;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OdometryThread extends Thread{

    private final DcMotor encoderLeft;
    private final DcMotor encoderRight;
    private final DcMotor encoderBack;
    private final double offset;
    private OdometryPosition currentPosition;
    private boolean running = true;
    private static OdometryThread currentInstance;
    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

    public static void initialize(double offset, DcMotor encoderLeft, DcMotor encoderRight, DcMotor encoderBack) {
        System.out.println("INITING");
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
        currentPosition = new OdometryPosition(offset, 0, 0, HeadingUnit.RADIANS);
        this.offset = offset;
        this.encoderLeft = encoderLeft;
        this.encoderRight = encoderRight;
        this.encoderBack = encoderBack;
        OdometryCore.initialize(1440, 1.5, 7.83, 7.83, 1);
    }

    public OdometryPosition getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public void run() {
        System.out.println("STARTING");
        exec.scheduleAtFixedRate(() -> {
            OdometryPosition rawPos = OdometryCore.getInstance().getCurrentPosition(new EncoderPositions(-encoderLeft.getCurrentPosition(), encoderRight.getCurrentPosition(), encoderBack.getCurrentPosition()));
            currentPosition = new OdometryPosition(rawPos.getX() + offset, rawPos.getY(), rawPos.getHeadingRadians(), HeadingUnit.RADIANS);
        }, 0, 10, TimeUnit.MILLISECONDS);
//        while (running) {
//            OdometryPosition rawPos = OdometryCore.getInstance().getCurrentPosition(new EncoderPositions(-encoderLeft.getCurrentPosition(), encoderRight.getCurrentPosition(), encoderBack.getCurrentPosition()));
//            currentPosition = new OdometryPosition(rawPos.getX() + offset, rawPos.getY(), rawPos.getHeadingRadians(), HeadingUnit.RADIANS);
//            Telemetry.addData("ODOM_POS", "Odometry Pos", ":", "x: " + currentPosition.getX() + ", y: " + currentPosition.getY() + ", heading: " + currentPosition.getHeadingDegrees());
//        }
    }
}
