/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: AutoRobot
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.recorder.Recorder;
import org.firstinspires.ftc.teamcode.ultimategoal.pathfinder.PathfinderConstants;
import org.firstinspires.ftc.teamcode.ultimategoal.util.TargetZone;
import org.firstinspires.ftc.teamcode.ultimategoal.util.WebcamTFOD;

import java.lang.annotation.Target;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AutoRobot {

    WebcamTFOD webcam = new WebcamTFOD();
    DcMotorEx flywheel1;
    DcMotorEx flywheel2;
    Servo loader;
    Servo pusher;
    DcMotor wobbleArm;
    Servo wobbleDropper;
    Servo webcamServo;
    Servo webcamTilt;
    DigitalChannel armOut;
    DigitalChannel armIn;
    int zoneOffset = 0;
    TargetZone zone = TargetZone.A;

    final HeadingPoint[] potentialWobblePoints;
    final HardwareMap hardwareMap;
    final Telemetry telemetry;
    final Point offset;
    HeadingPoint wobblePoint;
    Pathfinder pathfinder;
    private final Recorder recorder;

    AutoRobot(HeadingPoint[] potentialWobblePoints, HardwareMap hardwareMap, Telemetry telemetry, Point offset, Supplier<Boolean> shouldRun) {
        this.potentialWobblePoints = potentialWobblePoints;
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.offset = offset;
        wobblePoint = potentialWobblePoints[2];
        initialize();
        this.pathfinder = initializePathfinder(offset, shouldRun);
        this.recorder = new Recorder(
                pathfinder,
                "DATA.json"
        );
        recorder.start(shouldRun);
    }

    private void initialize() {
        initializeShooterMotor();
        loader = hardwareMap.get(Servo.class, "loader");
        pusher = hardwareMap.get(Servo.class, "pusher");
        wobbleDropper = hardwareMap.get(Servo.class, "wobbleDropper");
        armOut = hardwareMap.get(DigitalChannel.class, "armOut");
        armIn = hardwareMap.get(DigitalChannel.class, "armIn");
        wobbleArm = hardwareMap.get(DcMotor.class, "wobbleArm");
        webcamServo = hardwareMap.get(Servo.class, "webcamServo");
        webcamTilt = hardwareMap.get(Servo.class, "webcamTilt");
        setWebcamPos();
        webcam.init(hardwareMap);
        webcam.activateTfod();
        pusher.setPosition(1);
        wobbleDropper.setPosition(0.0);
    }

    private void setWebcamPos() {
        webcamTilt.setPosition(0.1);
        if (offset.getX() < 90) {
            if (offset.getX() < 30) {
                webcamServo.setPosition(0.45);
            } else {
                webcamServo.setPosition(0.26);
            }
        } else {
            if (offset.getX() < 120) {
                webcamServo.setPosition(0.45);
            } else {
                webcamServo.setPosition(0.26);
            }
        }
    }

    private void initializeShooterMotor() {
        flywheel1 = hardwareMap.get(DcMotorEx.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");
//        MotorConfigurationType flywheel1Config = flywheel1.getMotorType().clone();
//        flywheel1Config.setAchieveableMaxRPMFraction(1.0);
//        flywheel1.setMotorType(flywheel1Config);
//
//        MotorConfigurationType flywheel2Config = flywheel2.getMotorType().clone();
//        flywheel2Config.setAchieveableMaxRPMFraction(1.0);
//        flywheel2.setMotorType(flywheel2Config);
//
//        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        System.out.println("FW1 PID: " + flywheel1.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
//        System.out.println("FW2 PID: " + flywheel2.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
    }

    private Pathfinder initializePathfinder(Point offset, Supplier<Boolean> shouldRun) {
        PathfinderConstants.initializeMotors(hardwareMap);
        PathfinderConstants.resetMotors();
        PathfinderConstants.initializePathfinder(shouldRun);
        Pathfinder pathfinder = PathfinderConstants.getPathfinder();
        pathfinder.getManager().getExecutor().clear();
        PathfinderConstants.getChassisTracker().setOffset(offset);
//        pathfinder.open();
        pathfinder.tick();
        return pathfinder;
    }

    void stopRecording() {
        System.out.println("STOPPING");
        recorder.stop();
        System.out.println("MAIN EXPORT");
        recorder.export();
    }

    HeadingPoint getWobblePoint() {
        return wobblePoint;
    }

    void goToPoint(HeadingPoint point, Supplier<Boolean> shouldRun, int timeout) {
        HeadingPoint currentPoint = pathfinder.getPosition();
        if (currentPoint == null) {
            currentPoint = new HeadingPoint(offset.getX(), offset.getY(), 0);
        }
        System.out.println("HEADING PT: " + currentPoint);
        System.out.println("WOBBLE PT: " + point);
        pathfinder.followPath(new DynamicArray<>(
                currentPoint,
                point
        ));
        double xDist = Math.abs(pathfinder.getPosition().getX() - point.getX());
        double yDist = Math.abs(pathfinder.getPosition().getY() - point.getY());
        double headingDiff = Math.abs(pathfinder.getPosition().getHeading() - point.getHeading());
        long startTime = System.currentTimeMillis();
        while ((xDist >= 3 || yDist >= 3 || headingDiff >= 3) && shouldRun.get()
                && (System.currentTimeMillis() - startTime) < (timeout * 1000L)
                && !pathfinder.getManager().getExecutor().isEmpty()) {
            pathfinder.tick();
            xDist = Math.abs(pathfinder.getPosition().getX() - point.getX());
            yDist = Math.abs(pathfinder.getPosition().getY() - point.getY());
            headingDiff = Math.abs(pathfinder.getPosition().getHeading() - point.getHeading());
        }
        PathfinderConstants.stopMotors();
        pathfinder.stopRobot();
        pathfinder.getManager().getExecutor().clear();
        System.out.println("NEW HEADING PT: " + pathfinder.getPosition());
    }

    void runVision(Supplier<Boolean> isRunning) {
        wobbleDropper.setPosition(0.0);
        while (!isRunning.get()) {
            List<Recognition> updatedRecognitions = webcam.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() == 0) {
                    wobblePoint = potentialWobblePoints[2];
                    zone = TargetZone.A;
                }
                // step through the list of recognitions and display boundary info.
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals("Quad")) {
                        wobblePoint = potentialWobblePoints[0];
                        zone = TargetZone.C;
                    } else if (recognition.getLabel().equals("Single")) {
                        wobblePoint = potentialWobblePoints[1];
                        zoneOffset = 5;
                        zone = TargetZone.B;
                    } else {
                        wobblePoint = potentialWobblePoints[2];
                        zone = TargetZone.A;
                    }
                    telemetry.addData("Object", recognition.getLabel());
                }
                telemetry.update();
            }
        }
    }

    public TargetZone getTargetZone() {
        return zone;
    }

    public int getZoneOffset() {return zoneOffset;}

    public void dropGoal(Consumer<Integer> sleep) {
        while (armOut.getState()) {
            wobbleArm.setPower(0.3);
        }
        wobbleArm.setPower(0.0);
        wobbleDropper.setPosition(0.5);
//        sleep.accept(500);
//        wobbleDropper.setPosition(0.0);
//        while (armIn.getState()) {
//            wobbleArm.setPower(-0.3);
//        }
//        wobbleArm.setPower(0.0);
    }

    public void bringArmIn() {
        while (armIn.getState()) {
            wobbleArm.setPower(-0.3);
        }
        wobbleArm.setPower(0.0);
        wobbleDropper.setPosition(0.0);
    }

    public double getAdjPower() {
        int count = 0;
        double totalVoltage = 0;
        for(VoltageSensor voltageSensor : hardwareMap.voltageSensor) {
            System.out.println("VOLTAGE " + voltageSensor.getDeviceName() + ": " + voltageSensor.getVoltage());
            totalVoltage += voltageSensor.getVoltage();
            count++;
        }
        double voltageStep = 0.2/3;
        double averageExtVolts = Math.max(totalVoltage/count - 11, 0);
        System.out.println("POWER DECLINE: " + (averageExtVolts * voltageStep));
        return Math.min(1.0, 1 - (averageExtVolts * voltageStep));
    }

    public void setShooterPower(double pow) {
        for(VoltageSensor voltageSensor : hardwareMap.voltageSensor) {
            System.out.println("Voltage: " + voltageSensor.getVoltage());
        }
        System.out.println("Velocity: " +flywheel1.getVelocity());
        System.out.println("MAX FLYWHEEL 1: " + flywheel1.getMotorType().getAchieveableMaxTicksPerSecond());
        System.out.println("MAX FLYWHEEL 2: " + flywheel2.getMotorType().getAchieveableMaxTicksPerSecond());
//        flywheel1.setVelocity(flywheel1.getMotorType().getAchieveableMaxTicksPerSecond() * pow);
////        flywheel2.setPower(pow);
//        flywheel1.setVelocity(flywheel1.getMotorType().getAchieveableMaxTicksPerSecond() * pow);
        flywheel1.setPower(pow);
        flywheel2.setPower(pow);
        loader.setPosition((180.0-36.0)/180.0);
    }

    public void shoot(Consumer<Integer> sleep) {
        System.out.println("SHOOTING");
        pusher.setPosition(1);
        sleep.accept(500);
        pusher.setPosition(0.6);
        sleep.accept(500);
        pusher.setPosition(1.0);
        sleep.accept(500);
    }

}
