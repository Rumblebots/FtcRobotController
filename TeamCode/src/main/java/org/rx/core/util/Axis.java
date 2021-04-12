/*
 * Copyright (c) 2021+
 *
 * This file is part of the "rlib" project and is licensed under
 * the GNU General Public License V3. If you did not receive
 * a copy of that license, you may find one online.
 *
 * https://github.com/Wobblyyyy/rlib
 */

package org.rx.core.util;

public class Axis {
    private double min = Double.NEGATIVE_INFINITY;
    private double max = Double.POSITIVE_INFINITY;
    private double dead = 0.0;
    private double value = 0.0;

    public Axis() {

    }

    public void update(double value) {
        value = Math.max(min, value);
        value = Math.min(max, value);

        if (Math.abs(value) < dead) value = 0;

        this.value = value;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setDeadZone(double dead) {
        this.dead = dead;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getDeadZone() {
        return dead;
    }

    public double value() {
        return value;
    }
}
