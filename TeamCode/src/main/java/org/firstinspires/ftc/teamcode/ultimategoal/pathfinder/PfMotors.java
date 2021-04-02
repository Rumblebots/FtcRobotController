package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import com.qualcomm.robotcore.hardware.DcMotor;
import me.wobblyyyy.pathfinder.robot.Motor;
import me.wobblyyyy.pathfinder.robot.MotorBuilder;

public class PfMotors {
    public final Motor frontRight;
    public final Motor frontLeft;
    public final Motor backRight;
    public final Motor backLeft;

    public PfMotors(DcMotor fr,
                    DcMotor fl,
                    DcMotor br,
                    DcMotor bl,
                    boolean invertFr,
                    boolean invertFl,
                    boolean invertBr,
                    boolean invertBl) {
        frontRight = MotorBuilder.buildFrom(
                fr::setPower, fr::getPower, invertFr);
        frontLeft = MotorBuilder.buildFrom(
                fl::setPower, fl::getPower, invertFl);
        backRight = MotorBuilder.buildFrom(
                br::setPower, br::getPower, invertBr);
        backLeft = MotorBuilder.buildFrom(
                bl::setPower, bl::getPower, invertBl);
    }
}
