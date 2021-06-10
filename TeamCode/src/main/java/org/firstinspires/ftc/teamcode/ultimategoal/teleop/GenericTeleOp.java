package org.firstinspires.ftc.teamcode.ultimategoal.teleop;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ultimategoal.pathfinder.PathfinderConstants;
import org.firstinspires.ftc.teamcode.ultimategoal.util.Toggle;

/**
 * Generic & shared tele-op. Should be instantiated as a member of
 * implementing op modes.
 */
public class GenericTeleOp {
    private static final String N_FR = "frontRight";
    private static final String N_FL = "frontLeft";
    private static final String N_BR = "backRight";
    private static final String N_BL = "backLeft";

    private static final String N_FLY_1 = "flywheel1";
    private static final String N_FLY_2 = "flywheel2";

    private static final String N_INTAKE = "intakeMotor";
    private static final String N_INTAKE_LOWER = "intakeServo";
    private static final String N_INTAKE_UPPER = "upperIntakeServo";
    private static final String N_INTAKE_MOVER = "intakeMover";
    private static final String N_SHOOTER_LOADER = "loader";
    private static final String N_SHOOTER_PUSHER = "pusher";
    private static final String N_WOBBLE_ARM = "wobbleArm";
    private static final String N_WOBBLE_DROPPER = "wobbleDropper";

    private static final double P_WOBBLE_ARM_U = +0.5;
    private static final double P_WOBBLE_ARM_D = -0.5;
    private static final double P_WOBBLE_ARM_N = 0.0D;
    private static final double P_WOBBLE_DROPPER_ON = 1.0;
    private static final double P_WOBBLE_DROPPER_OFF = 0.0;

    private static final double P_FLY_1_VEL_ON = 2800;
    private static final double P_FLY_2_VEL_ON = 2800;
    private static final double P_FLY_1_VEL_OFF = 0;
    private static final double P_FLY_2_VEL_OFF = 0;

    private static final double P_PUSHER_ACTIVE = 0.65;
    private static final double P_PUSHER_INACTIVE = 1D;

    private static final double P_INTAKE_MOVER_IN = 0.0;
    private static final double P_LOADER_IN = 1.0;
    private static final double P_INTAKE_IN = -1.0;
    private static final double P_LOWER_IN = -0.8;
    private static final double P_UPPER_IN = -0.8;

    private static final double P_INTAKE_MOVER_OUT = 1.0;
    private static final double P_LOADER_OUT = (180D - 36) / 180;
    private static final double P_INTAKE_OUT = 1.0;
    private static final double P_LOWER_OUT = 0.8;
    private static final double P_UPPER_OUT = 0.8;

    private static final double P_INTAKE_MOVER = 1.0;
    private static final double P_LOADER = (180D - 36) / 180;
    private static final double P_INTAKE = 0D;
    private static final double P_LOWER = 0D;
    private static final double P_UPPER = 0D;

    private final HardwareMap map;
    private final AutoAlignPoints points;
    private final Gamepad gp1;
    private final Gamepad gp2;
    private final Telemetry telemetry;

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

    private Pathfinder pathfinder;
    private DcMotor fr;
    private DcMotor fl;
    private DcMotor br;
    private DcMotor bl;

    private DcMotorEx flywheel1;
    private DcMotorEx flywheel2;

    private DcMotor intake;
    private CRServo lowerIntake;
    private CRServo upperIntake;
    private Servo intakeMover;

    private Servo loader;
    private Servo pusher;
    private DcMotor wobbleArm;
    private Servo wobbleDropper;

    double multiplier = 0.5;

    private final Toggle t = new Toggle(false);
    private final Toggle loadToggle = new Toggle(false);
    private final Toggle pushToggle = new Toggle(false);
    private final Toggle wobbleToggle = new Toggle(false);

    public GenericTeleOp(HardwareMap map,
                         AutoAlignPoints points,
                         Gamepad gp1,
                         Gamepad gp2,
                         Telemetry telemetry) {
        this.map = map;
        this.points = points;
        this.gp1 = gp1;
        this.gp2 = gp2;
        this.telemetry = telemetry;
    }

    /*
     * hardware initialization
     */

    private void initDrive() {
        fr = map.get(DcMotor.class, N_FR);
        fl = map.get(DcMotor.class, N_FL);
        br = map.get(DcMotor.class, N_BR);
        bl = map.get(DcMotor.class, N_BL);
    }

    private void initShooter() {
        flywheel1 = map.get(DcMotorEx.class, N_FLY_1);
        flywheel2 = map.get(DcMotorEx.class, N_FLY_2);

        MotorConfigurationType c_flywheel1 = flywheel1.getMotorType().clone();
        MotorConfigurationType c_flywheel2 = flywheel2.getMotorType().clone();

        c_flywheel1.setAchieveableMaxRPMFraction(1.0);
        c_flywheel2.setAchieveableMaxRPMFraction(1.0);

        flywheel1.setMotorType(c_flywheel1);
        flywheel2.setMotorType(c_flywheel2);

        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        loader = map.get(Servo.class, N_SHOOTER_LOADER);
        pusher = map.get(Servo.class, N_SHOOTER_PUSHER);
    }

    private void initIntake() {
        intake = map.get(DcMotor.class, N_INTAKE);
        lowerIntake = map.get(CRServo.class, N_INTAKE_LOWER);
        upperIntake = map.get(CRServo.class, N_INTAKE_UPPER);
        intakeMover = map.get(Servo.class, N_INTAKE_MOVER);
    }

    private void initWobble() {
        wobbleArm = map.get(DcMotor.class, N_WOBBLE_ARM);
        wobbleDropper = map.get(Servo.class, N_WOBBLE_DROPPER);

        wobbleDropper.setPosition(0.0);
    }

    private void initHardware() {
        initDrive();
        initShooter();
        initIntake();
        initWobble();
    }

    /*
     * everything else
     */

    private void initStuff() {
        PathfinderConstants.initializeMotors(map);
        PathfinderConstants.initializePathfinder(() -> false);
        pathfinder = PathfinderConstants.getPathfinder();
    }

    /*
     * actual initialization
     */

    public void init() {
        initHardware();
        initStuff();
    }

    private boolean pressed(boolean current,
                            boolean last) {
        return current && !last;
    }

    private void updateButtons(boolean a,
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

    private void align(boolean a,
                       boolean b,
                       boolean x,
                       boolean y) {
        if (a) target = RadiusFinder.closestTargetPoint(
                pathfinder.getPosition(),
                points.L
        );
        else if (b) target = RadiusFinder.closestTargetPoint(
                pathfinder.getPosition(),
                points.M
        );
        else if (x) target = RadiusFinder.closestTargetPoint(
                pathfinder.getPosition(),
                points.R
        );
        else if (y) target = RadiusFinder.closestTargetPoint(
                pathfinder.getPosition(),
                points.G
        );
    }

    public void loop() {
        updateButtons(gp1.a, gp1.b, gp1.x, gp1.y);

        if (aPressed || bPressed || xPressed || yPressed) mustReset = true;

        if (mustReset && !isPathfinderActive) {
            align(gp1.a, gp1.b, gp1.x, gp1.y);
        }

        if (gp1.start) {
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
            if (gp1.left_trigger != 0 && gp1.right_trigger == 0) {
                multiplier = 0.25;
            } else {
                multiplier = 0.5;
            }

            if (gp1.left_trigger == 0 && gp1.right_trigger != 0) {
                multiplier = 1;
            } else {
                multiplier = 0.5;
            }

            double fr = -gp1.left_stick_y - gp1.right_stick_x - gp1.left_stick_x;
            double br = -gp1.left_stick_y - gp1.right_stick_x + gp1.left_stick_x;
            double fl = gp1.left_stick_y - gp1.right_stick_x + gp1.left_stick_x;
            double bl = gp1.left_stick_y - gp1.right_stick_x - gp1.left_stick_x;

            this.fr.setPower(fr * multiplier);
            this.br.setPower(br * multiplier);
            this.fl.setPower(fl * multiplier);
            this.bl.setPower(bl * multiplier);
        }

        if (gp2.a) t.onPress();
        else t.onRelease();

        if (gp2.x) wobbleToggle.onPress();
        else wobbleToggle.onRelease();

        if (gp2.dpad_up) wobbleArm.setPower(P_WOBBLE_ARM_U);
        else if (gp2.dpad_down) wobbleArm.setPower(P_WOBBLE_ARM_D);
        else wobbleArm.setPower(P_WOBBLE_ARM_N);

        if (gp2.right_bumper && t.state) {
            pusher.setPosition(P_PUSHER_ACTIVE);
        } else {
            pusher.setPosition(P_PUSHER_INACTIVE);
        }

        if (gp2.left_trigger > gp2.right_trigger) {
            intakeMover.setPosition(P_INTAKE_OUT);
            intake.setPower(P_INTAKE_OUT);
            lowerIntake.setPower(P_LOWER_OUT);
            upperIntake.setPower(P_UPPER_OUT);
        } else if (gp2.right_trigger > gp2.left_trigger) {
            intakeMover.setPosition(P_INTAKE_IN);
            intake.setPower(P_INTAKE_IN);
            lowerIntake.setPower(P_LOWER_IN);
            upperIntake.setPower(P_UPPER_IN);
        } else {
            intakeMover.setPosition(P_INTAKE);
            intake.setPower(P_INTAKE);
            lowerIntake.setPower(P_LOWER);
            upperIntake.setPower(P_UPPER);
        }

        if (wobbleToggle.state) wobbleDropper.setPosition(P_WOBBLE_DROPPER_ON);
        else wobbleDropper.setPosition(P_WOBBLE_DROPPER_OFF);

        if (t.state) {
            flywheel1.setVelocity(P_FLY_1_VEL_ON);
            flywheel2.setVelocity(P_FLY_2_VEL_ON);
        } else {
            flywheel1.setVelocity(P_FLY_1_VEL_OFF);
            flywheel2.setVelocity(P_FLY_2_VEL_OFF);
        }

        telemetry.addData("pos", pathfinder.getPosition());
        telemetry.update();
    }

    public static class AutoAlignPoints {
        /**
         * Left power shot.
         */
        private final HeadingPoint L;

        /**
         * Middle power shot.
         */
        private final HeadingPoint M;

        /**
         * Right power shot.
         */
        private final HeadingPoint R;

        /**
         * High goal.
         */
        private final HeadingPoint G;

        /**
         * @param l LEFT POWER SHOT
         * @param m MIDDLE POWER SHOT
         * @param r RIGHT POWER SHOT
         * @param g HIGH GOAL
         */
        public AutoAlignPoints(HeadingPoint l,
                               HeadingPoint m,
                               HeadingPoint r,
                               HeadingPoint g) {
            L = l;
            M = m;
            R = r;
            G = g;
        }

        public HeadingPoint getL() {
            return L;
        }

        public HeadingPoint getM() {
            return M;
        }

        public HeadingPoint getR() {
            return R;
        }

        public HeadingPoint getG() {
            return G;
        }
    }
}
