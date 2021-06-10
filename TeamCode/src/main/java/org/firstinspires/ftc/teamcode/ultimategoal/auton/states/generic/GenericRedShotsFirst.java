/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: GenericBlue
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;

public class GenericRedShotsFirst extends LinearOpMode {

    private Point offset;
    private boolean withDelay = false;

//    GenericBlue(double xOffset) {
//     this.xOffset = xOffset;
//    }

//    GenericRed() {
//        xOffset = 15;
//    }

    public void setOffset(Point offset) {
        this.offset = offset;
    }

    public void withDelay(boolean delay) {withDelay = delay;}

    @Override
    public void runOpMode() throws InterruptedException {
        AutoRobot robot = new AutoRobot(new HeadingPoint[] {
                new HeadingPoint(132, 120, 90),
                new HeadingPoint(128, 100, 180),
                new HeadingPoint(128, 72, 90),
        }, hardwareMap, telemetry, offset, this::opModeIsActive);
        robot.runVision(this::isStarted);
        waitForStart();
        if (withDelay) {
            switch (robot.getTargetZone()) {
                case A:
                    sleep(7000);
                case B:
                    sleep(6000);
                case C:
                    sleep(5000);
            }
        }
        robot.goToPoint(new HeadingPoint(96, 64, 180), this::opModeIsActive, 10);
        robot.setShooterPower(0.9);
        sleep(200);
        robot.shoot(this::sleep);
        robot.goToPoint(new HeadingPoint(96, 60, 180), this::opModeIsActive, 3);
        sleep(200);
        robot.goToPoint(new HeadingPoint(96, 65, -175), this::opModeIsActive, 3);
        sleep(200);
        robot.shoot(this::sleep);
        robot.goToPoint(new HeadingPoint(96, 60, 180), this::opModeIsActive, 3);
        sleep(200);
        robot.goToPoint(new HeadingPoint(96, 65, -170), this::opModeIsActive, 3);
        sleep(500);
        robot.shoot(this::sleep);
        robot.setShooterPower(0.0);
        sleep(500);
        robot.goToPoint(robot.getWobblePoint(), this::opModeIsActive, 10);
        sleep(500);
        robot.dropGoal(this::sleep);
        sleep(500);
        robot.bringArmIn();
        robot.goToPoint(new HeadingPoint(robot.getWobblePoint().getX(), robot.getWobblePoint().getY()-10, robot.getWobblePoint().getHeading()), this::opModeIsActive, 3);
        sleep(200);
        robot.goToPoint(new HeadingPoint(110, 74, 180), this::opModeIsActive, 3);
        sleep(1000);
        robot.stopRecording();
    }
}
