package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import com.qualcomm.robotcore.hardware.DcMotor;
import me.wobblyyyy.pathfinder.robot.Encoder;

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
        return isInverted ?
                motor.getCurrentPosition() * -1 :
                motor.getCurrentPosition();
    }

    @Override
    public double getCpr() {
        return 1440;
    }
}
