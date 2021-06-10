package org.firstinspires.ftc.teamcode.ultimategoal.teleop;

import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

public class AutoAlignmentPoints {
    private static final HeadingPoint BLUE_PS_L = new HeadingPoint(48 + 5.5 + 2, 144, 0);
    private static final HeadingPoint BLUE_PS_M = new HeadingPoint(48 + 5.5 + 7.5 + 7.5, 144, 0);
    private static final HeadingPoint BLUE_PS_R = new HeadingPoint(48 + 5.5 + 7.5 + 7.5 + 7.5, 144, 0);

    private static final HeadingPoint RED_PS_L = new HeadingPoint(48 + 5.5 + 2 + 24, 144, 0);
    private static final HeadingPoint RED_PS_M = new HeadingPoint(48 + 5.5 + 7.5 + 7.5 + 24, 144, 0);
    private static final HeadingPoint RED_PS_R = new HeadingPoint(48 + 5.5 + 7.5 + 7.5 + 7.5 + 24, 144, 0);

    private static final HeadingPoint RED_HI = new HeadingPoint(120, 144 + 24 - 8, 0);
    private static final HeadingPoint BLUE_HI = new HeadingPoint(48 - 12, 144 + 24 - 8, 0);

    public static final GenericTeleOp.AutoAlignPoints RED = new GenericTeleOp.AutoAlignPoints(
            RED_PS_L,
            RED_PS_M,
            RED_PS_R,
            RED_HI
    );

    public static final GenericTeleOp.AutoAlignPoints BLUE = new GenericTeleOp.AutoAlignPoints(
            BLUE_PS_L,
            BLUE_PS_M,
            BLUE_PS_R,
            BLUE_HI
    );
}
