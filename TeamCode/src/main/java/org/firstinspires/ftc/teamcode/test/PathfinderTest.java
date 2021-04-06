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
    private static final String NAME_FL_MOTOR = "frontLeft";
    private static final String NAME_BR_MOTOR = "backRight";
    private static final String NAME_BL_MOTOR = "backLeft";

    private static final boolean INVERT_FR_MOTOR = false;
    private static final boolean INVERT_FL_MOTOR = true;
    private static final boolean INVERT_BR_MOTOR = false;
    private static final boolean INVERT_BL_MOTOR = true;

    private static final boolean INVERT_ENCODER_L = false;
    private static final boolean INVERT_ENCODER_R = true;
    private static final boolean INVERT_ENCODER_B = true;

    private static final double SPEED = 0.45;
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
                    new HeadingPoint(0, 0, 0),
                    new HeadingPoint(20, 0, 0),
                    new HeadingPoint(20, 20, 0),
                    new HeadingPoint(0, 20, 0),
                    new HeadingPoint(0, 0, 0)
            );

    private void initializeMotors() {
        frMotor = hardwareMap.dcMotor.get(NAME_FR_MOTOR);
        flMotor = hardwareMap.dcMotor.get(NAME_FL_MOTOR);
        brMotor = hardwareMap.dcMotor.get(NAME_BR_MOTOR);
        blMotor = hardwareMap.dcMotor.get(NAME_BL_MOTOR);
    }

    private void initializePathfinder() {
        pathfinder = PathfinderCreator.create(
                frMotor,
                flMotor,
                brMotor,
                blMotor,
                brMotor,
                blMotor,
                frMotor,
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
                OFFSET_BACK
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