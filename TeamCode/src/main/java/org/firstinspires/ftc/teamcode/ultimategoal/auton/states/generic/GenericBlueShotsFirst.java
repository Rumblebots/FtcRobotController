/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: GenericBlue
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;

public class GenericBlueShotsFirst extends LinearOpMode {

    private Point offset;
    private boolean withDelay = false;

    public void setOffset(Point offset) {
        this.offset = offset;
    }

    public void withDelay(boolean delay) {withDelay = delay;}

    @Override
    public void runOpMode() throws InterruptedException {
        AutoRobot robot = new AutoRobot(new HeadingPoint[]{
                new HeadingPoint(26, 124, 180),
                new HeadingPoint(48, 100, 180),
                new HeadingPoint(24, 80, 180),
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
        robot.goToPoint(new HeadingPoint(54, 66, 175), this::opModeIsActive, 10);
        sleep(200);
        robot.setShooterPower(robot.getAdjPower());
        sleep(500);
        robot.shoot(this::sleep);
        robot.goToPoint(new HeadingPoint(54, 66, 170), this::opModeIsActive, 4);
        sleep(200);
        robot.shoot(this::sleep);
        robot.goToPoint(new HeadingPoint(54, 66, 165), this::opModeIsActive, 4);
        sleep(200);
        robot.shoot(this::sleep);
        robot.setShooterPower(0.0);
        robot.goToPoint(robot.getWobblePoint(), this::opModeIsActive, 10);
        sleep(500);
        robot.dropGoal(this::sleep);
        sleep(500);
        robot.bringArmIn();
        sleep(200);
        robot.goToPoint(new HeadingPoint(66, robot.getWobblePoint().getY(), 180), this::opModeIsActive, 4);
        sleep(200);
        robot.goToPoint(new HeadingPoint(66, 80, 180), this::opModeIsActive, 7);
        robot.stopRecording();
    }
}
