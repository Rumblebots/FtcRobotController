/****
 * Made by Tejas Mehta
 * Made on Tuesday, March 02, 2021
 * File Name: AutoVision
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.tejasmehta.OdometryCore.localization.OdometryPosition;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.ultimategoal.util.MotionType;
import org.firstinspires.ftc.teamcode.ultimategoal.util.OdometryThread;
import org.firstinspires.ftc.teamcode.ultimategoal.util.TargetZone;
import org.firstinspires.ftc.teamcode.ultimategoal.util.WebcamTFOD;

import java.util.List;

@Disabled
@Autonomous(name = "AutoVisionNewTest", group = "Auton")
public class AutoVisionNewTest extends LinearOpMode {

    WebcamTFOD webcam = new WebcamTFOD();
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    DcMotor flywheel1;
    DcMotor flywheel2;
    Servo loader;
    Servo pusher;
    Servo wobbleDropper;
    Servo wobbleArm;

    public OdometryPosition getCurrentPos() {
        OdometryPosition currentPosition = OdometryThread.getInstance().getCurrentPosition();
        telemetry.addData("Odometry Pos: ", "x: " + currentPosition.getX() + ", y: " + currentPosition.getY() + ", heading: " + currentPosition.getHeadingDegrees());
        telemetry.update();
        return OdometryThread.getInstance().getCurrentPosition();
    }

    public void odometryMove(double x, double y, boolean correct, double timeoutS) {
        boolean initialy = true;
        long startTime = System.currentTimeMillis();
        if (y + 2 > getCurrentPos().getY()) {
            double power = 0.3;
            double initialPos = getCurrentPos().getY();
            double tenths = (y - getCurrentPos().getY())/10;
            while (y > getCurrentPos().getY() && (System.currentTimeMillis() - startTime)/1000.0 < timeoutS) {
                double speed = (y - getCurrentPos().getY())/tenths;
                speed = Math.max(0.3, speed);
                speed = Math.min(0.8, speed);
                meccanumDrive(MotionType.FORWARD, initialy ? 0.6 : 0.3);
            }
            initialy = false;
        }

        if (y - 2 < getCurrentPos().getY()){
            setPower(0, 0, 0, 0);
            sleep(500);
            double backDist = !initialy ? y + 2 : y;
            while (backDist < getCurrentPos().getY() && (System.currentTimeMillis() - startTime)/1000.0 < timeoutS) {
                if (!initialy && !correct) {
                    break;
                }
                meccanumDrive(MotionType.BACKWARD, initialy ? 0.5 : 0.3);
            }
        }
        if (x == -1) {
            return;
        }
        startTime = System.currentTimeMillis();
        if (x + 2 > getCurrentPos().getX()) {
            setPower(0, 0, 0, 0);
            sleep(500);
            while (x > getCurrentPos().getX() && (System.currentTimeMillis() - startTime)/1000.0 < timeoutS) {
                meccanumDrive(MotionType.RIGHT, 0.6);
            }
        } else if (x - 2 < getCurrentPos().getX()){
            setPower(0, 0, 0, 0);
            sleep(500);
            while (x < getCurrentPos().getX() && (System.currentTimeMillis() - startTime)/1000.0 < timeoutS) {
                meccanumDrive(MotionType.LEFT, 0.5);
            }
        }

        setPower(0, 0, 0, 0);
    }

    public void odometryTurnMove(double x, double y) {
        double radHeading = Math.atan2(y-getCurrentPos().getY(), x-getCurrentPos().getX());
        odometryTurn(Math.toDegrees(radHeading), true);
        while (getCurrentPos().getX() < x) {
            meccanumDrive(MotionType.FORWARD, 0.5);
        }
        setPower(0, 0, 0, 0);
    }

    public void odometryTurn(double x, double y) {
        double radHeading = Math.atan2(Math.abs(x-getCurrentPos().getX()), Math.abs(y-getCurrentPos().getY()));
        if (x > getCurrentPos().getX()) radHeading *= -1;
        odometryTurn(Math.toDegrees(radHeading), false);
    }

    public void odometryTurn(double headingDegrees, boolean isAbsolute) {
        double targetDegrees = !isAbsolute ? getCurrentPos().getHeadingDegrees() + headingDegrees : headingDegrees;
        if (targetDegrees < 0) {
            while (getCurrentPos().getHeadingDegrees() > targetDegrees) {
                meccanumDrive(MotionType.TURN_CW, 0.25);
            }
        } else if (targetDegrees > 0) {
            while (getCurrentPos().getHeadingDegrees() < targetDegrees) {
                meccanumDrive(MotionType.TURN_CCW, 0.25);
            }
        } else {
            if (getCurrentPos().getHeadingDegrees() > 0) {
                while (getCurrentPos().getHeadingDegrees() > 0) {
                    meccanumDrive(MotionType.TURN_CW, 0.25);
                }
            } else if (getCurrentPos().getHeadingDegrees() < 0) {
                while (getCurrentPos().getHeadingDegrees() < 0) {
                    meccanumDrive(MotionType.TURN_CCW, 0.25);
                }
            }
        }
        setPower(0, 0, 0, 0);
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

    public void meccanumDrive(MotionType type, double power) {
        power = -power;
        switch (type) {
            case FORWARD:
                setPower(-power, power, -power, power);
                break;
            case BACKWARD:
                setPower(power, -power, power, -power);
                break;
            case LEFT:
                setPower(-power, power, power, -power);
                break;
            case RIGHT:
                setPower(power, -power, -power, power);
                break;
            case TURN_CW:
                setPower(power, power, power, power);
                break;
            case TURN_CCW:
                setPower(-power, -power, -power, -power);
                break;
        }
    }

    public void setPower(double fr, double fl, double br, double bl) {
        frontRight.setPower(fr);
        frontLeft.setPower(fl);
        backRight.setPower(br);
        backLeft.setPower(bl);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        flywheel1 = hardwareMap.get(DcMotor.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotor.class, "flywheel2");
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        flywheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        flywheel2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        loader = hardwareMap.get(Servo.class, "loader");
        pusher = hardwareMap.get(Servo.class, "pusher");
        wobbleDropper = hardwareMap.get(Servo.class, "wobbleDropper");
        wobbleArm = hardwareMap.get(Servo.class, "wobbleArm");
        webcam.init(hardwareMap);
        webcam.activateTfod();
        TargetZone z = TargetZone.UNKNOWN;
        wobbleDropper.setPosition(0.0);
        while (!isStarted()) {
            List<Recognition> updatedRecognitions = webcam.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                // step through the list of recognitions and display boundary info.
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals("Quad")) {
                        z = TargetZone.C;
                    } else if (recognition.getLabel().equals("Single")) {
                        z = TargetZone.B;
                    } else {
                        z = TargetZone.A;
                    }
                    telemetry.addData("Object", recognition.getLabel());
                    telemetry.addData("Object Label", z);
//                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
//                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
//                            recognition.getLeft(), recognition.getTop());
//                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
//                            recognition.getRight(), recognition.getBottom());
                }
                telemetry.update();
            }
//            System.out.println(webcam.getUpdatedRecognitions());
//            z = webcam.autoInitDetect();
////            if (z != TargetZone.UNKNOWN) {
////                break;
////            }
//            System.out.println(z);
        }
        waitForStart();
        OdometryThread.initialize(42, backLeft, backRight, frontRight, this::opModeIsActive);
        if (z == TargetZone.UNKNOWN) z = TargetZone.A;
        if (!opModeIsActive()) {
            return;
        }
        pusher.setPosition(1);
        switch (z) {
            case C:
                odometryMove(28, 107, true, 6);
                break;
            case B:
                odometryMove(40, 87, true, 6);
                break;
            case A:
                odometryMove(28, 67, true, 6);
                break;
        }
        wobbleArm.setPosition(1.0);
        sleep(500);
        wobbleDropper.setPosition(1.0);
        sleep(500);
        wobbleArm.setPosition(0.0);
        odometryMove(30, 58, true, 4);
        odometryTurn(-10, true);
        flywheel1.setPower(1.0);
        flywheel2.setPower(1.0);
        sleep(1000);
//        System.out.println(shooterThread.getMaxRps());
        shoot();
        sleep(500);
        flywheel1.setPower(0.0);
        flywheel2.setPower(0.0);
//        odometryMove(49, 14);
        odometryTurn(-15, true);
        flywheel1.setPower(1.0);
        flywheel2.setPower(1.0);
        sleep(1000);
//        System.out.println(shooterThread.getMaxRps());
        shoot();
        sleep(500);
        flywheel1.setPower(0.0);
        flywheel2.setPower(0.0);
//        odometryMove(getCurrentPos().getX()+6, getCurrentPos().getY());
        odometryTurn(-20, true);
        flywheel1.setPower(1.0);
        flywheel2.setPower(1.0);
        sleep(1000);
//        System.out.println(shooterThread.getMaxRps());
//        odometryMove(65, 12);
//        odometryTurn(40, 128);
        shoot();
        sleep(500);
        flywheel1.setPower(0.0);
        flywheel2.setPower(0.0);
        odometryMove(-1, 60, false, 3);
        OdometryThread.getInstance().stopThread();
    }
}
