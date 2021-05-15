/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: GenericBlue
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

public class GenericBlue extends LinearOpMode {

    private double xOffset;

//    GenericBlue(double xOffset) {
//     this.xOffset = xOffset;
//    }

    GenericBlue() {
        xOffset = 15;
    }

    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        AutoRobot robot = new AutoRobot(new HeadingPoint[]{
                new HeadingPoint(20, 113, 0),
                new HeadingPoint(45, 93, 0),
                new HeadingPoint(20, 73, 0),
        }, hardwareMap, telemetry, xOffset, this::opModeIsActive);
        robot.runVision(this::isStarted);
        waitForStart();
        robot.goToPoint(robot.getWobblePoint());
        robot.goToPoint(new HeadingPoint(20, 60, -90));
//        robot.shoot(this::sleep);
//        robot.goToPoint(new HeadingPoint(20, 60, -215));
//        robot.goToPoint(new HeadingPoint(20, 60, -230));
    }
}
