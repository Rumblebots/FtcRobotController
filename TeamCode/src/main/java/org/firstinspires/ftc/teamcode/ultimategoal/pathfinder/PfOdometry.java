package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import me.wobblyyyy.pathfinder.tracking.threeWheel.ThreeWheelChassisTracker;

/**
 * @see ThreeWheelChassisTracker
 */
public class PfOdometry extends ThreeWheelChassisTracker {
    public PfOdometry(PfEncoders encoders,
                      double wheelDiameter,
                      double offsetLeft,
                      double offsetRight,
                      double offsetBack) {
        super(
                encoders.left,
                encoders.right,
                encoders.back,
                wheelDiameter,
                offsetLeft,
                offsetRight,
                offsetBack
        );
    }
}
