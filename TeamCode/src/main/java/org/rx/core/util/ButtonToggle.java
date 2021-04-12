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

/**
 * A button that implements the {@link Toggle} interface's methods, allowing
 * for what is, in effect, the simplest form of toggle.
 *
 * @see Button
 * @see Toggle
 */
public class ButtonToggle extends Button implements Toggle {
    /**
     * The toggle's current state.
     */
    private boolean state = false;

    /**
     * Create a new {@code ButtonToggle}.
     */
    public ButtonToggle() {

    }

    /**
     * Update the button's state based on the provided parameter and
     * subsequently update the toggle's state based on whether or not the
     * button had just been pressed.
     *
     * @param isActive is the button currently active?
     */
    @Override
    public void update(boolean isActive) {
        super.update(isActive);

        if (isPressed()) toggle();
    }

    /**
     * Toggle the {@code Toggle}. If the toggle's state is {@code true},
     * this method should set the toggle's state to {@code false}. If the
     * toggle's state is {@code false}, this method should, predictably,
     * set the toggle's state to {@code true}.
     *
     * @return the toggle's updated state.
     */
    @Override
    public boolean toggle() {
        /*
         * An XOR operator is used here - it has the same effect as
         * the expression "state = !state".
         */
        return state ^= true;
    }

    /**
     * Get the {@code Toggle}'s state.
     *
     * @return the state the {@code Toggle} currently exists in.
     */
    @Override
    public boolean state() {
        return state;
    }

    /**
     * Manually set the {@code Toggle}'s state to a specific value. This
     * method should set the toggle's state to a certain value and allow
     * the toggle to function exactly as it did before prior to setting the
     * state to the toggle.
     *
     * @param state the state to set to the toggle.
     * @return the toggle's state. This value should be the same as the
     * {@code state} parameter provided to this method.
     */
    @Override
    public boolean setState(boolean state) {
        return this.state = state;
    }
}
