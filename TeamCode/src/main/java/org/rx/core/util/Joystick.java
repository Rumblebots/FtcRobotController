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

public class Joystick {
    private Axis x;
    private Axis y;

    public void update(double x,
                       double y) {
        updateX(x);
        updateY(y);
    }

    public void updateX(double x) {
        this.x.update(x);
    }

    public void updateY(double y) {
        this.y.update(y);
    }

    public void setDeadZone(double dead) {
        double actualDead = Math.hypot(dead, dead);

        x.setDeadZone(actualDead);
        y.setDeadZone(actualDead);
    }
}
