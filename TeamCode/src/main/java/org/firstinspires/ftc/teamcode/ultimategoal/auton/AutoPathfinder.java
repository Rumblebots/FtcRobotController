/****
 * Made by Tejas Mehta
 * Made on Wednesday, April 07, 2021
 * File Name: AutoPathfinder
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.ultimategoal.pathfinder.PathfinderConstants;
import org.firstinspires.ftc.teamcode.ultimategoal.util.OdometryThread;
import org.firstinspires.ftc.teamcode.ultimategoal.util.TargetZone;
import org.firstinspires.ftc.teamcode.ultimategoal.util.WebcamTFOD;

import java.util.List;

@Autonomous(name = "AutonPathfinder", group = "Autonomous")
public class AutoPathfinder extends LinearOpMode {

    WebcamTFOD webcam = new WebcamTFOD();
    DcMotor flywheel1;
    DcMotor flywheel2;
    Servo loader;
    Servo pusher;
    DcMotor wobbleArm;
    Servo wobbleDropper;
    TouchSensor armOut;
    TouchSensor armIn;

    HeadingPoint wobblePoint = new HeadingPoint(11, 70, 0);

    void initialize() {
        flywheel1 = hardwareMap.get(DcMotor.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotor.class, "flywheel2");
        loader = hardwareMap.get(Servo.class, "loader");
        pusher = hardwareMap.get(Servo.class, "pusher");
        wobbleDropper = hardwareMap.get(Servo.class, "wobbleDropper");
        armOut = hardwareMap.get(TouchSensor.class, "armOut");
        armIn = hardwareMap.get(TouchSensor.class, "armIn");
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
                        wobblePoint = new HeadingPoint(11, 110, 0);
                    } else if (recognition.getLabel().equals("Single")) {
                        wobblePoint = new HeadingPoint(35, 90, 0);
                    } else {
                        wobblePoint = new HeadingPoint(11, 70, 0);
                    }
                    telemetry.addData("Object", recognition.getLabel());
                }
                telemetry.update();
            }
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        PathfinderConstants.initializeMotors(hardwareMap);
        PathfinderConstants.initializePathfinder(this::opModeIsActive);
        Pathfinder pathfinder = PathfinderConstants.getPathfinder();
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
        while (HeadingPoint.pointOrIfNullZero(pathfinder.getPosition()).getY() < wobblePoint.getY()) {}
        while (!armOut.isPressed()) {
            wobbleArm.setPower(0.5);
        }
        wobbleArm.setPower(0.0);
        System.out.println(pathfinder.getPosition());
//        pathfinder.lock();
    }
}
