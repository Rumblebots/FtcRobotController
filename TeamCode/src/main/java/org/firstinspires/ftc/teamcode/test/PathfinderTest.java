package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ultimategoal.pathfinder.PathfinderCreator;

@TeleOp(name = "Pathfinder Test", group = "default")
public class PathfinderTest extends LinearOpMode {
    private Pathfinder pathfinder;

    private static final String NAME_FR_MOTOR = "frontRight";
//        private static final String NAME_FR_MOTOR = "backRight";
    private static final String NAME_FL_MOTOR = "backLeft";
    private static final String NAME_BR_MOTOR = "backRight";
    private static final String NAME_BL_MOTOR = "frontLeft";
//    private static final String NAME_BR_MOTOR = "frontRight";
    private static final boolean INVERT_FR_MOTOR = true;
    private static final boolean INVERT_FL_MOTOR = false;
    private static final boolean INVERT_BR_MOTOR = true;
    private static final boolean INVERT_BL_MOTOR = false;

    private static final boolean INVERT_ENCODER_L = true;
    private static final boolean INVERT_ENCODER_R = false;
    private static final boolean INVERT_ENCODER_B = false;

    private static final double SPEED = 0.3;
    private static final double WHEEL_DIAMETER = 1.5;
    private static final double OFFSET_LEFT = 7.83;
    private static final double OFFSET_RIGHT = 7.83;
    private static final double OFFSET_BACK = 1.0;

    private DcMotor frMotor;
    private DcMotor flMotor;
    private DcMotor brMotor;
    private DcMotor blMotor;

    private static final DynamicArray<HeadingPoint> RECTANGLE =
            new DynamicArray<>(
                    new HeadingPoint(0.1, 0.1, 0),
                    new HeadingPoint(0.1, 5, 45)
//                    new HeadingPoint(20.2, 0.2, 45),
//                    new HeadingPoint(20.3, 20.3, 90),
//                    new HeadingPoint(0.4, 20.4, 135),
//                    new HeadingPoint(0.5, 0.5, 180)
            );

    private void initializeMotors() {
        frMotor = hardwareMap.dcMotor.get(NAME_FR_MOTOR);
        flMotor = hardwareMap.dcMotor.get(NAME_FL_MOTOR);
        brMotor = hardwareMap.dcMotor.get(NAME_BR_MOTOR);
        blMotor = hardwareMap.dcMotor.get(NAME_BL_MOTOR);
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

    private void initializePathfinder() {
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
                this::opModeIsActive
        );
    }

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        initializeMotors();
        initializePathfinder();

        Thread.sleep(500);

        pathfinder.followPath(RECTANGLE);

        while (opModeIsActive()) {
            pathfinder.tick();
            telemetry.addData("pos", pathfinder.getPosition().toString());
            telemetry.update();
        }
    }
}
