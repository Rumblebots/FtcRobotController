package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import me.wobblyyyy.pathfinder.tracking.threeWheel.ThreeWheelChassisTracker;

/**
 * @see ThreeWheelChassisTracker
 */
public class PfOdometry extends ThreeWheelChassisTracker {
    private static final double WHEEL_DIAMETER = 0.5;
    private static final double LEFT_OFFSET = 0;
    private static final double RIGHT_OFFSET = 0;
    private static final double BACK_OFFSET = 0;

    public PfOdometry(PfEncoders encoders) {
        super(
                encoders.left,
                encoders.right,
                encoders.back,
                WHEEL_DIAMETER,
                LEFT_OFFSET,
                RIGHT_OFFSET,
                BACK_OFFSET
        );
    }
}
