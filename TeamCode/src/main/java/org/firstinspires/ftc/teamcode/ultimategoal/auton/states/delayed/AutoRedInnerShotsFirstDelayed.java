/****
 * Made by Tejas Mehta
 * Made on Tuesday, May 18, 2021
 * File Name: AutoBlueRight
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states.delayed;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericRedShotsFirst;

@Autonomous(name = "AutonRedInnerShotsFirstDelayed - States", group = "Delayed Autonomous")

public class AutoRedInnerShotsFirstDelayed extends GenericRedShotsFirst {
    @Override
    public void runOpMode() throws InterruptedException {
        System.out.println("RUNNING");
        setOffset(new Point(96, 9));
        withDelay(true);
        super.runOpMode();
    }
}
