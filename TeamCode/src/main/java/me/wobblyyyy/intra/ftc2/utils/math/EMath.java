package me.wobblyyyy.intra.ftc2.utils.math;

public class EMath {
    public static final double DEFAULT_MIN = -1.0;
    public static final double DEFAULT_MAX = 1.0;

    public static double inModulus(double input,
                                   double min,
                                   double max) {
        double m = max - min;

        int nMax = (int) ((input - min) / m);
        input -= nMax * m;
        int nMin = (int) ((input - max) / m);
        input -= nMin * m;

        return input;
    }

    public static double clip(double target) {
        return clip(
                DEFAULT_MIN,
                DEFAULT_MAX,
                target
        );
    }

    public static double clip(double min,
                              double max,
                              double target) {
        return Math.max(
                min,
                Math.min(
                        max,
                        target
                )
        );
    }
}
