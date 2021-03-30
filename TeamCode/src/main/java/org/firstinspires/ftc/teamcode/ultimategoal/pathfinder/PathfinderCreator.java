package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import com.qualcomm.robotcore.hardware.DcMotor;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.config.PathfinderConfigurationBuilder;
import me.wobblyyyy.pathfinder.followers.Followers;
import me.wobblyyyy.pathfinder.maps.ftc.EmptyFTC;

public class PathfinderCreator {
    private static final double SPEED = 0.225;

    public static Pathfinder create(DcMotor fr,
                                    DcMotor fl,
                                    DcMotor br,
                                    DcMotor bl) {
        PfMotors motors = new PfMotors(fr, fl, br, bl);
        PfEncoders encoders = new PfEncoders(fr, fl, br);
        PfDrivetrain drive = new PfDrivetrain(motors);
        PfOdometry odometry = new PfOdometry(encoders);

        PathfinderConfig config = PathfinderConfigurationBuilder
                .newConfiguration()
                .drive(drive)
                .odometry(odometry)
                .speed(SPEED)
                .followerType(Followers.LINEAR)
                .map(new EmptyFTC())
                .build();

        return new Pathfinder(config);
    }
}
