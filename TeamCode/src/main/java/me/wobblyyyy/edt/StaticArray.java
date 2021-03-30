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

package me.wobblyyyy.edt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A non-resizable and static array. You can't add or remove elements from
 * this array. If you'd like to be able to do that, you can check out the
 * {@link DynamicArray} class, which provides all of that functionality.
 *
 * @param <E> the type of elements that the array should contain.
 * @author Colin Robertson
 */
public class StaticArray<E> implements Arrayable<E> {
    /**
     * The internal array. This array is final - there's no need to change
     * the size of it, etc. Because it's static, it isn't changed after its
     * construction.
     */
    private final Object[] array;

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
    public StaticArray(int size) {
        this.array = new Object[size];
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
    @SafeVarargs
    public StaticArray(E... elements) {
        this.array = elements;
    }

    /**
     * Create a new {@code StaticArray} instance by using an {@code Arrayable}
     * object to get the newly-created static array's contents.
     *
     * @param arrayable the array object that should be the base of the new
     *                  dynamic array. Ex: {@link StaticArray} instance.
     */
    public StaticArray(Arrayable<E> arrayable) {
        this.array = arrayable.toArray();
    }

    /**
     * Set a value at a given index.
     *
     * @param index the index to set a value to.
     * @param value the value that should be set to the given index.
     * @throws ArrayIndexOutOfBoundsException if the requested index doesn't
     *                                        fit in the static array's bounds.
     */
    @Override
    public void set(int index, E value) {
        array[index] = value;
    }

    /**
     * Get a value from a given index.
     *
     * @param index the index to get a value from.
     * @return the value at the specified index.
     * @throws ArrayIndexOutOfBoundsException if the requested index doesn't
     *                                        fit in the static array's bounds.
     */
    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        return (E) array[index];
    }

    /**
     * Get the size of the array.
     *
     * @return the size of the array.
     */
    @Override
    public int size() {
        return array.length;
    }

    /**
     * Get the index of a specified value.
     *
     * @param value the value to find the index of.
     * @return if the value is contained in the array, return the element's
     * index. If the value isn't contained in the array, return -1.
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public int indexOf(E value) {
        int min = 0;
        int max = size();

        if (value == null) {
            for (int i = min; i < max; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = min; i < max; i++) {
                if (value.equals(array[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Check to see if the array is empty.
     *
     * @return whether or not the array is empty.
     */
    @Override
    public boolean isEmpty() {
        return array.length == 0;
    }

    /**
     * Check to see if the array contains a specified value.
     *
     * @param value the value to query.
     * @return whether or not the array contains the specified value.
     */
    @Override
    public boolean contains(E value) {
        return indexOf(value) >= 0;
    }

    /**
     * Check to see if the requested index is a valid part of the internal
     * array used by the {@code Arrayable} element in question.
     *
     * @param index the index to check.
     * @return true if the index is contained in the active size of the array.
     * False if the index isn't contained in the active size of the array.
     */
    @Override
    public boolean checkIndex(int index) {
        return index >= 0 && index < size() - 1;
    }

    /**
     * Get the static array as a list.
     *
     * @return the static array as a list.
     */
    @Override
    public List<Object> toList() {
        return Arrays.asList(toArray());
    }

    /**
     * Get the static array as an array list.
     *
     * @return the static array as an array list.
     */
    @Override
    public ArrayList<Object> toArrayList() {
        return new ArrayList<>(Arrays.asList(toArray()));
    }

    /**
     * Get the static array as an array.
     *
     * @return the array.
     */
    @Override
    public Object[] toArray() {
        return array;
    }

    /**
     * Convert the static array to a given array type.
     *
     * @param base the base array. This array will be over-written while
     *             cloning the static array.
     * @return an array of type E.
     */
    @SuppressWarnings("unchecked")
    @Override
    public E[] toArray(E[] base) {
        base = (E[]) Arrays.copyOf(toArray(), base.length);
        return base;
    }

    /**
     * Convert the static array into a dynamic array of the same type.
     *
     * @return a newly-generated dynamic array.
     */
    public DynamicArray<E> toDynamicArray() {
        return new DynamicArray<>(this);
    }

    /**
     * Convert the static array into a double array.
     *
     * @return the static array as a double array.
     */
    @Override
    public Double[] toDoubleArray() {
        Object[] objects = toArray();
        Double[] doubles = new Double[objects.length];

        for (int i = 0; i < objects.length; i++) {
            doubles[i] = Double.parseDouble(objects[i].toString());
        }

        return doubles;
    }

    /**
     * Convert the static array into an integer array.
     *
     * @return the static array as an integer array.
     */
    @Override
    public Integer[] toIntegerArray() {
        Object[] objects = toArray();
        Integer[] integers = new Integer[objects.length];

        for (int i = 0; i < objects.length; i++) {
            integers[i] = Integer.parseInt(objects[i].toString());
        }

        return integers;
    }

    /**
     * Get the static array's iterator.
     *
     * @return the array's iterator.
     */
    @Override
    public ItrSingle<E> itr() {
        return _itr_internal_;
    }

    /**
     * Internal iterator - the user shouldn't access this directly.
     *
     * @see StaticArray#itr()
     */
    private final ArrayIterator<E> _itr_internal_ =
            new ArrayIterator<>(() -> this);
}
