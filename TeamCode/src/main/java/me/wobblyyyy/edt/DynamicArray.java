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
 * An implementation of the dynamic array concept commonly seen in computer
 * science related fields. Although Java comes stock with several different
 * implementations of dynamic arrays, most of these are rather abstract and,
 * in turn, can lead to some performance losses.
 *
 * <p>
 * A dynamic array is an array that can be resized - meaning it can be grown,
 * shrunk, etc - whatever you'd like. Regular {@code Array} instances in Java
 * function fairly well already - they can store things and do tons of cool
 * stuff, y'know?
 * </p>
 *
 * <p>
 * Other iterations and implementations of dynamic array concepts, such as
 * the {@link java.util.Vector} class (which comes stock with the JDK) or even
 * the {@link ArrayList} class (which also comes stock with the JDK) are
 * already very effective. The {@code DynamicArray} class attempts to
 * distinguish itself from other implementations of this concept by nearly
 * quadrupling performance (in some cases) of large arrays by more liberally
 * allocating memory to array elements. This will use a lot more memory than
 * either of the two aforementioned data structures, however, performance is
 * increased - ESPECIALLY on larger arrays. For more information regarding
 * how performance is increased and the specific gains of performance,
 * keep reading! You know you want to.
 * </p>
 *
 * <p>
 * At first glance, it would appear that the {@code DynamicArray} class is
 * essentially the {@link java.util.Vector} class, which is already provided
 * stock by Java. This is somewhat true - both are re-sizable/dynamic arrays.
 * However, the {@code DynamicArray} class is better optimized for dealing
 * with large sets of data.
 * </p>
 *
 * <p>
 * The performance gains provided by utilizing a {@code DynamicArray} over
 * an {@link ArrayList}, for example, are rather noticeable. Performance tests
 * for an array 100,000 in size indicate that a {@code DynamicArray} instance
 * performs nearly 4x as fast as an {@code ArrayList} instance. These gains
 * don't transfer over nearly as cleanly to lower throughput operations,
 * however. As the {@code DynamicArray} can be effectively optimized for
 * dealing with tons of data, it performs faster on huge arrays. The
 * {@code ArrayList} gains a bit on the {@code DynamicArray} when it comes to
 * speed with lower-array-size operations, but the {@code DynamicArray} should
 * still outperform the {@code ArrayList}.
 * </p>
 *
 * <p>
 * Performance tests against an {@code ArrayList} reveal the following:
 * <ul>
 *     <li>
 *         {@code DynamicArray} time: 1.456149E8
 *     </li>
 *     <li>
 *         {@code ArrayList} time: 1.628252E8
 *     </li>
 *     <li>
 *         Delta: {@code DynamicArray} is faster, by about 10%.
 *     </li>
 * </ul>
 * This test was run by creating an empty {@code DynamicArray} and an empty
 * {@code ArrayList} and forcing them to add 1000, 2000, and then 3000 objects
 * to their arrays. Each array storage method had to dynamically allocate
 * memory while adding these elements.
 * </p>
 *
 * <p>
 * The {@code DynamicArray} class is an effective replacement for the
 * {@code ArrayList} class in many instances. Instances where using a dynamic
 * array instead of an array list wouldn't be appropriate are limited, for the
 * most part, to situations in which another method requires an array list
 * as a parameter, rather than an array list. This issue can largely be
 * circumvented by using the {@link DynamicArray#toArrayList()} method, which
 * will provide the {@code DynamicArray} as an array list, instead of a much
 * less adopted {@code DynamicArray} instance.
 * </p>
 *
 * <p>
 * In addition to providing methods for adding, deleting, removing, whatever -
 * whatever you want to call it - the {@code DynamicArray} class provides
 * iteration functionality by leveraging the {@link ArrayIterator} sub-class.
 * This class allows you to iterate over the entire data set quickly and
 * without having to worry about setting up a for loop or whatever other fear
 * you have. Look - I get it - for loops are incredibly scary and nobody really
 * knows how to use them. For more information regarding iterating over elements
 * stored in the {@code DynamicArray}, take a look at the iterator class and
 * the {@code JavaDoc} documentation provided there.
 * </p>
 *
 * @param <E> the type of elements stored in the dynamic array.
 * @author Colin Robertson
 */
public class DynamicArray<E> implements Arrayable<E> {
    /**
     * The default size of the dynamic array. We don't want to make this too
     * big or too small. Going too big means we'll end up allocating more
     * memory than we could ever even need, and going to small means we'll end
     * up having to grow the array very quickly.
     */
    private static final int DEFAULT_SIZE = 10;

    /**
     * An empty object array. Whenever a {@code DynamicArray} instance is
     * created without any input values, the {@code DynamicArray} uses this
     * value as the element value.
     */
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private static final Object[] EMPTY = new Object[DEFAULT_SIZE];

    /**
     * The internally-used iterator. This iterator is instanced upon the
     * creation of the {@code DynamicArray} and can not be interacted with,
     * aside from getting it, using the {@link DynamicArray#itr()} method.
     */
    private final ArrayIterator<E> _itr_internal_ =
            new ArrayIterator<>(() -> this);

    /**
     * The size of the currently-active portion of the array. Because this
     * {@code DynamicArray} class doesn't grow and shrink the array whenever
     * elements are added or removed in order to conserve processing power,
     * the active size of the array almost never matches the size of the
     * element array - where objects are actually stored.
     */
    private int activeSize;

    /**
     * The array's minimum size. This is used mostly in optimization for
     * very large arrays - say you have a {@code DynamicArray} instance that
     * you know will need to store thousands of objects. The minimum size of
     * the {@code DynamicArray} can be set somewhere in the thousands to
     * prevent having to grow the {@code DynamicArray} frequently.
     */
    private int minSize = 0;

    /**
     * All of the elements stored and used within the {@code DynamicArray}
     * instance. These elements are updated and queried via methods
     * provided in this class.
     */
    private Object[] elements;

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
    public DynamicArray() {
        elements = Arrays.copyOf(EMPTY, DEFAULT_SIZE);
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
    public DynamicArray(E... elements) {
        this.elements = Arrays.copyOf(elements, elements.length);
        activeSize = elements.length;
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
    public DynamicArray(int minSize) {
        this.minSize = minSize;
        this.elements = Arrays.copyOf(EMPTY, minSize);
    }

    /**
     * Create a new {@code DynamicArray} instance using a plain array of
     * elements as the initialization elements. These elements can be updated
     * later with the methods provided in the {@link DynamicArray} class.
     *
     * @param minSize  the minimum size of the array.
     * @param elements an array, containing all of the {@link Object} instances
     *                 that should be set as the {@code DynamicArray}'s
     *                 contents upon construction.
     */
    public DynamicArray(int minSize,
                        Object[] elements) {
        this.minSize = minSize;
        this.elements = elements;
        this.activeSize = elements.length;
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
    public DynamicArray(ArrayList<E> elements) {
        this.elements = elements.toArray();
        this.activeSize = elements.size();
    }

    /**
     * Create a new {@code DynamicArray} instance by using an {@code Arrayable}
     * object as the "base" that the dynamic array should be based on. This
     * can be useful when converting a static array to a dynamic array.
     *
     * @param arrayable the array object that should be the base of the new
     *                  dynamic array. Ex: {@link StaticArray} instance.
     */
    public DynamicArray(Arrayable<E> arrayable) {
        this.elements = arrayable.toArray();
        this.activeSize = arrayable.size();
    }

    /**
     * Get an out-of-bounds exception message based on a given index and
     * whether that index was too low or too high.
     *
     * @param h     if that index was too low or if it was too high. A state of
     *              true indicates that the index was too low, while a state of
     *              false indicates that the index was too high.
     * @param index the index that is out of bounds.
     * @param size  the maximum allowable size / index.
     * @return a {@code String} message that can be thrown as an exception
     * by another calling method. In this case, the calling method of this
     * method is almost always {@link DynamicArray#checkIndex(int)}.
     */
    private static String getOobException(boolean h,
                                          int index,
                                          int size) {
        String hm = h ? "LOW" : "HIGH";
        return "The specified index {" + index + "} is TOO " + hm + " " +
                "for a dynamic array of allocated size " + size;
    }

    /**
     * Check a given index to see if it is within the allowable range of
     * minimum and maximum index values. If it isn't, throw an exception
     * indicating that the specified index is invalid.
     *
     * @param index the index to check.
     */
    public boolean checkIndex(int index) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException(getOobException(
                    true,
                    index,
                    elements.length
            ));
        } else if (index > activeSize) {
            throw new ArrayIndexOutOfBoundsException(getOobException(
                    false,
                    index,
                    elements.length
            ));
        }

        return true;
    }

    /**
     * Check an array of index values by using the (provided elsewhere)
     * {@link DynamicArray#checkIndex(int)} method for the highest and lowest
     * index values. This operation can be expensive, as it requires the
     * array to be sorted before checking values.
     *
     * @param indices an array of the index values to check.
     */
    private void checkIndices(int[] indices) {
        Arrays.sort(indices);

        checkIndex(indices[0]);
        checkIndex(indices[indices.length - 1]);
    }

    /**
     * Calculate a new size for the array based on a minimum value. This method
     * over-estimates the array's new size to optimize CPU performance.
     *
     * @param minimum the minimum size of the array.
     * @return a new size for the array.
     */
    private int calculateNewSize(int minimum) {
        return (int) Math.ceil(minimum * 1.75);
    }

    /**
     * Calculate a minimum size requirement for the {@code DynamicArray} based
     * on the required growth of the array.
     *
     * @param required the required growth of the array.
     * @return a newly-calculated minimum.
     */
    private int calculateMinimum(int required) {
        return Math.max(
                minSize,
                required + activeSize
        );
    }

    /**
     * Grow the {@code DynamicArray} to extend to a specified minimum value.
     *
     * @param minimum the minimum value that the {@code DynamicArray} should
     *                extend to, at the very least.
     */
    private void grow(int minimum) {
        int newSize = calculateNewSize(minimum);
        elements = Arrays.copyOf(elements, newSize);
    }

    /**
     * Grow the {@code DynamicArray} by a specified size.
     *
     * @param required the required growth of the array.
     * @see DynamicArray#calculateMinimum(int)
     * @see DynamicArray#calculateNewSize(int)
     * @see DynamicArray#grow(int)
     */
    private void growByAtLeast(int required) {
        int minimum = calculateMinimum(required);
        grow(minimum);
    }

    /**
     * Add a value to the {@code DynamicArray} without checking if the value
     * can be added or if the array is large enough to fit the value.
     *
     * @param value the value that should be added to the array.
     */
    private void unsafeAdd(E value) {
        elements[activeSize] = value;
        activeSize++;
    }

    /**
     * Remove a value from the {@code DynamicArray} without performing any
     * checks to see if removing the value would work.
     */
    private void unsafeRemove() {
        activeSize--;
    }

    /**
     * Remove a value from the {@code DynamicArray} without performing any
     * background checks to ensure that the value can be removed.
     *
     * @param index the index of the element that should be removed.
     */
    private void unsafeRemoveAtIndex(int index) {
        System.arraycopy(
                elements,
                index + 1,
                elements,
                index,
                (activeSize - 1) - index
        );

        activeSize--;
    }

    /**
     * Remove all elements after a specified index.
     *
     * @param index the cutoff point - any element after this point will
     *              be removed from the {@code DynamicArray}'s array.
     */
    private void unsafeRemoveAfterIndex(int index) {
        int difference = activeSize - index;

        activeSize -= difference;
    }

    /**
     * Is the current array large enough to support a growth by a defined
     * increase in array size?
     *
     * @param increase the amount of growth required.
     * @return whether or not the current array is large enough.
     */
    private boolean isLargeEnough(int increase) {
        return elements.length >= activeSize + increase;
    }

    /**
     * Expand the {@code DynamicArray} instance based on a specified amount
     * to increase by.
     *
     * @param increase how much the size of the {@code DynamicArray} should
     *                 be increased by.
     */
    private void expandArray(int increase) {
        if (!isLargeEnough(increase)) {
            growByAtLeast(increase);
        }
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
    public void add(E[] values) {
        expandArray(values.length);

        for (E value : values) {
            unsafeAdd(value);
        }
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
    public void add(E value) {
        expandArray(1);

        unsafeAdd(value);
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
    public void add(int index,
                    E value) {
        checkIndex(index);
        expandArray(1);

        System.arraycopy(
                elements,
                index,
                elements,
                index + 1,
                activeSize - index
        );

        elements[index] = value;

        activeSize++;
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
    public void remove() {
        if (activeSize - 1 > minSize) {
            unsafeRemove();
        }
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
    public void remove(int[] indices) {
        checkIndices(indices);

        for (int index : indices) {
            unsafeRemoveAtIndex(index);
        }
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
    public void remove(int index) {
        checkIndex(index);

        unsafeRemoveAtIndex(index);
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
    public void removeAfter(int cutoff) {
        checkIndex(cutoff);

        unsafeRemoveAfterIndex(cutoff);
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
    public void set(int index,
                    E value) {
        final int increase = index > activeSize ? index - activeSize : 0;
        expandArray(increase);

        elements[index] = value;
    }

    /**
     * Get a value from the {@code DynamicArray} based on a provided index.
     *
     * <p>
     * Before returning the specified value, check to see if the given index
     * is valid using the {@link DynamicArray#checkIndex(int)} method. If the
     * index is out of bounds, we throw an {@code ArrayIndexOutOfBounds}
     * exception, indicating that the requested index is invalid.
     * </p>
     *
     * @param index the index to query a value from.
     * @return the value that comes from the index. If the index is out of
     * the array's bounds, we don't do anything - an exception is thrown
     * anyways, so we don't need to return anything.
     * @see DynamicArray#get(int[])
     */
    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);

        return (E) elements[index];
    }

    /**
     * Get an array of values based on an array of indices to query.
     *
     * <p>
     * Before returning the specified value, check to see if the given index
     * is valid using the {@link DynamicArray#checkIndex(int)} method. If the
     * index is out of bounds, we throw an {@code ArrayIndexOutOfBounds}
     * exception, indicating that the requested index is invalid.
     * </p>
     *
     * @param indices all of the indices to query the value of.
     * @return an array of the same size as the size of the input index array.
     * If no values are found, or no values are added, an empty array is
     * returned - that is, assuming there weren't any exceptions.
     * @see DynamicArray#get(int)
     */
    @SuppressWarnings("unchecked")
    public E[] get(int[] indices) {
        Object[] values = new Object[indices.length];

        for (int i = 0; i < values.length; i++) {
            values[i] = get(indices[i]);
        }

        return (E[]) values;
    }

    /**
     * Get the index of a specified element. This method searches the entire
     * active array and attempts to find the index of the first instance of
     * that object. If that object is stored in the array multiple times,
     * only the first index will be returned.
     *
     * <p>
     * This method uses the {@link DynamicArray#indexOfInRange(Object, int, int)}
     * method to determine the index of a given object within a certain range.
     * This method is essentially an overload - we use the default range of
     * the array, which is zero (array start) to the array's maximum.
     * </p>
     *
     * @param query the element to query. This element should be exactly
     *              the same as any of the elements that may be found. If this
     *              element isn't the same as any of the elements contained
     *              in the {@code DynamicArray}, we return -1.
     * @return if the queried element is contained within the predefined range
     * of (0) to (array max), return the index of the first instance of the
     * queried element. If the queried element is not contained within the
     * given range, return -1.
     * @see DynamicArray#indexOfInRange(Object, int, int)
     */
    @Override
    public int indexOf(E query) {
        return indexOfInRange(query, 0, activeSize);
    }

    /**
     * Get the index of a specified element, so long as it fits within that
     * range. If the element does not fit within that range, the element is
     * ignored, and thus, the index is ignored as well.
     *
     * @param query the element to query. This element should be the same
     *              element as the requested one. If it isn't, nothing will
     *              be found. If it is, the index will be returned.
     * @param min   the minimum index that should be searched. By default,
     *              this is zero.
     * @param max   the maximum index that should be searched. By default,
     *              this is the length of the {@code DynamicArray}.
     * @return if the queried element is contained in the range, return the
     * index of that queried element. If the queried element is not in the
     * range, however, return -1.
     * @see DynamicArray#indexOf(Object)
     */
    @SuppressWarnings("DuplicatedCode")
    public int indexOfInRange(E query,
                              int min,
                              int max) {
        Object[] es = elements;

        if (query == null) {
            for (int i = min; i < max; i++) {
                if (es[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = min; i < max; i++) {
                if (query.equals(es[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Check to see if the entire {@code DynamicArray} is empty. The
     * {@code DynamicArray} being empty is defined by the active size of
     * the array being equal to zero.
     *
     * @return whether or not the {@code DynamicArray} is entirely empty.
     * @see DynamicArray#size()
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Check to see if the {@code DynamicArray} contains any instances of the
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
     * @param query the element to query.
     * @return true if the queried element is found inside of the
     * {@code DynamicArray} at some point, false if it isn't.
     * @see DynamicArray#indexOf(Object)
     * @see DynamicArray#isEmpty()
     */
    @Override
    public boolean contains(E query) {
        return indexOf(query) >= 0;
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
    public void clear() {
        for (int i = 0; i < size(); i++) {
            elements[i] = null;
        }
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
    public void clearAndTrim() {
        clear();
        trim();
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
    public void reset() {
        elements = new Object[minSize];
    }

    /**
     * Trim the currently-stored element array into a smaller element array
     * by reducing the size of the array to the active size of the array.
     *
     * <p>
     * Array elements aren't always wiped from the array whenever that
     * element is removed. In order to optimize performance, these values are
     * often left alone until they're no longer needed. However, if you would
     * like to cut down on memory usage, using this method will allow you to
     * reduce the memory footprint of the {@code DynamicArray} by trimming
     * any values that aren't actively being used by the array.
     * </p>
     *
     * @see DynamicArray#reset()
     * @see DynamicArray#clear()
     * @see DynamicArray#clearAndTrim()
     */
    public void trim() {
        elements = Arrays.copyOf(elements, activeSize);
    }

    /**
     * Convert this instance of a {@code DynamicArray} into an instance of the
     * much more well-known {@code List}.
     *
     * @return a converted {@code DynamicArray} that has been transformed into
     * a list. Not an array list!
     * @see DynamicArray#toArray()
     * @see DynamicArray#toArrayList()
     */
    @Override
    public List<Object> toList() {
        return Arrays.asList(toArray());
    }

    /**
     * Convert this instance of a {@code DynamicArray} into an instance of
     * an {@link ArrayList}. This method calls the
     * {@link DynamicArray#toArray()} method in order to convert the internal
     * facing array into an external facing one, and then creates a new
     * {@code ArrayList} instance based on the converted array.
     *
     * @return a newly-created {@code ArrayList} instance, composed of the
     * same elements as the calling {@code DynamicArray}.
     * @see DynamicArray#toArray()
     * @see DynamicArray#toList()
     */
    @Override
    public ArrayList<Object> toArrayList() {
        return new ArrayList<>(Arrays.asList(toArray()));
    }

    /**
     * Get the size of the active portion of the element array.
     *
     * @return the size of the active portion of the element array.
     * @see DynamicArray#activeSize
     */
    @Override
    public int size() {
        return activeSize;
    }

    /**
     * Get the real size of the {@code DynamicArray}. The (very lovely)
     * {@link DynamicArray#size()} method will return the size of the active
     * portion of the array, while this method will return the size of the
     * entire element array.
     *
     * @return the size of the entire element array.
     * @see DynamicArray#activeSize
     */
    public int realSize() {
        return elements.length;
    }

    /**
     * Convert this instance of a {@code DynamicArray} into a plain array
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
     * @return the elements of the {@code DynamicArray}, in array form. This
     * is a new array, not the same array - you can't modify this array and
     * expect the values contained in the internally-used array to be updated,
     * as this method is intentionally immutable.
     * @see DynamicArray#toArrayList()
     * @see DynamicArray#toList()
     * @see DynamicArray#toDoubleArray()
     * @see DynamicArray#toIntegerArray()
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, activeSize);
    }

    /**
     * Get the array of objects contained in the {@code DynamicArray} and cast
     * them into a given array. Please note: number types CAN NOT be cast here.
     * In order to get an array of numbers, you need to use the methods linked
     * in the {@code see} tags of this method.
     *
     * @param base the base array - this array will be over-written by a copied
     *             array, originating from the internal array.
     * @return the internally-stored array of elements, cast into a given
     * array of the type {@code E}.
     * @see DynamicArray#toDoubleArray()
     * @see DynamicArray#toIntegerArray()
     */
    @Override
    @SuppressWarnings("unchecked")
    public E[] toArray(E[] base) {
        base = (E[]) toArray();
        return base;
    }

    /**
     * Get the {@code DynamicArray}'s internal array and cast it into a
     * number array. This method is rather expensive, as it has to manually
     * unbox each of the objects contained inside of the array.
     *
     * @return a pre-casted array of double elements.
     * @see DynamicArray#toIntegerArray()
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
     * Get the {@code DynamicArray}'s internal array and cast it into a
     * number array. This method is rather expensive, as it has to manually
     * unbox each of the objects contained inside of the array.
     *
     * @return a pre-casted array of integer elements.
     * @see DynamicArray#toDoubleArray()
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
     * Get the {@code DynamicArray}'s iterator nested class. The iterator class
     * provides iteration functionality for the {@code DynamicArray} data
     * structure. Documentation on the iterator class's functionality is
     * available in the iterator class itself - take a look at the "see" tags
     * on this JavaDoc if you're confused.
     *
     * @return this {@code DynamicArray}'s iterator nested class. This class
     * is generated at the {@code DynamicArray}'s construction and does not
     * need to be maintained or interacted with other than to iterate over
     * the array of elements.
     */
    @Override
    public ItrSingle<E> itr() {
        return _itr_internal_;
    }
}
