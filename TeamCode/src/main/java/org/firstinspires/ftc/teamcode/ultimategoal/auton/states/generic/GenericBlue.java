/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: GenericBlue
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;

public class GenericBlue extends LinearOpMode {

    private Point offset;

//    GenericBlue(double xOffset) {
//     this.xOffset = xOffset;
//    }

//    GenericBlue() {
//        xOffset = 15;
//    }

    public void setOffset(Point offset) {
        this.offset = offset;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        AutoRobot robot = new AutoRobot(new HeadingPoint[]{
                new HeadingPoint(24, 128, 180),
                new HeadingPoint(48, 104, 180),
                new HeadingPoint(24, 80, 180),
        }, hardwareMap, telemetry, offset, this::opModeIsActive);
        robot.runVision(this::isStarted);
        waitForStart();
        robot.goToPoint(robot.getWobblePoint(), this::opModeIsActive, 10);
        sleep(500);
        robot.dropGoal(this::sleep);
        sleep(500);
        robot.goToPoint(new HeadingPoint(24, 65, 158), this::opModeIsActive, 10);
        robot.setShooterPower(0.90);
        sleep(500);
        robot.shoot(this::sleep);
        robot.goToPoint(new HeadingPoint(24, 65, 153), this::opModeIsActive, 3);
        sleep(200);
        robot.shoot(this::sleep);
        robot.goToPoint(new HeadingPoint(24, 65, 148), this::opModeIsActive, 3);
        sleep(200);
        robot.shoot(this::sleep);
        robot.setShooterPower(0.0);
        sleep(200);
        robot.goToPoint(new HeadingPoint(24, 74, 146), this::opModeIsActive, 3);
    }
}
