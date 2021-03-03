/****
 * Made by Tejas Mehta
 * Made on Thursday, October 15, 2020
 * File Name: AutoRed
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.tejasmehta.OdometryCore.localization.OdometryPosition;
import org._11253.lib.robot.phys.assm.drivetrain.MotionType;
import org.firstinspires.ftc.teamcode.ultimategoal.shared.ShooterThread;
import org.firstinspires.ftc.teamcode.ultimategoal.shared.subystems.OdometryThread;

@Autonomous(name = "Main Auo", group = "Auton")

public class GenericAuto extends LinearOpMode {

//    public Drivetrain drivetrain = new Drivetrain();
//    public Shooter shooter = new Shooter(new Storage(), 0, 0, 0, 0, 0, 0);

    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    DcMotor flywheel1;
    DcMotor flywheel2;
//    DcMotor intake;
//    CRServo intakeServo;
//    CRServo upperIntakeServo;
    Servo loader;
    Servo pusher;

//    public GenericAuto(final double offsetPos) {
//        onFinish.add(new Runnable() {
//            @Override
//            public void run() {
//                OdometryThread.getInstance().stopThread();
//            }
//        });
//    }

    public OdometryPosition getCurrentPos() {
        OdometryPosition currentPosition = OdometryThread.getInstance().getCurrentPosition();
        telemetry.addData("Odometry Pos: ", "x: " + currentPosition.getX() + ", y: " + currentPosition.getY() + ", heading: " + currentPosition.getHeadingDegrees());
        telemetry.update();
        return OdometryThread.getInstance().getCurrentPosition();
    }

    public void odometryMove(double x, double y) {
        if (y + 2 > getCurrentPos().getY()) {
            while (y > getCurrentPos().getY()) {
                meccanumDrive(MotionType.FORWARD, 0.3);
            }
        } else if (y - 2 < getCurrentPos().getY()){
            while (y < getCurrentPos().getY()) {
                meccanumDrive(MotionType.BACKWARD, 0.5);
            }
        }

        if (x + 2 > getCurrentPos().getX()) {
            while (x > getCurrentPos().getX()) {
                meccanumDrive(MotionType.RIGHT, 0.25);
            }
        } else if (x - 2 < getCurrentPos().getX()){
            while (x < getCurrentPos().getX()) {
                meccanumDrive(MotionType.LEFT, 0.25);
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
//        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    telemetry.addData("RPM", getRPM());
//                }
//            }
//        });
//        t.start();
//        intake = hardwareMap.get(DcMotor.class, "intake");
//        intakeServo = hardwareMap.get(CRServo.class, "intakeServo");
        loader = hardwareMap.get(Servo.class, "loader");
        pusher = hardwareMap.get(Servo.class, "pusher");
        OdometryThread.initialize(42, backLeft, backRight, frontRight);
        ShooterThread shooterThread = new ShooterThread(flywheel2);
        shooterThread.start();
        getCurrentPos();
        waitForStart();
        pusher.setPosition(1);
        odometryMove(getCurrentPos().getX()-2, 54);
//        odometryTurn(45, 126);
        flywheel1.setPower(1.0);
        flywheel2.setPower(1.0);
        sleep(1500);
        System.out.println(shooterThread.getMaxRps());
        shoot();
        sleep(500);
        flywheel1.setPower(0.0);
        flywheel2.setPower(0.0);
//        odometryMove(49, 14);
//        odometryTurn(40, 128);
        odometryMove(getCurrentPos().getX()+6, getCurrentPos().getY()-1);
//        double neededVel = calculateMissing(true, 27);
//        if (neededVel == -1) {
//            System.out.println("BAD");
//        }
//        spinToSpeed(neededVel);
        flywheel1.setPower(1.0);
        flywheel2.setPower(1.0);
        sleep(1500);
        System.out.println(shooterThread.getMaxRps());
        shoot();
        sleep(500);
        flywheel1.setPower(0.0);
        flywheel2.setPower(0.0);
        odometryMove(getCurrentPos().getX()+6, getCurrentPos().getY());
        flywheel1.setPower(1.0);
        flywheel2.setPower(1.0);
        sleep(1500);
        System.out.println(shooterThread.getMaxRps());
//        odometryMove(65, 12);
//        odometryTurn(40, 128);
        shoot();
        sleep(500);
        flywheel1.setPower(0.0);
        flywheel2.setPower(0.0);
        odometryMove(getCurrentPos().getX(), 60);
        OdometryThread.getInstance().stopThread();
    }

    // Angle: 45
    // Horizontal from center (x dist): 144.5 mm
    // Vertical to bottom (offset): 258.823 mm -- 10.18in -- .258823 m
    // Goal to top 33 in, 35.5 to middle of top goal
    // Goal to mid 21 in, 27 to middle of middle goal
    // yDist = 30
    // xDist = Calculated below
    // Equation: .64(v * cos(45))^2 - (v * cos(45))^2 * x + 4.9x^2 = 0 <-- Solve for distance
    // Equation: (v * cos(45))^2 = -4.9x^2 / (.64 - x) <-- Solve for velocity
//    double calculateMissing(boolean vMode, double yDist) {
//        double height = (yDist/3.281) - .258823;
//        double startToGoal = 135.5;
//        double angleRads = Math.toRadians(45);
//        double cosCalc = Math.cos(angleRads);
//        double tanCalc = Math.tan(angleRads);
//        if (vMode) {
//            double distToGoal = (startToGoal - getCurrentPos().getY())/3.281;
//            double constant = -4.9 * (distToGoal * distToGoal);
//            double vVal = (height * cosCalc) - (cosCalc * distToGoal * tanCalc);
//            double rootable = constant/vVal;
//            if (rootable < 0) {
//                return -1;
//            }
//            return Math.sqrt(rootable);
//        } else {
//            double speed = shooterThread.getSpeed();
//            double constant = height * Math.pow(speed * cosCalc, 2); // C value in a quadratic
//            double singleCoefficient = Math.pow(speed * cosCalc * tanCalc, 2); // B value in a quadratic
//            double quadraticCoefficient = 4.9;
//            double radicand = (singleCoefficient * singleCoefficient) - (4 * quadraticCoefficient * constant);
//            if (radicand < 0) {
//                return -1;
//            }
//            double rooted = Math.sqrt(radicand);
//            double sol1 = ((-singleCoefficient + rooted)/(2 * quadraticCoefficient)) * 3.281;
//            double sol2 = ((-singleCoefficient - rooted)/(2 * quadraticCoefficient)) * 3.281;
//            return Math.max(sol1, sol2);
//        }
//    }

//    void spinToSpeed(double neededVelocity) {
//        double speed = 0.7;
//        while (neededVelocity > shooterThread.getSpeed()) {
//            speed+=0.05;
//            flywheel1.setPower(speed);
//            flywheel2.setPower(speed);
//        }
//    }
}
