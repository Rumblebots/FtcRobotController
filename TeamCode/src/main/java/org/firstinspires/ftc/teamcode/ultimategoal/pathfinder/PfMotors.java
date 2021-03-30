package org.firstinspires.ftc.teamcode.ultimategoal.pathfinder;

import com.qualcomm.robotcore.hardware.DcMotor;
import me.wobblyyyy.pathfinder.robot.Motor;
import me.wobblyyyy.pathfinder.robot.MotorBuilder;

public class PfMotors {
    private static final boolean FR_INVERT = false;
    private static final boolean FL_INVERT = false;
    private static final boolean BR_INVERT = false;
    private static final boolean BL_INVERT = false;

    public final Motor frontRight;
    public final Motor frontLeft;
    public final Motor backRight;
    public final Motor backLeft;

    /**
     * TODO (so someone sees this later)
     * this method uses suppliers and consumers which don't work with any api
     * under 24, if we need to downgrade APIs this will throw a hissy fit
     * we can accomplish the same thing by implementing the {@link Motor}
     * interface in a separate class file, but this is certainly preferable
     */
    public PfMotors(DcMotor fr,
                    DcMotor fl,
                    DcMotor br,
                    DcMotor bl) {
        frontRight = MotorBuilder.buildFrom(
                fr::setPower, fr::getPower, FR_INVERT);
        frontLeft = MotorBuilder.buildFrom(
                fl::setPower, fl::getPower, FL_INVERT);
        backRight = MotorBuilder.buildFrom(
                br::setPower, br::getPower, BR_INVERT);
        backLeft = MotorBuilder.buildFrom(
                bl::setPower, bl::getPower, BL_INVERT);
    }
}
