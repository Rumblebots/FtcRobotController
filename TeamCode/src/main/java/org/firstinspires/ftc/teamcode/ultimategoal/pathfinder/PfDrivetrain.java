package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import me.wobblyyyy.pathfinder.drive.MeccanumDrive;

/**
 * Wheelbase shouldn't matter all that much - some holonomic drivetrains
 * require specifications for accurate turning, meccanum drive does not.
 *
 * @see MeccanumDrive
 */
public class PfDrivetrain extends MeccanumDrive {
    private static final double WHEELBASE_GAP_X = 10;
    private static final double WHEELBASE_GAP_Y = 10;

    public PfDrivetrain(PfMotors motors) {
        super(
                motors.frontLeft,
                motors.frontRight,
                motors.backLeft,
                motors.backRight,
                WHEELBASE_GAP_X,
                WHEELBASE_GAP_Y
        );
    }
}
