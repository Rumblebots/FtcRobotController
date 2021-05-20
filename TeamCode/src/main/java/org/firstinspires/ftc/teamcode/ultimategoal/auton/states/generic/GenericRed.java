/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: GenericBlue
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

public class GenericRed extends LinearOpMode {

    private double xOffset;

//    GenericBlue(double xOffset) {
//     this.xOffset = xOffset;
//    }

//    GenericRed() {
//        xOffset = 15;
//    }

    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        AutoRobot robot = new AutoRobot(new HeadingPoint[]{
                new HeadingPoint(124, 113, 0),
                new HeadingPoint(99, 93, 0),
                new HeadingPoint(124, 73, 0),
        }, hardwareMap, telemetry, xOffset, this::opModeIsActive);
        robot.runVision(this::isStarted);
        waitForStart();
        robot.goToPoint(robot.getWobblePoint(), this::opModeIsActive, 15);
        robot.goToPoint(new HeadingPoint(124, 57, 195), this::opModeIsActive, 10);
        robot.setShooterPower(1.0);
        sleep(500);
        robot.shoot(this::sleep);
        robot.goToPoint(new HeadingPoint(124, 57, 201), this::opModeIsActive, 3);
        sleep(200);
        robot.shoot(this::sleep);
        robot.goToPoint(new HeadingPoint(124, 57, 206), this::opModeIsActive, 3);
        sleep(200);
        robot.shoot(this::sleep);
        robot.setShooterPower(0.0);
        sleep(200);
        robot.goToPoint(new HeadingPoint(124, 65, 206), this::opModeIsActive, 3);
    }
}
