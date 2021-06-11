/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: GenericBlue
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.teamcode.ultimategoal.util.TargetZone;

public class GenericRedWobbleFirst extends LinearOpMode {

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
                new HeadingPoint(130, 120, 90),
                new HeadingPoint(124, 100, 180),
                new HeadingPoint(128, 72, 90),
        }, hardwareMap, telemetry, offset, this::opModeIsActive);
        robot.runVision(this::isStarted);
        waitForStart();
        if (withDelay) {
            switch (robot.getTargetZone()) {
                case A:
                    sleep(6000);
                case B:
                    sleep(5000);
                case C:
                    sleep(4000);
            }
        }

        robot.goToPoint(robot.getWobblePoint(), this::opModeIsActive, 10);
        sleep(500);
        robot.dropGoal(this::sleep);
        sleep(500);
        robot.bringArmIn();
        robot.goToPoint(new HeadingPoint(122, 32, 165), this::opModeIsActive, 10);
        if (robot.getTargetZone() == TargetZone.A && !withDelay) {
            System.out.println("SLEEP");
            sleep(2000);
        }
        sleep(500);
        robot.goToPoint(new HeadingPoint(122, 64, 206), this::opModeIsActive, 10);
        robot.setShooterPower(0.9);
        sleep(200);
        robot.shoot(this::sleep);
        robot.goToPoint(new HeadingPoint(122, 65, -157), this::opModeIsActive, 3);
        sleep(200);
        robot.goToPoint(new HeadingPoint(122, 66, -150), this::opModeIsActive, 3);
        sleep(200);
        robot.shoot(this::sleep);
        robot.goToPoint(new HeadingPoint(122, 65, -153), this::opModeIsActive, 3);
        sleep(200);
        robot.goToPoint(new HeadingPoint(122, 66, -145), this::opModeIsActive, 3);
        sleep(500);
        robot.shoot(this::sleep);
        robot.setShooterPower(0.0);
        sleep(500);
        robot.goToPoint(new HeadingPoint(122, 74, 180), this::opModeIsActive, 3);
        sleep(1000);
        robot.stopRecording();
    }
}
