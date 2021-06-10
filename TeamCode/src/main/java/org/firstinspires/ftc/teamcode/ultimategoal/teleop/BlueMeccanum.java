package org.firstinspires.ftc.teamcode.ultimategoal.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Blue Meccanum", group = "Test")
public class BlueMeccanum extends OpMode {
    private GenericTeleOp op;

    public BlueMeccanum() {
        op = new GenericTeleOp(
                hardwareMap,
                AutoAlignmentPoints.BLUE,
                gamepad1,
                gamepad2,
                telemetry
        );
    }

    @Override
    public void init() {
        op.init();
    }

    @Override
    public void loop() {
        op.loop();
    }
}
