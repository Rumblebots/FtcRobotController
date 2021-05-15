package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import com.qualcomm.robotcore.hardware.DcMotor;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.config.PathfinderConfigurationBuilder;
import me.wobblyyyy.pathfinder.control.ProportionalController;
import me.wobblyyyy.pathfinder.followers.Followers;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.maps.ftc.EmptyFTC;
import me.wobblyyyy.pathfinder.tracking.threeWheel.ThreeWheelChassisTracker;

import java.util.function.Supplier;

/**
 * FIXME this whole class sucks
 */
public class PathfinderCreator {

    private static PfDrivetrain driveTrain;
    private static ThreeWheelChassisTracker odometry;

    public static PfDrivetrain getDriveTrain() {
        return driveTrain;
    }

    public static ThreeWheelChassisTracker getChassisTracker() {
        return odometry;
    }

    public static Pathfinder create(DcMotor fr,
                                    DcMotor fl,
                                    DcMotor br,
                                    DcMotor bl,
                                    DcMotor encoderL,
                                    DcMotor encoderR,
                                    DcMotor encoderB,
                                    boolean invertFr,
                                    boolean invertFl,
                                    boolean invertBr,
                                    boolean invertBl,
                                    boolean invertEncoderL,
                                    boolean invertEncoderR,
                                    boolean invertEncoderB,
                                    double speed,
                                    double wheelDiameter,
                                    double offsetLeft,
                                    double offsetRight,
                                    double offsetBack,
                                    Supplier<Boolean> shouldRun) {
        PfMotors motors = new PfMotors(
                fr, fl, br, bl,
                invertFr,
                invertFl,
                invertBr,
                invertBl
        );
        PfEncoders encoders = new PfEncoders(
                encoderL, encoderR, encoderB,
                invertEncoderL,
                invertEncoderR,
                invertEncoderB
        );
        driveTrain = new PfDrivetrain(motors);
        odometry = new PfOdometry(
                encoders,
                wheelDiameter,
                offsetLeft,
                offsetRight,
                offsetBack
        );

//        odometry.useCurrentPosAsOffset();
//        odometry.setOffset(new Point(42, 0));

        PathfinderConfig config = PathfinderConfigurationBuilder
                .newConfiguration()
                .drive(driveTrain)
                .odometry(odometry)
                .speed(speed)
                .followerType(Followers.LINEAR)
                .map(new EmptyFTC())
                .drivetrainSwapXY(true)
                .drivetrainInvertX(true)
                .tickerThreadOnStop(shouldRun)
                .turnController(new ProportionalController(-1/180.0))
                .build();

        return new Pathfinder(config);
    }
}
