package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import com.qualcomm.robotcore.hardware.DcMotor;
import me.wobblyyyy.pathfinder.robot.Encoder;

public class PfEncoder implements Encoder {
    private final DcMotor motor;

    public PfEncoder(DcMotor motor) {
        this.motor = motor;
    }

    @Override
    public int getCount() {
        return motor.getCurrentPosition();
    }

    @Override
    public double getCpr() {
        return 1440;
    }
}
