/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: AutoRedLeft
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericBlue;

@Autonomous(name = "AutonRedLeft - States", group = "Main Autonomous")
public class AutoRedLeft extends GenericBlue {

    @Override
    public void runOpMode() throws InterruptedException {
        System.out.println("RUNNING");
        setXOffset(132);
        super.runOpMode();
    }
}
