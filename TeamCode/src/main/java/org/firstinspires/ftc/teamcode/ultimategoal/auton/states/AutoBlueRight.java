/****
 * Made by Tejas Mehta
 * Made on Tuesday, May 18, 2021
 * File Name: AutoBlueRight
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "AutonBlueRight - States", group = "Main Autonomous")

public class AutoBlueRight extends GenericBlue {
    @Override
    public void runOpMode() throws InterruptedException {
        System.out.println("RUNNING");
        setXOffset(35);
        super.runOpMode();
    }
}
