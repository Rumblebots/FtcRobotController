/****
 * Made by Tejas Mehta
 * Made on Tuesday, May 18, 2021
 * File Name: AutoBlueRight
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.auton.states*/
package org.firstinspires.ftc.teamcode.ultimategoal.auton.states.main;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericBlueShotsFirst;
import org.firstinspires.ftc.teamcode.ultimategoal.auton.states.generic.GenericBlueWobbleFirst;

@Autonomous(name = "AutonBlueInnerShotsFirst - States", group = "Main Autonomous")

public class AutoBlueInnerShotsFirst extends GenericBlueShotsFirst {
    @Override
    public void runOpMode() throws InterruptedException {
        System.out.println("RUNNING");
        setOffset(new Point(48, 9));
        super.runOpMode();
    }
}
