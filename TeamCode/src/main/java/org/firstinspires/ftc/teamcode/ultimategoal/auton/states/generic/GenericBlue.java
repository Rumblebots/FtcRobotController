/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: GenericBlue
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

public class GenericBlue extends LinearOpMode {

    private double xOffset;

//    GenericBlue(double xOffset) {
//     this.xOffset = xOffset;
//    }

//    GenericBlue() {
//        xOffset = 15;
//    }

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
        robot.goToPoint(robot.getWobblePoint(), this::opModeIsActive, 15);
        robot.dropGoal();
        robot.goToPoint(new HeadingPoint(20, 57, 165), this::opModeIsActive, 10);
        robot.setShooterPower(1.0);
        sleep(500);
        robot.shoot(this::sleep);
        robot.goToPoint(new HeadingPoint(20, 57, 159), this::opModeIsActive, 3);
        sleep(200);
        robot.shoot(this::sleep);
        robot.goToPoint(new HeadingPoint(20, 57, 154), this::opModeIsActive, 3);
        sleep(200);
        robot.shoot(this::sleep);
        robot.setShooterPower(0.0);
        sleep(200);
        robot.goToPoint(new HeadingPoint(20, 65, 154), this::opModeIsActive, 3);
    }
}
