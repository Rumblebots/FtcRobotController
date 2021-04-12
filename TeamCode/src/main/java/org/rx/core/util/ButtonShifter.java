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

import java.util.HashMap;
import java.util.Map;

/**
 * A shifter capable of using several {@code Button} instances stored in a map
 * with integers to shift gears. This shifter utilizes a {@code Map} that
 * stores each of the buttons that should shift the shifter and a corresponding
 * integer value that represents how much the shifter should shift by if that
 * button's {@link Button#isPressed()} method returns true.
 */
public class ButtonShifter implements Shifter, Tickable {
    /**
     * A map containing the shifter's buttons and the values those buttons
     * should shift by.
     */
    protected final Map<Button, Integer> shiftAmounts;

    /**
     * The shifter's current gear.
     */
    private int gear = 0;

    /**
     * The shifter's minimum value.
     *
     * <p>
     * By default, this value is set to 0x7fffffff, or the lowest possible
     * integer value. In other words, there is no minimum.
     * </p>
     */
    private int min = 0x7fffffff;

    /**
     * The shifter's maximum value.
     *
     * <p>
     * By default, this value is set to 0x80000000, or the maximum possible
     * integer value. In other words, there is no maximum.
     * </p>
     */
    private int max = 0x80000000;

    /**
     * Create a new {@code ButtonShifter}.
     *
     * @param shiftDown the button that should shift the shifter down by 1.
     * @param shiftUp   the button that should shift the shifter up by 1.
     */
    public ButtonShifter(Button shiftDown,
                         Button shiftUp) {
        this(new HashMap<>(2) {{
            put(shiftDown, -1);
            put(shiftUp, 1);
        }});
    }

    /**
     * Create a new {@code ButtonShifter}.
     *
     * @param shiftAmounts a {@code Map<Button, Integer>} that contains
     *                     a series of buttons and amounts that that button's
     *                     press status should shift the shifter by.
     */
    public ButtonShifter(Map<Button, Integer> shiftAmounts) {
        this.shiftAmounts = shiftAmounts;
    }

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
    @Override
    public void shift(int toShiftBy) {
        int shifted = gear + toShiftBy;

        shifted = Math.max(this.min, shifted);
        shifted = Math.min(this.max, shifted);

        this.gear = shifted;
    }

    /**
     * Set the shifter's current gear.
     *
     * @param gear the gear to set the shifter to.
     */
    @Override
    public void setGear(int gear) {
        this.gear = gear;
    }

    /**
     * Get the shifter's current gear.
     *
     * @return the shifter's current gear.
     */
    @Override
    public int gear() {
        return gear;
    }

    /**
     * Set the shifter's current gear to the shifter's minimum gear.
     */
    @Override
    public void goToMin() {
        this.gear = min;
    }

    /**
     * Set the shifter's current gear to the shifter's maximum gear.
     */
    @Override
    public void goToMax() {
        this.gear = max;
    }

    /**
     * Set the shifter's minimum gear.
     *
     * @param min the shifter's minimum gear.
     */
    @Override
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Set the shifter's maximum gear.
     *
     * @param max the shifter's maximum gear.
     */
    @Override
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Get the shifter's minimum gear.
     *
     * @return the shifter's minimum gear.
     */
    @Override
    public int getMin() {
        return min;
    }

    /**
     * Get the shifter's maximum gear.
     *
     * @return the shifter's maximum gear.
     */
    @Override
    public int getMax() {
        return max;
    }

    /**
     * Check if any of the buttons in the {@code Map<Button, Integer>} that
     * stores buttons and their respective shift values are active. If any
     * of these buttons are, in fact, active, shift the current gear by
     * whatever value is associated with the pressed button.
     *
     * <p>
     * This method does not stop after only a single shift has taken place.
     * If several buttons are pressed at the same time, several shifts will
     * happen at the same time. Of course, these shifts occur synchronously,
     * so they don't happen at THE SAME time, but you know what I mean.
     * </p>
     */
    @Override
    public void tick() {
        for (Map.Entry<Button, Integer> pair : shiftAmounts.entrySet()) {
            Button button = pair.getKey();
            Integer amount = pair.getValue();

            if (button.isPressed()) {
                shift(amount);
            }
        }
    }
}
