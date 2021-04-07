package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import com.qualcomm.robotcore.hardware.DcMotor;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.config.PathfinderConfigurationBuilder;
import me.wobblyyyy.pathfinder.followers.Followers;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.maps.ftc.EmptyFTC;

/**
 * FIXME this whole class sucks
 */
public class PathfinderCreator {
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
                                    double offsetBack) {
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
        PfDrivetrain drive = new PfDrivetrain(motors);
        PfOdometry odometry = new PfOdometry(
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
                .drive(drive)
                .odometry(odometry)
                .speed(speed)
                .followerType(Followers.LINEAR)
                .map(new EmptyFTC())
                .drivetrainSwapXY(true)
                .drivetrainInvertX(true)
                .build();

        return new Pathfinder(config);
    }
}
