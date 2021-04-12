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

import org.rx.core.Tickable;

import java.util.function.Supplier;

public class AutoJoystick extends Joystick implements Tickable {
    private final Supplier<Double> xSupplier;
    private final Supplier<Double> ySupplier;

    public AutoJoystick(Supplier<Double> xSupplier,
                        Supplier<Double> ySupplier) {
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
    }

    @Override
    public void tick() {
        update(
                xSupplier.get(),
                ySupplier.get()
        );
    }
}
