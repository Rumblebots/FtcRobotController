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
import java.util.List;

/**
 * An interface to be shared by all implementations of the core array concept
 * that much of edt is based on.
 *
 * @param <E> the type of element that's stored in the array.
 */
public interface Arrayable<E> {
    /**
     * Set a value to a given index.
     *
     * @param index the index that should be set.
     * @param value the value that should be set to the given index.
     */
    void set(int index, E value);

    /**
     * Get a value from the {@code array} based on a provided index.
     *
     * @param index the index to query a value from.
     * @return the value that comes from the index. If the index is out of
     * the array's bounds, we don't do anything - an exception is thrown
     * anyways, so we don't need to return anything.
     */
    E get(int index);

    /**
     * Get the size of the {@code array}.
     *
     * @return the size of the {@code array}.
     */
    int size();

    /**
     * Get the index of a specified element. This method searches the entire
     * active array and attempts to find the index of the first instance of
     * that object. If that object is stored in the array multiple times,
     * only the first index will be returned.
     *
     * @param value the element to query. This element should be exactly
     *              the same as any of the elements that may be found. If this
     *              element isn't the same as any of the elements contained
     *              in the {@code array}, we return -1.
     * @return if the queried element is contained within the predefined range
     * of (0) to (array max), return the index of the first instance of the
     * queried element. If the queried element is not contained within the
     * given range, return -1.
     * @see DynamicArray#indexOfInRange(Object, int, int)
     */
    int indexOf(E value);

    /**
     * Check to see if the entire {@code array} is empty. The
     * {@code array} being empty is defined by the active size of
     * the array being equal to zero.
     *
     * @return whether or not the {@code array} is entirely empty.
     */
    boolean isEmpty();

    /**
     * Check to see if the {@code array} contains any instances of the
     * queried element. If an instance of the element is found, this method
     * will return true. If no instances of the element are found, this method
     * will return false.
     *
     * <p>
     * This method works by attempting to find the index of the queried element.
     * If the index is -1, we know the element isn't contained in the array,
     * and we return false. If the index is 0 or above, however, we know that
     * the element is contained somewhere in the array - thus, we return true.
     * </p>
     *
     * @param value the element to query.
     * @return true if the queried element is found inside of the {@code array}
     * at some point, false if it isn't.
     * @see DynamicArray#indexOf(Object)
     * @see DynamicArray#isEmpty()
     */
    boolean contains(E value);

    /**
     * Check to see if the requested index is a valid part of the internal
     * array used by the {@code Arrayable} element in question.
     *
     * @param index the index to check.
     * @return true if the index is contained in the active size of the array.
     * False if the index isn't contained in the active size of the array.
     */
    boolean checkIndex(int index);

    /**
     * Convert this instance of an {@code array} into an instance of the
     * much more well-known {@code List}.
     *
     * @return a converted {@code array} that has been transformed into
     * a list. Not an array list!
     */
    List<Object> toList();

    /**
     * Convert this instance of an {@code array} into an instance of
     * an {@link ArrayList}. This method calls the
     * {@link Arrayable#toArray()} method in order to convert the internal
     * facing array into an external facing one, and then creates a new
     * {@code ArrayList} instance based on the converted array.
     *
     * @return a newly-created {@code ArrayList} instance, composed of the
     * same elements as the calling {@code array}.
     */
    ArrayList<Object> toArrayList();

    /**
     * Convert this instance of an {@code array} into a plain array
     * instance by creating a clone of the elements contained in the calling
     * instance that is the same size as the active size.
     *
     * <p>
     * The internally-stored array is often larger than the external-facing
     * array for performance reasons - although it uses more memory, it takes
     * less CPU power to store un-used array values even when they aren't
     * accessible to the user. This means that the returned array here isn't
     * exactly the same as the array that's used inside of this class.
     * </p>
     *
     * @return the elements of the {@code array}, in array form. This
     * is a new array, not the same array - you can't modify this array and
     * expect the values contained in the internally-used array to be updated,
     * as this method is intentionally immutable.
     */
    Object[] toArray();

    /**
     * Get the array of objects contained in the {@code array} and cast
     * them into a given array. Please note: number types CAN NOT be cast here.
     * In order to get an array of numbers, you need to use the methods linked
     * in the {@code see} tags of this method.
     *
     * @param base the base array - this array will be over-written by a copied
     *             array, originating from the internal array.
     * @return the internally-stored array of elements, cast into a given
     * array of the type {@code E}.
     */
    E[] toArray(E[] base);

    /**
     * Get the {@code array}'s internal array and cast it into a
     * number array. This method is rather expensive, as it has to manually
     * unbox each of the objects contained inside of the array.
     *
     * @return a pre-casted array of double elements.
     */
    Double[] toDoubleArray();

    /**
     * Get the {@code array}'s internal array and cast it into a
     * number array. This method is rather expensive, as it has to manually
     * unbox each of the objects contained inside of the array.
     *
     * @return a pre-casted array of integer elements.
     */
    Integer[] toIntegerArray();

    /**
     * Get the {@code array}'s iterator nested class. The iterator class
     * provides iteration functionality for the {@code array} data
     * structure. Documentation on the iterator class's functionality is
     * available in the iterator class itself - take a look at the "see" tags
     * on this JavaDoc if you're confused.
     *
     * @return this {@code array}'s iterator nested class. This class
     * is generated at the {@code array}'s construction and does not
     * need to be maintained or interacted with other than to iterate over
     * the array of elements.
     */
    ItrSingle<E> itr();
}
