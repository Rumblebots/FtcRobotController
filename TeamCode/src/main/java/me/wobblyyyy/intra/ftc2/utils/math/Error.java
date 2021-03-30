package me.wobblyyyy.intra.ftc2.utils.math;

public class Error {
    public static final double ZERO = 0.0;

    private double previous = 0.0;
    private double current = 0.0;
    private double total = 0.0;

    public Error() {
        this(
                ZERO,
                ZERO
        );
    }

    public Error(double current) {
        this(
                ZERO,
                current
        );
    }

    public Error(double previous,
                 double current) {
        this.previous = previous;
        this.current = current;
    }

    public static Error copy(Error e) {
        return new Error(
                e.getPrevious(),
                e.getCurrent()
        );
    }

    public void set(double number) {
        previous = current;
        current = number;

        total += number;
    }

    public double getPrevious() {
        return previous;
    }

    public double getCurrent() {
        return current;
    }

    public double getTotal() {
        return total;
    }
}
