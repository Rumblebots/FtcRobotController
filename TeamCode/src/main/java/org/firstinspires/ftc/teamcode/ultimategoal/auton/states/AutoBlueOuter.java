/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: AutoRedLeft
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericBlue;

@Autonomous(name = "AutonBlueOuter- States", group = "Main Autonomous")
public class AutoBlueOuter extends GenericBlue {

    @Override
    public void runOpMode() throws InterruptedException {
        System.out.println("RUNNING");
        setOffset(new Point(24, 9));
        super.runOpMode();
    }
}
