/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: AutoRedLeft
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states.main;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericBlueShotsFirst;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericBlueWobbleFirst;

@Autonomous(name = "AutonBlueOuterShotsFirst- States", group = "Main Autonomous")
public class AutoBlueOuterShotsFirst extends GenericBlueShotsFirst {

    @Override
    public void runOpMode() throws InterruptedException {
        System.out.println("RUNNING");
        setOffset(new Point(24, 9));
        super.runOpMode();
    }
}
