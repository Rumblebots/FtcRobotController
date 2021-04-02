package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import com.qualcomm.robotcore.hardware.DcMotor;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.config.PathfinderConfigurationBuilder;
import me.wobblyyyy.pathfinder.followers.Followers;
import me.wobblyyyy.pathfinder.maps.ftc.EmptyFTC;

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
                fr, fl, br,
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

        PathfinderConfig config = PathfinderConfigurationBuilder
                .newConfiguration()
                .drive(drive)
                .odometry(odometry)
                .speed(speed)
                .followerType(Followers.LINEAR)
                .map(new EmptyFTC())
                .build();

        return new Pathfinder(config);
    }
}
