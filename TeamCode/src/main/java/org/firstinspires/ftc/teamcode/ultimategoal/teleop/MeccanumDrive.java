/****
 * Made by Tejas Mehta
 * Made on Saturday, December 05, 2020
 * File Name: TestDrive
 * Package: org.firstinspires.ftc.teamcode.testCode*/
package org.firstinspires.ftc.teamcode.ultimategoal.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.tejasmehta.OdometryCore.localization.OdometryPosition;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.teamcode.ultimategoal.pathfinder.PathfinderConstants;
import org.firstinspires.ftc.teamcode.ultimategoal.util.OdometryThread;
import org.firstinspires.ftc.teamcode.ultimategoal.util.ShooterThread;
import org.firstinspires.ftc.teamcode.ultimategoal.util.Toggle;

@TeleOp(name = "Actual Meccanum", group = "Test")
public class MeccanumDrive extends OpMode {
    private static final HeadingPoint TARGET_A = HeadingPoint.ZERO;
    private static final HeadingPoint TARGET_B = HeadingPoint.ZERO;
    private static final HeadingPoint TARGET_X = HeadingPoint.ZERO;
    private static final HeadingPoint TARGET_Y = HeadingPoint.ZERO;

    private boolean aPressedLast = false;
    private boolean bPressedLast = false;
    private boolean xPressedLast = false;
    private boolean yPressedLast = false;

    private boolean aPressed = false;
    private boolean bPressed = false;
    private boolean xPressed = false;
    private boolean yPressed = false;

    private HeadingPoint target = HeadingPoint.ZERO;
    private boolean mustReset = false;

    Pathfinder pathfinder;

    DcMotor frontRight;

    DcMotor frontLeft;

    DcMotor backRight;

    DcMotor backLeft;

    DcMotor flywheel1;

    DcMotor flywheel2;

    DcMotor intake;

    CRServo intakeServo;

    CRServo upperIntakeServo;

    Servo intakeMover;

    Servo loader;

    Servo pusher;

    DcMotor wobbleArm;

    Servo wobbleDropper;

    //    ColorSensor bottomSensor;
//    ColorSensor topSensor;
    double multiplier = 0.5;

    Toggle t = new Toggle();

    Toggle loadToggle = new Toggle();

    Toggle pushToggle = new Toggle();

    Toggle wobbleToggle = new Toggle();

    //    boolean moveUpper = true;
    ShooterThread shooterThread;

    @Override
    public void init() {
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        flywheel1 = hardwareMap.get(DcMotor.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotor.class, "flywheel2");
        intake = hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeServo = hardwareMap.get(CRServo.class, "intakeServo");
        upperIntakeServo = hardwareMap.get(CRServo.class, "upperIntakeServo");
        intakeMover = hardwareMap.get(Servo.class, "intakeMover");
        loader = hardwareMap.get(Servo.class, "loader");
        pusher = hardwareMap.get(Servo.class, "pusher");
        wobbleArm = hardwareMap.get(DcMotor.class, "wobbleArm");
        wobbleDropper = hardwareMap.get(Servo.class, "wobbleDropper");
//        bottomSensor = hardwareMap.get(ColorSensor.class, "bottomSensor");
//        topSensor = hardwareMap.get(ColorSensor.class, "topSensor");
        flywheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        t.state = false;
        loadToggle.state = false;
        pushToggle.state = false;
        wobbleToggle.state = true;
        intakeMover.setPosition(0.33);
//        wobbleArm.setPosition(0.25);
        shooterThread = new ShooterThread(flywheel2);
        shooterThread.start();
        try {
            OdometryThread.getInstance();
        } catch (Exception e) {
            OdometryThread.initialize(42, backLeft, backRight, frontRight, () -> true);
        }
        PathfinderConstants.initializeMotors(hardwareMap);
        PathfinderConstants.initializePathfinder(() -> false);
        pathfinder = PathfinderConstants.getPathfinder();
        PathfinderConstants.getChassisTracker().setOffset(new Point(35, 0));
    }

    public boolean pressed(boolean current,
                           boolean last) {
        return current && !last;
    }

    public void updateButtons(boolean a,
                              boolean b,
                              boolean x,
                              boolean y) {
        aPressed = pressed(a, aPressedLast);
        bPressed = pressed(b, bPressedLast);
        xPressed = pressed(x, xPressedLast);
        yPressed = pressed(y, yPressedLast);

        aPressedLast = a;
        bPressedLast = b;
        xPressedLast = x;
        yPressedLast = y;
    }

    public void updatePathfinder() {
        if (mustReset) {
            // if the pathfinder needs to reset its path
            if (pathfinder.getManager().getExecutor().isEmpty()) {
                // if the exec is empty we need to regen a path
                pathfinder.followPath(new DynamicArray<>(
                        HeadingPoint.pointOrIfNullZero(
                                pathfinder.getPosition()
                        ),
                        target
                ));

                // pathfinder shouldn't reset anymore
                mustReset = false;
            } else {
                // clear the pathfinder, recurse
                pathfinder.getManager().getExecutor().clear();

                updatePathfinder();
            }
        }
    }

    @Override
    public void loop() {
        // update each of the button's press status
        // buttons can only be "pressed" once
        // they're only considered to be "pressed" the first execution of
        // the loop when they're pressed, afterwards they can be reset and
        // re-pressed if that makes sense???
        updateButtons(
                gamepad1.a,
                gamepad1.b,
                gamepad1.x,
                gamepad1.y
        );

        // if any of the buttons are pressed we have to reset
        if (aPressed || bPressed || xPressed || yPressed) mustReset = true;

        // set the pathfinder's target based on whatever point is used
        if (aPressed) {
            target = TARGET_A;
        } else if (bPressed) {
            target = TARGET_B;
        } else if (xPressed) {
            target = TARGET_X;
        } else if (yPressed) {
            target = TARGET_Y;
        }

        // update and tick the pathfinder
        updatePathfinder();
        pathfinder.tick();

        if (gamepad1.left_trigger != 0 && gamepad1.right_trigger == 0) {
            multiplier = 0.25;
        } else {
            multiplier = 0.5;
        }

        if (gamepad1.left_trigger == 0 && gamepad1.right_trigger != 0) {
            multiplier = 1;
        } else {
            multiplier = 0.5;
        }

        double fr = -gamepad1.left_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x;
        double br = -gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x;
        double fl = gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x;
        double bl = gamepad1.left_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x;
        frontRight.setPower(fr * multiplier);
        backRight.setPower(br * multiplier);
        frontLeft.setPower(fl * multiplier);
        backLeft.setPower(bl * multiplier);


//        if (bottomSensor.red() > 1000) {
//            moveUpper = false;
//        }
//
//        if (topSensor.red() > 1000) {
//            moveUpper = true;
//        }
//        telemetry.addData("s1 Red", bottomSensor.red());
//        telemetry.addData("s2 red", topSensor.red());
//        telemetry.addData("mover", moveUpper);
        if (gamepad2.a) {
            t.onPress();
        } else {
            t.onRelease();
        }

        if (gamepad2.x) {
            wobbleToggle.onPress();
        } else {
            wobbleToggle.onRelease();
        }

        if (gamepad2.dpad_up) {
            wobbleArm.setPower(0.5);
        } else if (gamepad2.dpad_down) {
            wobbleArm.setPower(-0.5);
        } else {
            wobbleArm.setPower(0);
        }


//        if (gamepad2.b) {
//            loadToggle.onPress();
//        } else {
//            loadToggle.onRelease();
//        }

        if (gamepad2.right_bumper) {
            pusher.setPosition(0.65);
        } else {
            pusher.setPosition(1);
        }
//
//        if (pushToggle.state) {
//        } else {
//            pusher.setPosition(1);
//        }

//        if (loadToggle.state) {
//            loader.setPosition((180.0-36.0)/180.0);
//        } else {
//            loader.setPosition(1);
//        }

        if (gamepad2.left_trigger > gamepad2.right_trigger && gamepad2.left_trigger > 0.3) {
            intake.setPower(1.0);
            intakeServo.setPower(0.8);
            System.out.println("Move HEre");
            upperIntakeServo.setPower(0.8);
        } else if (gamepad2.right_trigger > gamepad2.left_trigger && gamepad2.right_trigger > 0.3) {
            loader.setPosition(1);
            intake.setPower(-1);
            intakeServo.setPower(-0.8);
            upperIntakeServo.setPower(-0.8);
//            if (moveUpper) {
//                System.out.println("SHOULD BE MOVING");
//            }
        } else {
            loader.setPosition((180.0 - 36.0) / 180.0);
            intake.setPower(0);
            intakeServo.setPower(0);
            upperIntakeServo.setPower(0);
        }

        if (wobbleToggle.state) {
            wobbleDropper.setPosition(1.0);
        } else {
            wobbleDropper.setPosition(0.0);
        }

        if (t.state) {
//            double neededVel = calculateMissing(true, 27);
//            if (neededVel == -1) {
//                System.out.println("BAD");
//            }
//            spinToSpeed(neededVel);
            flywheel1.setPower(1.0);
            flywheel2.setPower(1.0);
        } else {
            flywheel1.setPower(0);
            flywheel2.setPower(0);
        }

    }

    @Override
    public void stop() {
        super.stop();
        shooterThread.stopThread();
    }

    void spinToSpeed(double neededVelocity) {
        double speed = 0.7;
        double desiredRps = (5000.0 / 37.0) * 27;
        do {
            speed += 0.05;
            flywheel1.setPower(speed);
            flywheel2.setPower(speed);
        } while (desiredRps < shooterThread.getSpeed() && shooterThread.getSpeed() < shooterThread.getMaxRps());
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
    double calculateMissing(boolean vMode, double yDist) {
        double height = (yDist / 39.37) - .258823;
        double startToGoal = 135.5;
        double angleRads = Math.toRadians(45);
        double cosCalc = Math.cos(angleRads);
        double tanCalc = Math.tan(angleRads);
        if (vMode) {
            double distToGoal = (startToGoal - getCurrentPos().getY() - 55.5) / 39.37;
            double constant = -4.9 * (distToGoal * distToGoal);
            double vVal = (height * cosCalc) - (cosCalc * distToGoal * tanCalc);
            double rootable = constant / vVal;
            if (rootable < 0) {
                return -1;
            }
            return Math.sqrt(rootable);
        } else {
            double speed = shooterThread.getSpeed();
            double aVal = 4.9;
            double bVal = -speed * Math.sin(angleRads);
            double cVal = -.258823;
            double root = (bVal * bVal) - 4 * aVal * cVal;
            double rooted = Math.sqrt(root);
            double sol1 = (-bVal + rooted) / (2 * aVal);
            double sol2 = (-bVal - rooted) / (2 * aVal);
            double t = Math.max(sol1, sol2);
            return speed * cosCalc * t;
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
        }
    }

    public OdometryPosition getCurrentPos() {
        OdometryPosition currentPosition = OdometryThread.getInstance().getCurrentPosition();
        return OdometryThread.getInstance().getCurrentPosition();
    }
}
