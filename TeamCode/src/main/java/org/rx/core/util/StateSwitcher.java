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

import java.util.Map;
import java.util.function.Supplier;

/**
 * A map-based switcher that iterates over a map of suppliers and states,
 * thus determining a state on every iteration.
 *
 * @param <T> the type of state that the switcher should output.
 */
public class StateSwitcher<T> implements Tickable {
    /**
     * The switcher's states.
     */
    protected final Map<Supplier<Boolean>, T> states;

    /**
     * The switcher's current state.
     */
    private T state;

    /**
     * Create a new {@code StateSwitcher} instance.
     *
     * @param states a {@code Map} containing suppliers and states that the
     *               true status of those suppliers should correspond to.
     */
    public StateSwitcher(Map<Supplier<Boolean>, T> states) {
        this.states = states;
    }

    /**
     * Get the switcher's current state.
     *
     * @return the switcher's current state.
     */
    public T getState() {
        return state;
    }

    /**
     * Tick the switcher once. These ticks occur by iterating over the map
     * of suppliers and {@code T} states. For every supplier that returns true,
     * the {@code StateSwitcher}'s state will be updated to the corresponding
     * {@code T} value.
     */
    @Override
    public void tick() {
        for (Map.Entry<Supplier<Boolean>, T> e : states.entrySet()) {
            boolean isTrue = e.getKey().get();
            T state = e.getValue();

            if (isTrue) {
                this.state = state;
            }
        }
    }
}
