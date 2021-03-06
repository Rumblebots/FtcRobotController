/****
 * Made by Tejas Mehta
 * Made on Wednesday, April 07, 2021
 * File Name: AutoPathfinder
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.Distance;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.ultimategoal.pathfinder.PathfinderConstants;
import org.firstinspires.ftc.teamcode.ultimategoal.util.OdometryThread;
import org.firstinspires.ftc.teamcode.ultimategoal.util.TargetZone;
import org.firstinspires.ftc.teamcode.ultimategoal.util.WebcamTFOD;

import java.util.List;

@Disabled
@Autonomous(name = "AutonPathfinder", group = "Autonomous")
public class AutoPathfinder extends LinearOpMode {

    WebcamTFOD webcam = new WebcamTFOD();
    DcMotor flywheel1;
    DcMotor flywheel2;
    Servo loader;
    Servo pusher;
    DcMotor wobbleArm;
    Servo wobbleDropper;
    DigitalChannel armOut;
    DigitalChannel armIn;

    HeadingPoint wobblePoint = new HeadingPoint(20, 70, 0);

    void initialize() {
        flywheel1 = hardwareMap.get(DcMotor.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotor.class, "flywheel2");
        loader = hardwareMap.get(Servo.class, "loader");
        pusher = hardwareMap.get(Servo.class, "pusher");
        wobbleDropper = hardwareMap.get(Servo.class, "wobbleDropper");
        armOut = hardwareMap.get(DigitalChannel.class, "armOut");
        armIn = hardwareMap.get(DigitalChannel.class, "armIn");
        wobbleArm = hardwareMap.get(DcMotor.class, "wobbleArm");
        webcam.init(hardwareMap);
        webcam.activateTfod();
    }

    void runVision() {
        wobbleDropper.setPosition(0.0);
        while (!isStarted()) {
            List<Recognition> updatedRecognitions = webcam.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                // step through the list of recognitions and display boundary info.
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals("Quad")) {
                        wobblePoint = new HeadingPoint(20, 113, 0);
                    } else if (recognition.getLabel().equals("Single")) {
                        wobblePoint = new HeadingPoint(45, 93, 0);
                    } else {
                        wobblePoint = new HeadingPoint(20, 73, 0);
                    }
                    telemetry.addData("Object", recognition.getLabel());
                }
                telemetry.update();
            }
        }
    }

    public void shoot() {
        loader.setPosition((180.0-36.0)/180.0);
        sleep(500);
        pusher.setPosition(1);
        sleep(1000);
        pusher.setPosition(0.6);
        sleep(500);
        pusher.setPosition(1.0);
        sleep(1000);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        PathfinderConstants.initializeMotors(hardwareMap);
        PathfinderConstants.initializePathfinder(this::opModeIsActive);
        Pathfinder pathfinder = PathfinderConstants.getPathfinder();
        pathfinder.getManager().getExecutor().clear();
        System.out.println("CLR: " + pathfinder.getPosition());
        PathfinderConstants.getChassisTracker().setOffset(new Point(35, 0));
        initialize();
        runVision();
        waitForStart();
        pathfinder.open();
        pathfinder.followPath(
                new DynamicArray<>(
                        new HeadingPoint(35, 0, 0),
                        wobblePoint
                )
        );
        while (!Distance.isNearPoint(Point.pointOrIfNullZero(pathfinder.getPosition()), wobblePoint, 3)) {
            System.out.println(pathfinder.getPosition());
            System.out.println(wobblePoint);
        }
//        while (HeadingPoint.pointOrIfNullZero(pathfinder.getPosition()).getY() < wobblePoint.getY()) {}
        while (armOut.getState()) {
            wobbleArm.setPower(0.3);
        }
        wobbleArm.setPower(0.0);
        wobbleDropper.setPosition(1.0);
        pathfinder.followPath(new DynamicArray<>(
                wobblePoint,
                new HeadingPoint(37, 50, 0)
        ));
        pathfinder.tickUntil();
//        while (Distance.isNearPoint(HeadingPoint.pointOrIfNullZero(pathfinder.getPosition()), new HeadingPoint(42, 50, 0), 3)) { }
//        pathfinder.lock();
        flywheel1.setPower(1.0);
        flywheel2.setPower(1.0);
        sleep(1500);
        shoot();
        sleep(500);
        pathfinder.followPath(new DynamicArray<>(
                pathfinder.getPosition(),
                new HeadingPoint(42, 50, 10)
        ));
        pathfinder.tickUntil();
//        while (Distance.isNearPoint(HeadingPoint.pointOrIfNullZero(pathfinder.getPosition()), new HeadingPoint(47, 50, 0), 3)) { }
        shoot();
        pathfinder.followPath(new DynamicArray<>(
                pathfinder.getPosition(),
                new HeadingPoint(47, 50, 10)
        ));
        pathfinder.tickUntil();
//        while (Distance.isNearPoint(HeadingPoint.pointOrIfNullZero(pathfinder.getPosition()), new HeadingPoint(52, 50, 0), 3)) { }
        shoot();
        flywheel1.setPower(0.0);
        flywheel2.setPower(0.0);
//        pathfinder.followPath(new DynamicArray<>(
//                pathfinder.getPosition(),
//                new HeadingPoint(52, 65, 10)
//        ));
//        while (HeadingPoint.pointOrIfNullZero(pathfinder.getPosition()).getY() < 65) {}
        HeadingPoint point = new HeadingPoint(pathfinder.getPosition().getX(), 57, 0);
        pathfinder.followPath(new DynamicArray<>(
                pathfinder.getPosition(),
                point
        ));
        while (!Distance.isNearPoint(pathfinder.getPosition(), point, 2) && opModeIsActive()) {}
        if (!opModeIsActive()) {
            pathfinder.stopRobot();
        }
        //        pathfinder.followPath(new DynamicArray<>(
//                pathfinder.getPosition(),
//                new HeadingPoint(pathfinder.getPosition().getX(), 5, 0)
//        ));
//        while (pathfinder.getPosition().getY() > 7) {}
////        pathfinder.stopRobot();
////        pathfinder.tickUntil();
//        wobbleDropper.setPosition(0.0);
//        sleep(700);
//        pathfinder.followPath(new DynamicArray<>(
//                pathfinder.getPosition(),
//                new HeadingPoint(wobblePoint.getX()+5, wobblePoint.getY(), 0)
//        ));
//        while ((pathfinder.getPosition().getX() < wobblePoint.getX()+4 || pathfinder.getPosition().getY() < wobblePoint.getY()-2) && opModeIsActive()) {}
//        if (!opModeIsActive()) {
//            return;
//        }
////        pathfinder.tickUntil();
//        wobbleDropper.setPosition(1.0);
//        sleep(700);
//        HeadingPoint point = new HeadingPoint(pathfinder.getPosition().getX(), 60, 0);
//        pathfinder.followPath(new DynamicArray<>(
//                pathfinder.getPosition(),
//                point
//        ));
//        while (!Distance.isNearPoint(pathfinder.getPosition(), point, 2) && opModeIsActive()) {}
//        pathfinder.stopRobot();
//        return;
//        pathfinder.lock();
    }
}
