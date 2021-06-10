/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: AutoRedLeft
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states.main;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericRedShotsFirst;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericRedWobbleFirst;

@Autonomous(name = "AutonRedOuterWobbleFirst - States", group = "Main Autonomous")
public class AutoRedOuterWobbleFirst extends GenericRedWobbleFirst {

    @Override
    public void runOpMode() throws InterruptedException {
        System.out.println("RUNNING");
        setOffset(new Point(120, 9));
        super.runOpMode();
    }
}
