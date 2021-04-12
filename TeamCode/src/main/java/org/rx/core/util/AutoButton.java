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

/**
 * An extension of the {@code Button} class that implements the
 * {@link Tickable#tick()} method, allowing for an end user to update the
 * button's state more easily. To accomplish this, this class makes use of the
 * {@link Supplier} functionality provided in JDK8, letting us dynamically
 * poll the button's state every time we tick the button.
 */
public class AutoButton extends Button implements Tickable {
    /**
     * A {@code Supplier} to access whether or not the button is active.
     */
    private final Supplier<Boolean> isButtonActive;

    /**
     * Create a new {@code AutoButton} instance.
     *
     * @param isButtonActive a supplier that supplies whether or not the
     *                       button is currently active. This supplier is
     *                       used in the {@link #tick()} method.
     */
    public AutoButton(Supplier<Boolean> isButtonActive) {
        this.isButtonActive = isButtonActive;
    }

    /**
     * Tick the {@code AutoButton} once. This method polls the button's status
     * using the {@link Supplier#get()} method and updates the button
     * accordingly.
     */
    @Override
    public void tick() {
        update(isButtonActive.get());
    }
}
