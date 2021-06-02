/****
 * Made by Tejas Mehta
 * Made on Wednesday, April 07, 2021
 * File Name: PathfinderConstants
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.pathfinder*/
package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.tracking.threeWheel.ThreeWheelChassisTracker;

import java.util.function.Supplier;

public class PathfinderConstants {
    private static Pathfinder pathfinder;

    private static final String NAME_FR_MOTOR = "frontRight";
    private static final String NAME_FL_MOTOR = "backLeft";
    private static final String NAME_BR_MOTOR = "backRight";
    private static final String NAME_BL_MOTOR = "frontLeft";

    private static final boolean INVERT_FR_MOTOR = true;
    private static final boolean INVERT_FL_MOTOR = false;
    private static final boolean INVERT_BR_MOTOR = true;
    private static final boolean INVERT_BL_MOTOR = false;

    private static final boolean INVERT_ENCODER_L = true;
    private static final boolean INVERT_ENCODER_R = false;
    private static final boolean INVERT_ENCODER_B = false;

    private static final double SPEED = 0.7;
    private static final double WHEEL_DIAMETER = 1.5;
    private static final double OFFSET_LEFT = 7.83;
    private static final double OFFSET_RIGHT = 7.83;
    private static final double OFFSET_BACK = 1.75;

    private static DcMotor frMotor;
    private static DcMotor flMotor;
    private static DcMotor brMotor;
    private static DcMotor blMotor;

    public static void initializeMotors(HardwareMap hardwareMap) {
        frMotor = hardwareMap.dcMotor.get(NAME_FR_MOTOR);
        flMotor = hardwareMap.dcMotor.get(NAME_FL_MOTOR);
        brMotor = hardwareMap.dcMotor.get(NAME_BR_MOTOR);
        blMotor = hardwareMap.dcMotor.get(NAME_BL_MOTOR);
        resetMotors();
    }

    public static void resetMotors() {
        frMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        brMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        blMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        brMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        blMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        brMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        blMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public static void initializePathfinder(Supplier<Boolean> shouldRun) {
        pathfinder = PathfinderCreator.create(
                frMotor,
                flMotor,
                brMotor,
                blMotor,
                flMotor, // Left
                brMotor, // Right
                frMotor, // Back
                INVERT_FR_MOTOR,
                INVERT_FL_MOTOR,
                INVERT_BR_MOTOR,
                INVERT_BL_MOTOR,
                INVERT_ENCODER_L,
                INVERT_ENCODER_R,
                INVERT_ENCODER_B,
                SPEED,
                WHEEL_DIAMETER,
                OFFSET_LEFT,
                OFFSET_RIGHT,
                OFFSET_BACK,
                shouldRun
        );
        getChassisTracker().setOffset(new Point(9, 9));
    }

    public static PfDrivetrain getDriveTrain() {
        return PathfinderCreator.getDriveTrain();
    }

    public static ThreeWheelChassisTracker getChassisTracker() {
        return PathfinderCreator.getChassisTracker();
    }

    public static Pathfinder getPathfinder() {
        if (pathfinder == null)
            throw new IllegalArgumentException(
                    "Please initialize Pathfinder first");
        return pathfinder;
    }
}
