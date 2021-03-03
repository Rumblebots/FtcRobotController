/****
 * Made by Tejas Mehta
 * Made on Thursday, February 11, 2021
 * File Name: RPMThread
 * Package: org.firstinspires.ftc.teamcode.ultimategoal.shared*/
package org.firstinspires.ftc.teamcode.ultimategoal.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.tejasmehta.OdometryCore.localization.OdometryPosition;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ShooterThread extends Thread {
    DcMotor encoder;
    double prevPos;
    double speed = 0;
    double motorPower = 0.5;
    double rps = 0;
    double power = 0;
    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    ScheduledExecutorService speedControl = Executors.newSingleThreadScheduledExecutor();

    public ShooterThread(DcMotor encoder) {
        this.encoder = encoder;
        prevPos = encoder.getCurrentPosition();
    }

    @Override
    public void run() {
        exec.scheduleAtFixedRate(
                () -> {
                    double cPos = encoder.getCurrentPosition();
                    double countsToRotations = 28.0 * 3.0/2.0; // cpr * gear ratio
                    double rotations = (cPos - prevPos)/countsToRotations; // Get rotation count
//                    System.out.println("Rotations " + rotations);
                    rps = rotations * 4.0;
                    power = encoder.getPower();
//                    double rps = (rotations * 4); // Get rotations per second
                    double period = (250.0/rotations) / 1000;
//                    System.out.println("RPS: " + rps);
                    double angularVelocity = ((2.0/39.37) * 2 * Math.PI)/period;
//                    System.out.println("ANGLEV: " + angularVelocity);
//                    if (rps > 2) {
//                        System.out.println(rps);
//                    }
//                    speed = angularVelocity * (2.0/39.37); //Angular velocity (m/sec)
                    speed = angularVelocity/2;
                    prevPos = cPos;
                    // Angle: 45
                    // Horizontal from center (x dist): 144.5
                    // Vertical to bottom (offset): 258.823
                    // Goal to top 33 in, 35.5 to middle of top goal
                    // Goal to mid 21 in, 27 to middle of middle goal
                }, 0, 250, TimeUnit.MILLISECONDS);
        exec.scheduleAtFixedRate(() -> {

        }, 0, 250, TimeUnit.MILLISECONDS);
    }

    public void spinToSpeed(DcMotor flywheel1, DcMotor flywheel2, double xDist) {
        speedControl = Executors.newSingleThreadScheduledExecutor();
        speedControl.scheduleAtFixedRate(() -> {
            double cDist = calculateMissing(false, 27);
            if (cDist > xDist) {
                motorPower -= 0.05;
            } else if (cDist < xDist){
                motorPower+=0.05;
            }
            flywheel1.setPower(motorPower);
            flywheel2.setPower(motorPower);
        }, 0, 250, TimeUnit.MILLISECONDS);

    }

    public void stopSpin(DcMotor flywheel1, DcMotor flywheel2) {
        speedControl.shutdown();
        flywheel1.setPower(0);
        flywheel2.setPower(0);
    }

    public double getMaxRps() {
//        System.out.println(power);
//        if (power == 0) return -1;
        return rps;
    }
    public double getSpeed() {
        return speed;
    }

    public void stopThread() {
        exec.shutdown();
    }

    // Angle: 45
    // Horizontal from center (x dist): 144.5 mm
    // Vertical to bottom (offset): 258.823 mm -- 10.18in -- .258823 m
    // Goal to top 33 in, 35.5 to middle of top goal
    // Goal to mid 21 in, 27 to middle of middle goal
    // yDist = 30
    // xDist = Calculated below
    // Equation: .64(v * cos(45))^2 - (v * cos(45))^2 * x + 4.9x^2 = 0 <-- Solve for distance
    // Equation: (v * cos(45))^2 = -4.9x^2 / (.64 - x) <-- Solve for velocity
    double calculateMissing(boolean vMode, double yDist) {
        double height = (yDist/39.37) - .258823;
        double startToGoal = 135.5;
        double angleRads = Math.toRadians(45);
        double cosCalc = Math.cos(angleRads);
        double tanCalc = Math.tan(angleRads);
        OdometryPosition cPos = OdometryThread.getInstance().getCurrentPosition();
        if (vMode) {
            double distToGoal = (startToGoal - cPos.getY() -55.5)/39.37;
            double constant = -4.9 * (distToGoal * distToGoal);
            double vVal = (height * cosCalc) - (cosCalc * distToGoal * tanCalc);
            double rootable = constant/vVal;
            if (rootable < 0) {
                return -1;
            }
            return Math.sqrt(rootable);
        } else {
            double speed = getSpeed();
//            System.out.println("SPeed: " + speed);
            double aVal = 4.9;
            double bVal = -speed * Math.sin(angleRads);
            double cVal = -.258823;
            double root = (bVal * bVal) - 4 * aVal * cVal;
//            System.out.println("Root: " + root);
            double rooted = Math.sqrt(root);
            double sol1 = (-bVal + rooted) / (2 * aVal);
            double sol2 = (-bVal - rooted) / (2 * aVal);
            double t = Math.max(sol1, sol2);
            return speed * cosCalc * t;
//            double constant = height * Math.pow(speed * cosCalc, 2); // C value in a quadratic
//            double singleCoefficient = Math.pow(speed * cosCalc * tanCalc, 2); // B value in a quadratic
//            double quadraticCoefficient = 4.9;
//            double radicand = (singleCoefficient * singleCoefficient) - (4 * quadraticCoefficient * constant);
//            if (radicand < 0) {
//                return -1;
//            }
//            double rooted = Math.sqrt(radicand);
//            double sol1 = ((-singleCoefficient + rooted)/(2 * quadraticCoefficient)) * 3.281;
//            double sol2 = ((-singleCoefficient - rooted)/(2 * quadraticCoefficient)) * 3.281;
//            return Math.max(sol1, sol2);
        }
    }
}
