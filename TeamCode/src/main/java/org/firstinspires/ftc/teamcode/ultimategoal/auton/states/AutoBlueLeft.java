/****
 * Made by Tejas Mehta
 * Made on Saturday, May 15, 2021
 * File Name: AutoRedLeft
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericBlue;

@Autonomous(name = "AutonBlueLeft - States", group = "Main Autonomous")
public class AutoBlueLeft extends GenericBlue {

    @Override
    public void runOpMode() throws InterruptedException {
        System.out.println("RUNNING");
        setXOffset(12);
        super.runOpMode();
    }
}
