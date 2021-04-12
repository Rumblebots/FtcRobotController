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
 * An interface shared between objects capable of existing in multiple integer
 * based states (colloquially known as gears).
 */
public interface Shifter {
    /**
     * Shift the shifter's gear by a specified amount.
     *
     * <p>
     * If this method call would cause the shifter's current gear to either
     * exceed the shifter's maximum or exceed the shifter's minimum, the
     * shifter's gear should be set accordingly to either the minimum or
     * maximum value.
     * </p>
     *
     * @param toShiftBy the amount to shift the shifter's current gear by. This
     *                  can be a positive or negative integer.
     */
    void shift(int toShiftBy);

    /**
     * Set the shifter's current gear.
     *
     * @param gear the gear to set the shifter to.
     */
    void setGear(int gear);

    /**
     * Get the shifter's current gear.
     *
     * @return the shifter's current gear.
     */
    int gear();

    /**
     * Set the shifter's current gear to the shifter's minimum gear.
     */
    void goToMin();

    /**
     * Set the shifter's current gear to the shifter's maximum gear.
     */
    void goToMax();

    /**
     * Set the shifter's minimum gear.
     *
     * @param min the shifter's minimum gear.
     */
    void setMin(int min);

    /**
     * Set the shifter's maximum gear.
     *
     * @param max the shifter's maximum gear.
     */
    void setMax(int max);

    /**
     * Get the shifter's minimum gear.
     *
     * @return the shifter's minimum gear.
     */
    int getMin();

    /**
     * Get the shifter's maximum gear.
     *
     * @return the shifter's maximum gear.
     */
    int getMax();
}
