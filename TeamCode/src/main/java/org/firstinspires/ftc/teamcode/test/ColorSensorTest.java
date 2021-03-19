/****
 * Made by Tejas Mehta
 * Made on Thursday, March 18, 2021
 * File Name: ColorSensorTest
 * Package: org.firstinspires.ftc.teamcode.test*/
package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp(name = "ColorSensorTest", group = "Tests")
public class ColorSensorTest extends OpMode {
    ColorSensor colorSensor;

    @Override
    public void init() {
        colorSensor = hardwareMap.get(ColorSensor.class, "bottomSensor");
    }

    @Override
    public void loop() {
        telemetry.addData("ColorSensor Value ARGB", colorSensor.argb());
        telemetry.addData("ColorSensor Value R", colorSensor.red());
        telemetry.addData("ColorSensor Value G", colorSensor.green());
        telemetry.addData("ColorSensor Value B", colorSensor.blue());
        telemetry.update();
    }
}
