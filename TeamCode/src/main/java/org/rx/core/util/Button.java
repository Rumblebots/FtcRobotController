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
 * A representation of a physical button with a boolean state. A button can
 * either be "active" or "inactive" - most typically, the button is active
 * whenever the physical button that corresponds to the encapsulated button
 * is held down, and inactive when the button is not held down.
 */
public class Button {
    /**
     * Was the button pressed last time the button was updated?
     */
    private boolean wasPressed = false;

    /**
     * Is the button currently pressed?
     *
     * <p>
     * A button is in a "pressed" state for the first update cycle where the
     * button is held down (active). On a controller with a button (labelled A
     * for the sake of argument), A would only be "pressed" for a single update
     * cycle where A is active.
     * </p>
     */
    private boolean isPressed = false;

    /**
     * Was the button released during the last update cycle?
     *
     * <p>
     * When a button's status goes from "active" to "inactive" the button
     * is considered to have been released.
     * </p>
     */
    private boolean wasReleased = false;

    /**
     * Is the button currently active?
     *
     * <p>
     * Button activity is defined more simply as whether or not the button
     * was held down in the last update cycle.
     * </p>
     */
    private boolean isActive = false;

    /**
     * Create a new {@code Button}.
     */
    public Button() {

    }

    /**
     * Update the {@code Button} by supplying a boolean indicating the state
     * of the button.
     *
     * @param currentlyPressed whether or not the button is currently active.
     *                         Button activity is defined as whether or not
     *                         the button is held down at the time of calling.
     */
    public void update(boolean currentlyPressed) {
        isPressed = !wasPressed && currentlyPressed;
        wasReleased = wasPressed && !currentlyPressed;
        isActive = wasPressed = currentlyPressed;
    }

    /**
     * Is the button currently pressed?
     *
     * @return whether or not the button is currently pressed. A button is
     * only considered to be "pressed" for the first update cycle in which
     * it is active.
     */
    public boolean isPressed() {
        return isPressed;
    }

    /**
     * Was the button released during the last update cycle?
     *
     * @return whether or not the button was released during the last update
     * cycle. A button is considered to have been released when its state goes
     * from active to inactive. A button is only released one time when the
     * button goes from active to inactive. After the button is released,
     * this method will return false.
     */
    public boolean wasReleased() {
        return wasReleased;
    }

    /**
     * Is the button currently active?
     *
     * @return whether or not the button is currently active. Button activity
     * can most generally be defined as whether or not the button is currently
     * being held down. This value will always return whatever was fet into the
     * update function in the last update cycle.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Is the button currently inactive?
     *
     * @return whether or not the button is inactive. This method returns
     * the inverse of the {@link #isActive()} method.
     * @see #isActive()
     */
    public boolean isInactive() {
        return !isActive();
    }
}
