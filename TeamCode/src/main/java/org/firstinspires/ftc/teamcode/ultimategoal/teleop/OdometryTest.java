/****
 * Made by Tejas Mehta
 * Made on Thursday, April 15, 2021
 * File Name: OdometryTest
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.teleop*/
package org.firstinspires.ftc.teamcode.ultimategoal.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.tejasmehta.OdometryCore.localization.OdometryPosition;
import org.firstinspires.ftc.teamcode.ultimategoal.util.OdometryThread;

@TeleOp(name = "OdometryTest", group = "Test")
public class OdometryTest extends OpMode {

    DcMotor frontRight;
    DcMotor backRight;
    DcMotor backLeft;

    @Override
    public void init() {
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        flywheel1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        flywheel2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        OdometryThread.initialize(9, backLeft, backRight, frontRight, () -> true);
    }

    @Override
    public void loop() {
        telemetry.addData("Left ENC", backLeft.getCurrentPosition());
        telemetry.addData("Right ENC", backRight.getCurrentPosition());
        telemetry.addData("Back ENC", frontRight.getCurrentPosition());
        telemetry.addData("Odometry Pos: ", "x: " + getCurrentPos().getX() + ", y: " + getCurrentPos().getY() + ", heading: " + getCurrentPos().getHeadingDegrees());
        telemetry.update();
    }

    public OdometryPosition getCurrentPos() {
        OdometryPosition currentPosition = OdometryThread.getInstance().getCurrentPosition();
        return OdometryThread.getInstance().getCurrentPosition();
    }
}
