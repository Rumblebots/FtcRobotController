/****
 * Made by Tejas Mehta
 * Made on Wednesday, April 07, 2021
 * File Name: AutoPathfinder
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.teamcode.ultimategoal.pathfinder.PathfinderConstants;

@Autonomous(name = "AutonPathfinder", group = "Autonomous")
public class AutoPathfinder extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        PathfinderConstants.initializeMotors(hardwareMap);
        Pathfinder pathfinder = PathfinderConstants.getPathfinder();
        waitForStart();
        sleep(1000);
        pathfinder.tick();
        pathfinder.followPath(
                new DynamicArray<>(
                        new HeadingPoint(0, 0, 0),
                        new HeadingPoint(0, 80, 0)
                )
        );
        pathfinder.tickUntil();
    }
}
