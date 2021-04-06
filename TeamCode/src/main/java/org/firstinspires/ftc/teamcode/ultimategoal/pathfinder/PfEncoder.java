package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import com.qualcomm.robotcore.hardware.DcMotor;
import me.wobblyyyy.pathfinder.math.Invertable;
import me.wobblyyyy.pathfinder.robot.Encoder;

/**
 * A singular encoder based on a {@code DcMotor}. Can be inverted, etc -
 * doesn't do much more.
 */
public class PfEncoder implements Encoder {
    private final DcMotor motor;
    private final boolean isInverted;

    public PfEncoder(DcMotor motor,
                     boolean isInverted) {
        this.motor = motor;
        this.isInverted = isInverted;
    }

    @Override
    public int getCount() {
        return Invertable.apply(motor.getCurrentPosition(), isInverted);
    }

    @Override
    public double getCpr() {
        return 1440;
    }
}
