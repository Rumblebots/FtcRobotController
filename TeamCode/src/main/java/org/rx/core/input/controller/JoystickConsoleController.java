/*
 * Copyright (c) 2021+
 *
 * This file is part of the "rlib" project and is licensed under
 * the GNU General Public License V3. If you did not receive
 * a copy of that license, you may find one online.
 *
 * https://github.com/Wobblyyyy/rlib
 */

package org.rx.core.input.controller;

/**
 * A controller composed of two joysticks - a right joystick and a left
 * joystick. These joysticks can do maaaagical things.
 */
public interface JoystickConsoleController {
    /**
     * Get the right stick's X value.
     *
     * @return the right stick's X value.
     */
    double rightX();

    /**
     * Get the right stick's Y value.
     *
     * @return the right stick's Y value.
     */
    double rightY();

    /**
     * Get the left stick's X value.
     *
     * @return the left stick's X value.
     */
    double leftX();

    /**
     * Get the left stick's Y value.
     *
     * @return the left stick's Y value.
     */
    double leftY();
}
