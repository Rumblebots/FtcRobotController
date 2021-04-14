/****
 * Made by Tejas Mehta
 * Made on Tuesday, April 13, 2021
 * File Name: TouchSensorTest
 * Package: org.firstinspires.ftc.teamcode.test*/
package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name = "Touch Sensor Test", group = "Test")
public class TouchSensorTest extends OpMode {
    DigitalChannel armOut;
    DigitalChannel armIn;

    @Override
    public void init() {
        armOut = hardwareMap.get(DigitalChannel.class, "armOut");
        armIn = hardwareMap.get(DigitalChannel.class, "armIn");
    }

    @Override
    public void loop() {
        telemetry.addData("armOut", armOut.getState());
        telemetry.addData("armIn", armIn.getState());
        telemetry.update();
    }
}
