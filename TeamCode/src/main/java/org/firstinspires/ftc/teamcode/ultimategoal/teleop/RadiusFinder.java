package org.firstinspires.ftc.teamcode.ultimategoal.teleop;

import me.wobblyyyy.pathfinder.geometry.AngleUtils;
import me.wobblyyyy.pathfinder.geometry.Distance;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;

public class RadiusFinder {
    /**
     * distance between the center of the robot and the shooter
     */
    private static final double DISTANCE_CENTER_SHOOTER = 92.0;

    /**
     * get closest point
     *
     * @param point  robot position
     * @param center center of power shot circle
     * @param radius radius of power shot circle
     * @return closest possible point along the arc
     */
    public static Point closestPoint(Point point,
                                     Point center,
                                     double radius) {
        double angleToPoint = Point.angleOfDeg(Point.pointOrIfNullZero(point), center);
        return Distance.inDirection(center, angleToPoint, radius);
    }

    /**
     * calculate angle from robot pos to power shot pos.
     * offset point by 6 inches to the right.
     *
     * @param pos          center of robot position
     * @param powerShotPos position of target power shot
     * @return desired angle between position and power shot position
     */
    public static double calcAngle(HeadingPoint pos,
                                   Point powerShotPos) {
        Point adjusted = Distance.inDirection(
                pos,
                AngleUtils.fixDeg(pos.getHeading() + 90),
                DISTANCE_CENTER_SHOOTER
        );
        return Point.angleOfDeg(adjusted, powerShotPos);
    }

    /**
     * Get the closest target point? Hopefully? Not sure? Maybe?
     *
     * @param currentPos the robot's position
     * @param targetPos  the power shot position
     * @return the closest point on the circle
     */
    public static HeadingPoint closestTargetPoint(Point currentPos,
                                                  Point targetPos) {
        double angle = Point.angleOfDeg(
                targetPos,
                currentPos
        );

        Point closestPoint = Distance.inDirection(
                targetPos,
                angle,
                DISTANCE_CENTER_SHOOTER
        );

        // TODO Fix angle
        double desiredAngle = AngleUtils.fixDeg(Point.angleOfDeg(
                closestPoint,
                targetPos
//        ) + 90 - Math.toDegrees(Math.atan(6.0/84)));
        ) + 83);

        return closestPoint.withHeading(desiredAngle);
    }
}
