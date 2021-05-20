/****
 * Made by Tejas Mehta
 * Made on Tuesday, May 18, 2021
 * File Name: AutoBlueRight
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericBlue;

@Autonomous(name = "AutonRedRight - States", group = "Main Autonomous")

public class AutoRedRight extends GenericBlue {
    @Override
    public void runOpMode() throws InterruptedException {
        System.out.println("RUNNING");
        setXOffset(109);
        super.runOpMode();
    }
}
