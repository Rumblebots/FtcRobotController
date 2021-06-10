package org.firstinspires.ftc.teamcode.ultimategoal.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Red Meccanum", group = "Test")
public class RedMeccanum extends OpMode {
    private GenericTeleOp op;

    public RedMeccanum() {
        op = new GenericTeleOp(
                hardwareMap,
                AutoAlignmentPoints.RED,
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
