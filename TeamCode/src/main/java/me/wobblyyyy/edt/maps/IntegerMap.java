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

package me.wobblyyyy.edt.maps;

import me.wobblyyyy.edt.DynamicMap;
import me.wobblyyyy.edt.KeyValue;

import java.util.Map;

/**
 * A specialized dynamic map type designed to reduce clutter in code (and
 * the amount of those damn diamond brackets).
 *
 * <p>
 * This class doesn't actually provide any utility other than making it easier
 * to construct and access a specific type of dynamic map.
 * </p>
 *
 * @author Colin Robertson
 */
public class IntegerMap<V> extends DynamicMap<Integer, V> {
    /**
     * Create a new {@code DynamicMap} without any elements. Because the map
     * has no elements, you'll need to add some later.
     *
     * @see DynamicMap#add(Object, Object)
     * @see DynamicMap#add(int, Object, Object)
     * @see DynamicMap#add(KeyValue)
     * @see DynamicMap#add(int, KeyValue)
     */
    public IntegerMap() {
        super();
    }

    /**
     * Create a new {@code DynamicMap} without any elements. Because the map
     * doesn't have any elements, you'll have to add some later.
     *
     * @param minSize the minimum size to be allocated by the internal dynamic
     *                arrays. Having a larger minimum size decreases the amount
     *                of time that will need to be spent on memory allocation
     *                in the future. Using a larger maximum size will, however,
     *                use more memory. The minimum size should generally be the
     *                average amount of entries your map will have. By default,
     *                this value is 10.
     */
    public IntegerMap(int minSize) {
        super(minSize);
    }

    /**
     * Create a new {@code DynamicMap}, using an array of key-value pairs of
     * the type {@code K}, {@code V}.
     *
     * @param values an array of keys and values to add to the map upon
     *               construction. Each of these values will be added to the
     *               map as soon as it's created. The minimum size of the
     *               map will be set to the length of the values array.
     */
    public IntegerMap(KeyValue<Integer, V>[] values) {
        super(values);
    }

    /**
     * Create a new {@code DynamicMap} using a map as a base. The elements in
     * this map will be converted to the dynamic map. Note that this conversion
     * can take a while, as it requires map iteration, which isn't exactly
     * blazing fast, especially on certain implementations of the map
     * interface. But it works!
     *
     * @param inputMap the map that the {@code DynamicMap}'s values should be
     *                 set to/cloned to.
     */
    public IntegerMap(Map<Integer, V> inputMap) {
        super(inputMap);
    }
}
