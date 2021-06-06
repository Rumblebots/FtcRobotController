package org.firstinspires.ftc.teamcode.ultimategoal.util;

import me.wobblyyyy.pathfinder.control.AbstractController;
import me.wobblyyyy.pathfinder.math.Invertable;

public class ExponentialController extends AbstractController {
    private final double exponent;
    private final double coefficient;

    public ExponentialController(double exponent,
                                 double coefficient) {
        this.exponent = exponent;
        this.coefficient = coefficient;
    }

    @Override
    public double calculate(double current) {
        double distance = target - current;
        boolean isNegative = distance < 0;
        double calculated = Math.pow(target / coefficient, exponent);
        return Invertable.apply(calculated, isNegative);
    }

    @Override
    public double calculate(double current, double target) {
        this.setTarget(target);
        return calculate(current);
    }
}
