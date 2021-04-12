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

import org.rx.core.Tickable;
import org.rx.core.Ticker;
import org.rx.core.util.AutoAxis;
import org.rx.core.util.AutoButton;
import org.rx.core.util.AutoJoystick;

/**
 * An abstract representation of the Xbox controller interface. Please note
 * that this class doesn't implement any of the methods for actually
 * acquiring values from the physical controller - this class simply provides
 * some {@link AutoButton}s, some {@link AutoAxis}es, and, of course, the
 * very shiny and exciting {@link AutoJoystick}.
 */
public abstract class AbstractXbc implements XboxController, Tickable {
    public final AutoButton a = new AutoButton(this::a);
    public final AutoButton b = new AutoButton(this::b);
    public final AutoButton x = new AutoButton(this::x);
    public final AutoButton y = new AutoButton(this::y);
    public final AutoButton dpadUp = new AutoButton(this::dpadUp);
    public final AutoButton dpadRight = new AutoButton(this::dpadRight);
    public final AutoButton dpadDown = new AutoButton(this::dpadDown);
    public final AutoButton dpadLeft = new AutoButton(this::dpadLeft);
    public final AutoButton bumperRight = new AutoButton(this::bumperRight);
    public final AutoButton bumperLeft = new AutoButton(this::bumperLeft);
    public final AutoAxis triggerRight = new AutoAxis(this::triggerRight);
    public final AutoAxis triggerLeft = new AutoAxis(this::triggerLeft);
    public final AutoButton stickRight = new AutoButton(this::stickRight);
    public final AutoButton stickLeft = new AutoButton(this::stickLeft);
    public final AutoAxis rightX = new AutoAxis(this::rightX);
    public final AutoAxis rightY = new AutoAxis(this::rightY);
    public final AutoAxis leftX = new AutoAxis(this::leftX);
    public final AutoAxis leftY = new AutoAxis(this::leftY);
    public final AutoJoystick rightStick = new AutoJoystick(
            this::rightX,
            this::rightY
    );
    public final AutoJoystick leftStick = new AutoJoystick(
            this::leftX,
            this::leftY
    );
    public final AutoButton start = new AutoButton(this::start);
    public final AutoButton select = new AutoButton(this::select);
    public final Ticker ticker = new Ticker(
            a,
            b,
            x,
            y,
            dpadUp,
            dpadRight,
            dpadDown,
            dpadLeft,
            bumperRight,
            bumperLeft,
            triggerRight,
            triggerLeft,
            stickRight,
            stickLeft,
            rightX,
            rightY,
            leftX,
            leftY,
            rightStick,
            leftStick,
            start,
            select
    );

    @Override
    public void tick() {
        ticker.tick();
    }
}
