/*
 * Copyright (c) 2021 Colin Robertson (wobblyyyy@gmail.com)
 *
 * This file is a part of the EDT (Extended Data Types) project. This project
 * is available on GitHub at:
 * https://github.com/Wobblyyyy/edt
 *
 * For more information on the data structures and types included in this
 * library, check out the online documentation for this project - all of which
 * is available via GitHub.
 *
 * All files in this project are licensed under the MIT license, meaning you're
 * free to distribute and modify code included in this project however you
 * see fit. For more information on the licensing behind this project, check
 * out the license file in the root directory of this project.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package me.wobblyyyy.edt.arrays;

import me.wobblyyyy.edt.Arrayable;
import me.wobblyyyy.edt.StaticArray;

/**
 * A specialized static array type designed to reduce clutter in code (and
 * the amount of those damn diamond brackets).
 *
 * <p>
 * This class doesn't actually provide any utility other than making it easier
 * to construct and access a specific type of static array.
 * </p>
 *
 * @author Colin Robertson
 */
public class StaticDouble extends StaticArray<Double> {
    /**
     * Create a new static array.
     *
     * <p>
     * Please note that when creating a static array, it is VERY STRONGLY
     * suggested that you make use of the constructor that accepts an array
     * as a parameter. Upcasting may sometimes cause issues here, which can
     * be very frustrating to debug.
     * </p>
     *
     * @param size how large the array should be.
     */
    public StaticDouble(int size) {
        super(size);
    }

    /**
     * Create a new static array.
     *
     * @param elements the array that the static array should use. This array
     *                 WILL NOT be copied. Remember, arrays are MUTABLE objects,
     *                 meaning that any reference to them returns the same object.
     *                 If you create a static array WITHOUT copying the array,
     *                 the array you passed to this constructor will be the
     *                 static array's array - modifying the array via the
     *                 methods in this class will modify that array as well.
     */
    public StaticDouble(Double... elements) {
        super(elements);
    }

    /**
     * Create a new {@code StaticArray} instance by using an {@code Arrayable}
     * object to get the newly-created static array's contents.
     *
     * @param arrayable the array object that should be the base of the new
     *                  dynamic array. Ex: {@link StaticArray} instance.
     */
    public StaticDouble(Arrayable<Double> arrayable) {
        super(arrayable);
    }
}
