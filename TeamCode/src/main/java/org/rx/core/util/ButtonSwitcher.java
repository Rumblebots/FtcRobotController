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
 * A specification of the {@link StateSwitcher} designed specifically for
 * buttons.
 *
 * @param <T> the type of state that the switcher should switch between.
 */
public class ButtonSwitcher<T> extends StateSwitcher<T> {
    /**
     * Create a new {@code ButtonSwitcher}.
     *
     * @param states a map of buttons and the states that that button's
     *               {@code isPressed} status corresponds to.
     */
    public ButtonSwitcher(Map<Button, T> states) {
        super(new HashMap<>(states.size()) {{
            states.forEach((button, t) -> put(button::isPressed, t));
        }});
    }
}
