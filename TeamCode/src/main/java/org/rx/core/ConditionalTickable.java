/*
 * Copyright (c) 2021+
 *
 * This file is part of the "rlib" project and is licensed under
 * the GNU General Public License V3. If you did not receive
 * a copy of that license, you may find one online.
 *
 * https://github.com/Wobblyyyy/rlib
 */

package org.rx.core;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ConditionalTickable<T> implements Tickable {
    private final Supplier<T> supplier;

    private final Predicate<T> predicate;

    private final Tickable tickable;

    private boolean isDone = false;

    public ConditionalTickable(Supplier<T> supplier,
                               Predicate<T> predicate,
                               Tickable tickable) {
        this.supplier = supplier;
        this.predicate = predicate;
        this.tickable = tickable;
    }

    @Override
    public void tick() {
        if (predicate.test(supplier.get())) {
            /*
             * The test worked, meaning we should still continue.
             */
            tickable.tick();
        } else {
            /*
             * The test did not work, we should stop.
             */
            this.isDone = false;
        }
    }

    public boolean isDone() {
        return isDone;
    }
}
