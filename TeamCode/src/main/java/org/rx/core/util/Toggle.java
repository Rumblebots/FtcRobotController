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
 * An interface capable of existing in one of two states. Typically, these
 * states are "on/off" or "true/false". Calling the {@link #toggle()} method
 * should invert the toggle's state based on the state the {@code Toggle}
 * exists in at the time of calling.
 */
public interface Toggle {
    /**
     * Toggle the {@code Toggle}. If the toggle's state is {@code true},
     * this method should set the toggle's state to {@code false}. If the
     * toggle's state is {@code false}, this method should, predictably,
     * set the toggle's state to {@code true}.
     *
     * @return the toggle's updated state.
     */
    boolean toggle();

    /**
     * Get the {@code Toggle}'s state.
     *
     * @return the state the {@code Toggle} currently exists in.
     */
    boolean state();

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
    boolean setState(boolean state);
}
