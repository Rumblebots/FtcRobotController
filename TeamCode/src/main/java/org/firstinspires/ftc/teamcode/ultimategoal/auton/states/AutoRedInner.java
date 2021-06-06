/****
 * Made by Tejas Mehta
 * Made on Tuesday, May 18, 2021
 * File Name: AutoBlueRight
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericRed;

@Autonomous(name = "AutonRedInner - States", group = "Main Autonomous")

public class AutoRedInner extends GenericRed {
    @Override
    public void runOpMode() throws InterruptedException {
        System.out.println("RUNNING");
        setOffset(new Point(96, 9));
        super.runOpMode();
    }
}
