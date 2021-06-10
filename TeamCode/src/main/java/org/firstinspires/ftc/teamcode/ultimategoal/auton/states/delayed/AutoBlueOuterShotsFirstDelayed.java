/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: AutoRedLeft
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states.delayed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericBlueShotsFirst;

@Autonomous(name = "AutonBlueOuterShotsFirstDelayed- States", group = "Delayed Autonomous")
public class AutoBlueOuterShotsFirstDelayed extends GenericBlueShotsFirst {

    @Override
    public void runOpMode() throws InterruptedException {
        System.out.println("RUNNING");
        setOffset(new Point(24, 9));
        withDelay(true);
        super.runOpMode();
    }
}
