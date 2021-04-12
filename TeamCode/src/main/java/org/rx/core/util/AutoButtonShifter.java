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

import java.util.HashMap;
import java.util.Map;

/**
 * The fully tickable variant of the {@link ButtonShifter}. This {@link Shifter}
 * implementation ticks each of the {@link AutoButton} instances that are
 * provided to the constructor of this class during the {@link #tick()} method
 * and then calls the {@link ButtonShifter#tick()} method afterwards. In
 * essence, this is a fully-automated implementation of the {@link Shifter}
 * interface - all you have to do is call the {@link #tick()} method.
 */
public class AutoButtonShifter extends ButtonShifter {
    /**
     * Create a new {@code AutoButtonShifter}.
     *
     * @param shiftDown the button that should shift the shifter down by 1.
     * @param shiftUp   the button that should shift the shifter up by 1.
     */
    public AutoButtonShifter(AutoButton shiftDown,
                             AutoButton shiftUp) {
        super(shiftDown, shiftUp);
    }

    /**
     * Create a new {@code AutoButtonShifter}.
     *
     * @param shiftAmounts a {@code Map<AutoButton, Integer>} that contains
     *                     a series of buttons and amounts that that button's
     *                     press status should shift the shifter by.
     */
    public AutoButtonShifter(Map<AutoButton, Integer> shiftAmounts) {
        super(new HashMap<>(shiftAmounts.size()) {{
            for (Entry<AutoButton, Integer> pair : shiftAmounts.entrySet()) {
                this.put(pair.getKey(), pair.getValue());
            }
        }});
    }

    /**
     * Tick each of the buttons contained in the {@code AutoButtonShifter}
     * and then call the {@link ButtonShifter#tick()} method.
     */
    @Override
    public void tick() {
        for (Button button : shiftAmounts.keySet()) {
            ((AutoButton) button).tick();
        }

        super.tick();
    }
}
