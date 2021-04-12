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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A container for multiple {@link Tickable} items. This class implements the
 * {@code Tickable} class itself, and makes use of the {@link #tick()} method.
 * The intention behind this class is to provide a way to create inheritable
 * ticking - for example, you could create a {@code Ticker} that would tick
 * several buttons for you, so you'd only have to call the
 * {@link Tickable#tick()} method of a single object.
 */
public class Ticker implements Tickable {
    /**
     * A list of {@link Tickable} elements.
     */
    private final List<Tickable> tickables;

    /**
     * Create a new {@code Ticker} without any elements.
     */
    public Ticker() {
        this(new ArrayList<>());
    }

    /**
     * Create a new {@code Ticker} using an array of elements.
     *
     * @param tickables the elements to tick.
     */
    public Ticker(Tickable... tickables) {
        this(Arrays.asList(tickables));
    }

    /**
     * Create a new {@code Ticker} using a list of elements.
     *
     * @param tickables the elements to tick.
     */
    public Ticker(List<Tickable> tickables) {
        this.tickables = tickables;
    }

    /**
     * Add a {@code Tickable} to the {@code Ticker}.
     *
     * @param tickable the {@code Tickable} to add.
     */
    public void add(Tickable tickable) {
        tickables.add(tickable);
    }

    /**
     * Execute each of the {@code Tickable}s contained in the list.
     */
    @Override
    public void tick() {
        tickables.forEach(Tickable::tick);
    }
}
