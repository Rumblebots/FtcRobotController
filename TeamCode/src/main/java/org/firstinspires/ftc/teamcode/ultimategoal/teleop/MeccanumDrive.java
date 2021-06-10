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
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.tejasmehta.OdometryCore.localization.OdometryPosition;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import org.firstinspires.ftc.teamcode.recorder.Recorder;
import org.firstinspires.ftc.teamcode.ultimategoal.pathfinder.PathfinderConstants;
import org.firstinspires.ftc.teamcode.ultimategoal.util.OdometryThread;
import org.firstinspires.ftc.teamcode.ultimategoal.util.ShooterThread;
import org.firstinspires.ftc.teamcode.ultimategoal.util.Toggle;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@TeleOp(name = "Actual Meccanum", group = "Test")
public class MeccanumDrive extends OpMode {
    private static final HeadingPoint RED_PS_L = new HeadingPoint(48 + 5.5 + 2, 144, 0);
    private static final HeadingPoint RED_PS_M = new HeadingPoint(48 + 5.5 + 7.5 + 7.5, 144, 0);
    private static final HeadingPoint RED_PS_R = new HeadingPoint(48 + 5.5 + 7.5 + 7.5 + 7.5, 144, 0);

    private static final HeadingPoint BLUE_PS_L = new HeadingPoint(48 + 5.5 + 2 + 24, 144, 0);
    private static final HeadingPoint BLUE_PS_M = new HeadingPoint(48 + 5.5 + 7.5 + 7.5 + 24, 144, 0);
    private static final HeadingPoint BLUE_PS_R = new HeadingPoint(48 + 5.5 + 7.5 + 7.5 + 7.5 + 24, 144, 0);

    private static final HeadingPoint BLUE_HI = new HeadingPoint(120, 144 + 24 - 8, 0);
    // todo fix this lol
    private static final HeadingPoint RED_HI = new HeadingPoint(48 - 12, 144 + 24 - 8, 0);

    private boolean aPressedLast = false;
    private boolean bPressedLast = false;
    private boolean xPressedLast = false;
    private boolean yPressedLast = false;

    private boolean aPressed = false;
    private boolean bPressed = false;
    private boolean xPressed = false;
    private boolean yPressed = false;

    private boolean isPathfinderActive = false;
    private HeadingPoint target = HeadingPoint.ZERO;
    private boolean mustReset = false;

    private enum Color {
        RED,
        BLUE
    }

    private Color color = Color.BLUE;

    Pathfinder pathfinder;
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    DcMotorEx flywheel1;
    DcMotorEx flywheel2;
    DcMotor intake;
    CRServo intakeServo;
    CRServo upperIntakeServo;
    Servo intakeMover;
    Servo loader;
    Servo pusher;
    DcMotor wobbleArm;
    Servo wobbleDropper;
    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    boolean canStop = true;
    //    ColorSensor bottomSensor;
//    ColorSensor topSensor;
    double multiplier = 0.5;
    Toggle t = new Toggle();
    Toggle loadToggle = new Toggle();
    Toggle pushToggle = new Toggle();
    Toggle wobbleToggle = new Toggle();


    //    boolean moveUpper = true;
    ShooterThread shooterThread;
    private Recorder recorder;
    private boolean hasStopped = false;

    /**
     * initialize everything
     *
     * <p>
     * <ul>
     *     <li>{@code gamepad1.a}: set color to BLUE</li>
     *     <li>{@code gamepad1.b}: set color to RED</li>
     *     <li>default: set color to BLUE</li>
     * </ul>
     * </p>
     */
    @Override
    public void init() {
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        initializeFlywheels();
//        flywheel1 = hardwareMap.get(DcMotor.class, "flywheel1");
//        flywheel2 = hardwareMap.get(DcMotor.class, "flywheel2");
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
//        flywheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        flywheel2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        t.state = false;
        loadToggle.state = false;
        pushToggle.state = false;
        wobbleToggle.state = false;
        wobbleDropper.setPosition(0.0);
//        intakeMover.setPosition(0.33);
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
//        PathfinderConstants.getChassisTracker().setOffset(new Point(35, 0));

        if (gamepad1.dpad_up) {
            this.color = Color.BLUE;
        }

        if (gamepad1.dpad_down) {
            this.color = Color.RED;
        }

        recorder = new Recorder(
                pathfinder,
                "manual.json"
        );
        recorder.start(() -> true);
    }

    void initializeFlywheels() {
        flywheel1 = hardwareMap.get(DcMotorEx.class, "flywheel1");
        flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");
        MotorConfigurationType flywheel1Config = flywheel1.getMotorType().clone();
        flywheel1Config.setAchieveableMaxRPMFraction(1.0);
        flywheel1.setMotorType(flywheel1Config);

        MotorConfigurationType flywheel2Config = flywheel2.getMotorType().clone();
        flywheel2Config.setAchieveableMaxRPMFraction(1.0);
        flywheel2.setMotorType(flywheel2Config);

        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        System.out.println("FW1 PID: " + flywheel1.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
        System.out.println("FW2 PID: " + flywheel2.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
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
            if (pathfinder.getManager().getExecutor().isEmpty()) {
                pathfinder.followPath(new DynamicArray<>(
                        HeadingPoint.pointOrIfNullZero(
                                pathfinder.getPosition()
                        ),
                        target
                ));
                mustReset = false;
            } else {
                pathfinder.getManager().getExecutor().clear();
                updatePathfinder();
            }
        }
        isPathfinderActive = !pathfinder.getManager().getExecutor().isEmpty();
    }

    @Override
    public void loop() {
//        recorder.record();

        if (!hasStopped) {
            if (gamepad1.dpad_right) {
                recorder.stop();
                recorder.export();
                hasStopped = true;
            }
        }

        updateButtons(
                gamepad1.a,
                gamepad1.b,
                gamepad1.x,
                gamepad1.y
        );

        if (aPressed || bPressed || xPressed || yPressed) mustReset = true;

        if (mustReset && !isPathfinderActive) {
            switch (this.color) {
                case RED: {
                    align(RED_PS_L, RED_PS_M, RED_PS_R, RED_HI);
                    break;
                }

                case BLUE: {
                    align(BLUE_PS_L, BLUE_PS_M, BLUE_PS_R, BLUE_HI);
                    break;
                }

                default:
                    throw new UnsupportedOperationException("");
            }
        }

        if (gamepad1.right_bumper) {
            pathfinder.getManager().getExecutor().clear();
        }

        if (gamepad1.start) {
            PathfinderConstants.resetMotors();
            HeadingPoint current = pathfinder.getPosition();
            HeadingPoint offset = PathfinderConstants.getChassisTracker().getOffset();
            PathfinderConstants.getChassisTracker().setOffset(
                    new HeadingPoint(
                            (current.getX() * -1) + 9 + offset.getX(),
                            (current.getY() * -1) + 9 + offset.getY(),
                            (current.getHeading() * -1) + offset.getHeading()
                    )
            );
        }

        updatePathfinder();
        pathfinder.tick();

        if (!isPathfinderActive) {
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
        }

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

        if (gamepad2.right_bumper && t.state) {
            pusher.setPosition(0.65);
            canStop = false;
            exec.schedule(() -> {
                canStop = true;
            }, 300, TimeUnit.MILLISECONDS);
        } else {
            if (canStop) {
                pusher.setPosition(1);
            }
        }

        if (gamepad2.left_trigger > gamepad2.right_trigger && gamepad2.left_trigger > 0.3) {
            intakeMover.setPosition(1.0);
            intake.setPower(1.0);
            intakeServo.setPower(0.8);
            System.out.println("Move HEre");
            upperIntakeServo.setPower(0.8);
        } else if (gamepad2.right_trigger > gamepad2.left_trigger && gamepad2.right_trigger > 0.3) {
            intakeMover.setPosition(0.0);
            loader.setPosition(1);
            intake.setPower(-1);
            intakeServo.setPower(-0.8);
            upperIntakeServo.setPower(-0.8);
        } else {
            intakeMover.setPosition(1.0);
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
            flywheel1.setVelocity(2800);
            flywheel2.setVelocity(2800);
//            System.out.println("CVEL: " + flywheel1.getVelocity());
//            System.out.println("CVEL2z: " + flywheel2.getVelocity());
//            System.out.println("MAX FLYWHEEL 1: " + flywheel1.getMotorType().getAchieveableMaxTicksPerSecond());
//            System.out.println("MAX FLYWHEEL 2: " + flywheel2.getMotorType().getAchieveableMaxTicksPerSecond());
        } else {
            flywheel1.setPower(0);
            flywheel2.setPower(0);
        }

        telemetry.addData("pos", pathfinder.getPosition().toString());
        telemetry.update();
    }

    private void align(HeadingPoint positionA, HeadingPoint positionB, HeadingPoint positionX, HeadingPoint positionY) {
        if (aPressed) {
            target = RadiusFinder.closestTargetPoint(
                    pathfinder.getPosition(),
                    positionA
            );
        } else if (bPressed) {
            target = RadiusFinder.closestTargetPoint(
                    pathfinder.getPosition(),
                    positionB
            );
        } else if (xPressed) {
            target = RadiusFinder.closestTargetPoint(
                    pathfinder.getPosition(),
                    positionX
            );
        } else if (yPressed) {
            // shouldn't really be used, we only have 3 power shots
            target = RadiusFinder.closestTargetPoint(
                    pathfinder.getPosition(),
                    positionY
            );
        }
        return;
    }

    @Override
    public void stop() {
        super.stop();
//        recorder.stop();
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
