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
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.edt.StaticArray;

import java.util.ArrayList;

/**
 * A specialized dynamic array type designed to reduce clutter in code (and
 * the amount of those damn diamond brackets).
 *
 * <p>
 * This class doesn't actually provide any utility other than making it easier
 * to construct and access a specific type of dynamic array.
 * </p>
 *
 * @author Colin Robertson
 */
public class DynamicString extends DynamicArray<String> {
    /**
     * Create a new {@code DynamicArray} instance without any values or
     * elements. Because there are no values or elements inserted into the
     * array at construction, the array will be entirely empty. New values
     * and elements can be added to the {@code DynamicArray} using any of the
     * insertion or append methods provided in this class.
     *
     * @see DynamicArray#add(Object)
     * @see DynamicArray#add(Object[])
     * @see DynamicArray#add(int, Object)
     */
    public DynamicString() {
        super();
    }

    /**
     * Create a new {@code DynamicArray} instance using a plain array of
     * elements as the initialization elements. These elements can be updated
     * later with the methods provided in the {@link DynamicArray} class.
     *
     * @param elements an array, containing all of the {@link Object} instances
     *                 that should be set as the {@code DynamicArray}'s
     *                 contents upon construction.
     */
    public DynamicString(String... elements) {
        super(elements);
    }

    /**
     * Create a new {@code DynamicArray} instance without any data. Instead of
     * data, we have a lovely integer, representing the minimum allocation
     * size of the array. This allocation size can be used to boost performance.
     * Allocating a larger size uses more memory but reduces CPU time when
     * dealing with arrays of that size of slightly smaller.
     *
     * @param minSize the minimum size of the array. By default, this value is
     *                10. Larger arrays can have sizes up to 10,000, 100,000,
     *                or even 1,000,000 - it's entirely up to you.
     */
    public DynamicString(int minSize) {
        super(minSize);
    }

    /**
     * Create a new {@code DynamicArray} instance using a plain array of
     * elements as the initialization elements. These elements can be updated
     * later with the methods provided in the {@link DynamicArray} class.
     *
     * @param minSize
     * @param elements an array, containing all of the {@link Object} instances
     *                 that should be set as the {@code DynamicArray}'s
     */
    public DynamicString(int minSize, Object[] elements) {
        super(minSize, elements);
    }

    /**
     * Create a new {@code DynamicArray} instance by using an {@code ArrayList}
     * instance to provide initialization values for the newly-constructed
     * array.
     *
     * @param elements an {@code ArrayList} of elements that should be
     *                 set to the dynamic array's contents. These elements
     *                 must be of the same type the newly-created
     *                 {@code DynamicArray} is of.
     */
    public DynamicString(ArrayList<String> elements) {
        super(elements);
    }

    /**
     * Create a new {@code DynamicArray} instance by using an {@code Arrayable}
     * object as the "base" that the dynamic array should be based on. This
     * can be useful when converting a static array to a dynamic array.
     *
     * @param arrayable the array object that should be the base of the new
     *                  dynamic array. Ex: {@link StaticArray} instance.
     */
    public DynamicString(Arrayable<String> arrayable) {
        super(arrayable);
    }
}
