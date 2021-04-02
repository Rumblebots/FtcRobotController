package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import com.qualcomm.robotcore.hardware.DcMotor;
import me.wobblyyyy.pathfinder.robot.Encoder;

public class PfEncoders {
    public final Encoder left;
    public final Encoder right;
    public final Encoder back;

    public PfEncoders(DcMotor leftMotor,
                      DcMotor rightMotor,
                      DcMotor backMotor,
                      boolean invertEncoderL,
                      boolean invertEncoderR,
                      boolean invertEncoderB) {
        left = new PfEncoder(leftMotor, invertEncoderL);
        right = new PfEncoder(rightMotor, invertEncoderR);
        back = new PfEncoder(backMotor, invertEncoderB);
    }
}
