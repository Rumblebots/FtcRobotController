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

/**
 * An interface shared between objects which can be "ticked". Ticks are method
 * calls that are designed to occur repeatedly, frequently hundreds of times
 * per second. These method calls are often most useful in loop-based
 * environments, which robotic applications often are.
 */
public interface Tickable {
    /**
     * Tick the object. The functionality of this method is largely dependent
     * on the specific implementation, but as a rule of thumb, this method
     * should in some way update the state of the object.
     */
    void tick();
}
