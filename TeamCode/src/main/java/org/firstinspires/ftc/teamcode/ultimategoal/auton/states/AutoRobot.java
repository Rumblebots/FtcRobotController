/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: AutoRobot
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.ultimategoal.pathfinder.PathfinderConstants;
import org.firstinspires.ftc.teamcode.ultimategoal.util.WebcamTFOD;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AutoRobot {

    WebcamTFOD webcam = new WebcamTFOD();
    DcMotor flywheel1;
    DcMotor flywheel2;
    Servo loader;
    Servo pusher;
    DcMotor wobbleArm;
    Servo wobbleDropper;
    DigitalChannel armOut;
    DigitalChannel armIn;

    final HeadingPoint[] potentialWobblePoints;
    final HardwareMap hardwareMap;
    final Telemetry telemetry;
    final double xOffset;
    HeadingPoint wobblePoint;
    Pathfinder pathfinder;

    AutoRobot(HeadingPoint[] potentialWobblePoints, HardwareMap hardwareMap, Telemetry telemetry, double xOffset, Supplier<Boolean> shouldRun) {
        this.potentialWobblePoints = potentialWobblePoints;
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.xOffset = xOffset;
        wobblePoint = potentialWobblePoints[2];
        initialize();
        this.pathfinder = initializePathfinder(xOffset, shouldRun);
    }

    private void initialize() {
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

    private Pathfinder initializePathfinder(double xOffset, Supplier<Boolean> shouldRun) {
        PathfinderConstants.initializeMotors(hardwareMap);
        PathfinderConstants.initializePathfinder(shouldRun);
        Pathfinder pathfinder = PathfinderConstants.getPathfinder();
        pathfinder.getManager().getExecutor().clear();
        PathfinderConstants.getChassisTracker().setOffset(new Point(xOffset, 0));
        pathfinder.open();
        return pathfinder;
    }

    HeadingPoint getWobblePoint() {
        return wobblePoint;
    }

    void goToPoint(HeadingPoint point) {
        HeadingPoint currentPoint = pathfinder.getPosition();
        if (currentPoint == null) {
            currentPoint = new HeadingPoint(xOffset, 0, 0);
        }
        System.out.println("HEADING PT: " + currentPoint);
        System.out.println("WOBBLE PT: " + point);
        pathfinder.followPath(new DynamicArray<>(
                currentPoint,
                point
        ));
        pathfinder.tickUntil();
        System.out.println("NEW HEADING PT: " + pathfinder.getPosition());
    }

    void runVision(Supplier<Boolean> isRunning) {
        wobbleDropper.setPosition(0.0);
        while (!isRunning.get()) {
            List<Recognition> updatedRecognitions = webcam.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                // step through the list of recognitions and display boundary info.
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals("Quad")) {
                        wobblePoint = potentialWobblePoints[0];
                    } else if (recognition.getLabel().equals("Single")) {
                        wobblePoint = potentialWobblePoints[1];
                    } else {
                        wobblePoint = potentialWobblePoints[2];
                    }
                    telemetry.addData("Object", recognition.getLabel());
                }
                telemetry.update();
            }
        }
    }

    public void shoot(Consumer<Integer> sleep) {
        flywheel1.setPower(1.0);
        flywheel2.setPower(1.0);
        sleep.accept(1000);
        loader.setPosition((180.0-36.0)/180.0);
        sleep.accept(500);
        pusher.setPosition(1);
        sleep.accept(1000);
        pusher.setPosition(0.6);
        sleep.accept(500);
        pusher.setPosition(1.0);
        sleep.accept(1000);
    }

}
