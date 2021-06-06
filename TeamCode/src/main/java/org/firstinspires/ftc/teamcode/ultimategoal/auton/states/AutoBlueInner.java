/****
 * Made by Tejas Mehta
 * Made on Tuesday, May 18, 2021
 * File Name: AutoBlueRight
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericBlue;

@Autonomous(name = "AutonBlueInner - States", group = "Main Autonomous")

public class AutoBlueInner extends GenericBlue {
    @Override
    public void runOpMode() throws InterruptedException {
        System.out.println("RUNNING");
        setOffset(new Point(24, 9));
        super.runOpMode();
    }
}
