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
 * A fairly standard low-level controller that has all the same buttons and
 * features as an Xbox controller. If you're confused about what means what
 * and what goes where, you can do yourself a solid and look up the layout
 * of an Xbox controller - that might help. At least a little bit.
 */
public interface XboxController extends JoystickConsoleController {
    boolean a();
    boolean b();
    boolean x();
    boolean y();
    boolean dpadUp();
    boolean dpadRight();
    boolean dpadLeft();
    boolean dpadDown();
    boolean bumperRight();
    boolean bumperLeft();
    double triggerRight();
    double triggerLeft();
    boolean stickRight();
    boolean stickLeft();
    boolean start();
    boolean select();
}
