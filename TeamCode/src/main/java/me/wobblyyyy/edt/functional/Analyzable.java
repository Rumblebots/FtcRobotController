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

package me.wobblyyyy.edt.functional;

import me.wobblyyyy.edt.Arrayable;
import me.wobblyyyy.edt.DynamicArray;

import java.util.ArrayList;

/**
 * An extension of the dynamic array type that allows for a limited amount
 * of analysis on a given set of numbers. This is a specialization of the
 * dynamic array class designed exclusively for numbers - thus, this class
 * is able to provide methods, such as minimum, maximum, average, and sum.
 *
 * @param <T> the type of number which this array will hold. It's worth noting
 *            that this parameter type doesn't actually mean much, as all
 *            numbers are internally handled as doubles.
 * @author Colin Robertson
 */
public class Analyzable<T extends Number> extends DynamicArray<T> {
    /**
     * Is this analysis the first that's being executed?
     */
    private boolean isFirstRun = true;

    /**
     * A sum of all of the numbers.
     */
    private Number sum = 0;

    /**
     * The minimum number that's been inputted.
     */
    private Number minimum;

    /**
     * The maximum number that's been inputted.
     */
    private Number maximum;

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
    public Analyzable() {
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
    @SafeVarargs
    public Analyzable(T... elements) {
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
    public Analyzable(int minSize) {
        super(minSize);
    }

    /**
     * Create a new {@code DynamicArray} instance using a plain array of
     * elements as the initialization elements. These elements can be updated
     * later with the methods provided in the {@link DynamicArray} class.
     *
     * @param minSize  the minimum size of the array.
     * @param elements an array, containing all of the {@link Object} instances
     *                 that should be set as the {@code DynamicArray}'s
     */
    public Analyzable(int minSize, Object[] elements) {
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
    public Analyzable(ArrayList<T> elements) {
        super(elements);
    }

    /**
     * Create a new {@code DynamicArray} instance by using an {@code Arrayable}
     * object as the "base" that the dynamic array should be based on. This
     * can be useful when converting a static array to a dynamic array.
     *
     * @param arrayable the array object that should be the base of the new
     *                  dynamic array.
     */
    public Analyzable(Arrayable<T> arrayable) {
        super(arrayable);
    }

    /**
     * Internal method called whenever a number needs to be analyzed.
     *
     * @param value the number to analyze.
     */
    private void analyzeNumber(Number value) {
        double real = value.doubleValue();

        if (isFirstRun) {
            minimum = value;
            maximum = value;
            isFirstRun = false;
        } else {
            minimum = Math.min(minimum.doubleValue(), real);
            maximum = Math.max(maximum.doubleValue(), real);
        }

        sum = sum.doubleValue() + real;
    }

    /**
     * Internal method called whenever a number needs to be removed.
     *
     * @param value the number to remove.
     */
    private void removeNumber(Number value) {
        double real = value.doubleValue();

        if (minimum.doubleValue() == real) minimum = null;
        if (maximum.doubleValue() == real) maximum = null;

        sum = sum.doubleValue() - real;
    }

    /**
     * Add an array of elements to the {@code DynamicArray}'s internal list
     * of elements.
     *
     * <p>
     * These elements are, by default, appended to the end of the dynamic
     * array. If you'd like to control where the elements go, you can use...
     * {@link DynamicArray#add(int, Object)}
     * </p>
     *
     * @param values the values that should be appended to the
     *               {@code DynamicArray} instance's elements array.
     * @see DynamicArray#add(int, Object)
     * @see DynamicArray#add(Object)
     */
    @Override
    public void add(T[] values) {
        for (Number n : values) {
            analyzeNumber(n);
        }

        super.add(values);
    }

    /**
     * Add a value to the end of the {@code DynamicArray}'s internal array
     * of elements. If the array isn't large enough to support the requested
     * operation, the array will be grown by a factor that allows the array
     * to support the requested operation.
     *
     * @param value the value that should be appended to the end of the
     *              {@code DynamicArray}'s element array.
     * @see DynamicArray#add(Object[])
     * @see DynamicArray#add(int, Object)
     */
    @Override
    public void add(T value) {
        analyzeNumber(value);

        super.add(value);
    }

    /**
     * Add a value to the {@code DynamicArray}'s internal array of elements.
     * If the array isn't large enough to support the requested
     * operation, the array will be grown by a factor that allows the array
     * to support the requested operation.
     *
     * <p>
     * Unlike the {@link DynamicArray#add(Object)} method, this method gives
     * you control over the index of where the element is inserted. This method
     * will split the array in half and insert the value you requested at the
     * index you specified.
     * </p>
     *
     * @param index the target index for the specified value.
     * @param value the value to set to the index.
     * @see DynamicArray#add(Object[])
     * @see DynamicArray#add(Object)
     */
    @Override
    public void add(int index, T value) {
        analyzeNumber(value);

        super.add(index, value);
    }

    /**
     * Remove the last-added element from the dynamic array. This simply
     * removes the element that's at the array's end point.
     *
     * <p>
     * If the current active size of the {@code DynamicArray} is already too
     * small to remove another element from, nothing happens and this method
     * simply... well... doesn't do anything. Yeah. That's all.
     * </p>
     *
     * @see DynamicArray#remove(int)
     * @see DynamicArray#remove(int[])
     * @see DynamicArray#removeAfter(int)
     */
    @Override
    public void remove() {
        removeNumber(get(size() - 1));

        super.remove();
    }

    /**
     * Remove a set of values based on an array of index targets. Each of the
     * values for each of the corresponding index targets will be removed from
     * the {@code DynamicArray}'s register of elements.
     *
     * <p>
     * This method is rather expensive, and, preferably, should be avoided if
     * at all possible. This isn't to say that it won't work, or that it's a
     * slow method - it's still blazing fast, but by comparison to the rest
     * of the {@code DynamicArray} methods, this one is a bit slower.
     * </p>
     *
     * @param indices all the index targets to be removed.
     * @see DynamicArray#remove()
     * @see DynamicArray#remove(int)
     * @see DynamicArray#removeAfter(int)
     */
    @Override
    public void remove(int[] indices) {
        for (int i : indices) {
            removeNumber(get(i));
        }

        super.remove(indices);
    }

    /**
     * Remove a value from a specified index in the {@code DynamicArray}'s
     * internal array of elements.
     *
     * @param index the index that should have its corresponding value
     *              removed from existence.
     * @see DynamicArray#remove()
     * @see DynamicArray#remove(int[])
     * @see DynamicArray#removeAfter(int)
     */
    @Override
    public void remove(int index) {
        removeNumber(get(index));

        super.remove(index);
    }

    /**
     * Remove any elements after a specified cutoff index. Any element after
     * this point will be removed from the active array's data.
     *
     * @param cutoff the cutoff point. Any element after this point will be
     *               removed from the {@code DynamicArray}'s active area.
     * @see DynamicArray#remove(int[])
     * @see DynamicArray#remove()
     * @see DynamicArray#remove(int)
     */
    @Override
    public void removeAfter(int cutoff) {
        itr().forEach(this::removeNumber, cutoff, size() - 1);

        super.removeAfter(cutoff);
    }

    /**
     * Set a value at a specific index.
     *
     * <p>
     * Unlike the {@link DynamicArray#add(Object)} methods provided in this
     * class, this method will not add anything to the array. Rather, it will
     * simply set a value.
     * </p>
     *
     * @param index the index that should be set.
     * @param value the value to set to the given index.
     */
    @Override
    public void set(int index, T value) {
        Number previous = get(index);
        removeNumber(previous);
        analyzeNumber(value);

        super.set(index, value);
    }

    /**
     * Clear all of the values contained in the active portion of the array.
     *
     * <p>
     * It's important to note that this method WILL NOT re-size the array. The
     * active portion of the {@code DynamicArray} will still be whatever it was
     * before. This method will, however, set all of the elements in the active
     * portion of the array to be equal to null.
     * </p>
     *
     * @see DynamicArray#clearAndTrim()
     * @see DynamicArray#trim()
     * @see DynamicArray#reset()
     */
    @Override
    public void clear() {
        sum = 0;
        maximum = null;
        minimum = null;

        super.clear();
    }

    /**
     * This method will do exactly what these two methods normally do.
     *
     * <p>
     * <ul>
     *     <li>
     *         {@link DynamicArray#clear()}
     *     </li>
     *     <li>
     *         {@link DynamicArray#trim()}
     *     </li>
     * </ul>
     * </p>
     *
     * <p>
     * In case it matters at all: The array will be cleared before it is
     * trimmed. This shouldn't impact much, but yeah. There you go.
     * </p>
     *
     * @see DynamicArray#clear()
     * @see DynamicArray#trim()
     * @see DynamicArray#reset()
     */
    @Override
    public void clearAndTrim() {
        sum = 0;
        maximum = null;
        minimum = null;

        super.clearAndTrim();
    }

    /**
     * Reset the entire array. Unlike the clear method, this method will
     * actually reset the size of the array to zero and clear each and every
     * one of the elements that was originally contained in it.
     *
     * @see DynamicArray#clear()
     * @see DynamicArray#clearAndTrim()
     * @see DynamicArray#trim()
     */
    @Override
    public void reset() {
        sum = 0;
        maximum = null;
        minimum = null;

        super.reset();
    }

    /**
     * Get the smallest number that's stored in the array.
     *
     * @return the smallest number that's stored in the array.
     */
    public Number minimum() {
        return minimum;
    }

    /**
     * Get the largest number that's stored in the array.
     *
     * @return the largest number that's stored in the array.
     */
    public Number maximum() {
        return maximum;
    }

    /**
     * Get an average of all of the values that are contained in the array.
     *
     * @return an average of all of the values that are contained in the array.
     */
    public Number average() {
        return sum.doubleValue() / size();
    }
}
