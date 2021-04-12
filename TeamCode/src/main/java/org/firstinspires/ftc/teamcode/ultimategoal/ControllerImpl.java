package org.firstinspires.ftc.teamcode.ultimategoal;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.rx.core.input.controller.AbstractXbc;

public class ControllerImpl extends AbstractXbc {
    private final Gamepad gamepad;

    public ControllerImpl(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    @Override
    public double rightX() {
        return gamepad.right_stick_x;
    }

    @Override
    public double rightY() {
        return gamepad.right_stick_y;
    }

    @Override
    public double leftX() {
        return gamepad.left_stick_x;
    }

    @Override
    public double leftY() {
        return gamepad.left_stick_y;
    }

    @Override
    public boolean a() {
        return gamepad.a;
    }

    @Override
    public boolean b() {
        return gamepad.b;
    }

    @Override
    public boolean x() {
        return gamepad.x;
    }

    @Override
    public boolean y() {
        return gamepad.y;
    }

    @Override
    public boolean dpadUp() {
        return gamepad.dpad_up;
    }

    @Override
    public boolean dpadRight() {
        return gamepad.dpad_right;
    }

    @Override
    public boolean dpadLeft() {
        return gamepad.dpad_left;
    }

    @Override
    public boolean dpadDown() {
        return gamepad.dpad_down;
    }

    @Override
    public boolean bumperRight() {
        return gamepad.right_bumper;
    }

    @Override
    public boolean bumperLeft() {
        return gamepad.left_bumper;
    }

    @Override
    public double triggerRight() {
        return gamepad.right_trigger;
    }

    @Override
    public double triggerLeft() {
        return gamepad.left_trigger;
    }

    @Override
    public boolean stickRight() {
        return gamepad.right_stick_button;
    }

    @Override
    public boolean stickLeft() {
        return gamepad.left_stick_button;
    }

    @Override
    public boolean start() {
        return gamepad.start;
    }

    @Override
    public boolean select() {
        return gamepad.back;
    }
}
