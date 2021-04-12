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
 * A {@link Tickable}-enabled {@link ButtonToggle}. This button-toggle combo
 * only requires the {@link #tick()} method to be called to update. A
 * {@link Supplier} is passed to this class's constructor, which is then
 * polled every {@code tick} execution.
 */
public class AutoButtonToggle extends ButtonToggle implements Tickable {
    /**
     * Accessor for the button's state.
     */
    private final Supplier<Boolean> isButtonActive;

    /**
     * Create a new {@code AutoButtonToggle}.
     *
     * @param isButtonActive a {@code Supplier} that provides whether or not
     *                       the button is active.
     */
    public AutoButtonToggle(Supplier<Boolean> isButtonActive) {
        this.isButtonActive = isButtonActive;
    }

    /**
     * Tick the {@code AutoButtonToggle} by calling the {@link #update(boolean)}
     * method.
     *
     * <p>
     * This method call will...
     * <ul>
     *     <li>Update the button's state ({@link Button#update(boolean)})</li>
     *     <li>Update the toggle's state accordingly.</li>
     * </ul>
     * </p>
     */
    @Override
    public void tick() {
        update(isButtonActive.get());
    }
}
