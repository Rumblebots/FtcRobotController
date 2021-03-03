/****
 * Made by Tejas Mehta
 * Made on Tuesday, March 02, 2021
 * File Name: AutoVision
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton;

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

public class AutoVision extends LinearOpMode {

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

    public OdometryPosition getCurrentPos() {
        OdometryPosition currentPosition = OdometryThread.getInstance().getCurrentPosition();
        telemetry.addData("Odometry Pos: ", "x: " + currentPosition.getX() + ", y: " + currentPosition.getY() + ", heading: " + currentPosition.getHeadingDegrees());
        telemetry.update();
        return OdometryThread.getInstance().getCurrentPosition();
    }

    public void odometryMove(double x, double y, boolean correct) {
        boolean initialy = true;
        if (y + 2 > getCurrentPos().getY()) {
            double power = 0.3;
            double initialPos = getCurrentPos().getY();
            double tenths = (y - getCurrentPos().getY())/10;
            while (y > getCurrentPos().getY()) {
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
            while (backDist < getCurrentPos().getY()) {
                if (!initialy && !correct) {
                    break;
                }
                meccanumDrive(MotionType.BACKWARD, initialy ? 0.5 : 0.3);
            }
        }
        if (x == -1) {
            return;
        }
        if (x + 2 > getCurrentPos().getX()) {
            setPower(0, 0, 0, 0);
            sleep(500);
            while (x > getCurrentPos().getX()) {
                meccanumDrive(MotionType.RIGHT, 0.6);
            }
        } else if (x - 2 < getCurrentPos().getX()){
            setPower(0, 0, 0, 0);
            sleep(500);
            while (x < getCurrentPos().getX()) {
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
        webcam.init(hardwareMap);
        TargetZone z = TargetZone.UNKNOWN;
        while (!isStarted()) {
            z = webcam.autoInitDetect();
            if (z != webcam.autoInitDetect()) {
                break;
            }
        }
        OdometryThread.initialize(42, backLeft, backRight, frontRight, this::opModeIsActive);
        getCurrentPos();
        waitForStart();
        if (z == TargetZone.UNKNOWN) z = TargetZone.B;
        if (!opModeIsActive()) {
            return;
        }
        pusher.setPosition(1);
        odometryMove(40, 120, true);
        wobbleDropper.setPosition(0);
//        wobbleDropper.setPosition(1);
//        odometryTurn(18, false);
        odometryMove(80, 54, true);
        odometryTurn(18, false);
        flywheel1.setPower(1.0);
        flywheel2.setPower(1.0);
        sleep(1500);
//        System.out.println(shooterThread.getMaxRps());
        shoot();
        sleep(500);
        flywheel1.setPower(0.0);
        flywheel2.setPower(0.0);
//        odometryMove(49, 14);
        odometryTurn(5, false);
//        odometryMove(getCurrentPos().getX()+6, getCurrentPos().getY()-1);
//        double neededVel = calculateMissing(true, 27);
//        if (neededVel == -1) {
//            System.out.println("BAD");
//        }
//        spinToSpeed(neededVel);
        flywheel1.setPower(1.0);
        flywheel2.setPower(1.0);
        sleep(1500);
//        System.out.println(shooterThread.getMaxRps());
        shoot();
        sleep(500);
        flywheel1.setPower(0.0);
        flywheel2.setPower(0.0);
//        odometryMove(getCurrentPos().getX()+6, getCurrentPos().getY());
        odometryTurn(6, false);
        flywheel1.setPower(1.0);
        flywheel2.setPower(1.0);
        sleep(1500);
//        System.out.println(shooterThread.getMaxRps());
//        odometryMove(65, 12);
//        odometryTurn(40, 128);
        shoot();
        sleep(500);
        flywheel1.setPower(0.0);
        flywheel2.setPower(0.0);
        odometryMove(-1, 60, false);
        OdometryThread.getInstance().stopThread();
    }
}
