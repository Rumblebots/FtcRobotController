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

/**
 * A map that takes three parameters. Fancy, I know. Trio maps are based on
 * groups of three elements, each of which serves as a key and a value at
 * the same time. Because these trio maps utilize triplets, it can be very
 * challenging to conceptualize and abstract at times. For this reason, it's
 * suggested you make use of the other types of maps provided in this library
 * whenever possible, ensuring a lot more readable code.
 *
 * @param <A> the type of object that the A values are.
 * @param <B> the type of object that the B values are.
 * @param <C> the type of object that the C values are.
 * @author Colin Robertson
 */
public class TrioMap<A, B, C> {
    /**
     * The minimum size of the map. Each of the component dynamic arrays in
     * the trio map is initialized to that size.
     */
    private final int minSize;

    /**
     * A dynamic array.
     */
    private DynamicArray<A> elementsA;

    /**
     * B dynamic array.
     */
    private DynamicArray<B> elementsB;

    /**
     * C dynamic array.
     */
    private DynamicArray<C> elementsC;

    /**
     * The internal iterator class.
     */
    private final TrioIterator<A, B, C> _itr_internal_ =
            new TrioIterator<>(() -> this);

    /**
     * Initialize all of the elements in the array. This method should only
     * be utilized by trio map constructors.
     */
    private void initElements() {
        elementsA = new DynamicArray<>(minSize);
        elementsB = new DynamicArray<>(minSize);
        elementsC = new DynamicArray<>(minSize);
    }

    /**
     * Create a new trio map without any contained elements. This map has
     * a default size of 10.
     */
    public TrioMap() {
        minSize = 10;
        initElements();
    }

    /**
     * Create a new trio map without any contained elements and a set size.
     * Each of the internal arrays will allocate (at minimum) whatever size
     * you input.
     *
     * @param minSize the minimum size for each of the component arrays. Having
     *                a higher minimum size increases memory consumption and
     *                allocation time, but, in turn, reduces required CPU power.
     */
    public TrioMap(int minSize) {
        this.minSize = minSize;
        initElements();
    }

    /**
     * Add a trio of elements to the internal arrays.
     *
     * @param a the A value that should be added.
     * @param b the B value that should be added.
     * @param c the C value that should be added.
     */
    public void add(A a, B b, C c) {
        elementsA.add(a);
        elementsB.add(b);
        elementsC.add(c);
    }

    /**
     * Set B and C values based on A.
     *
     * @param a a value.
     * @param b b value.
     * @param c c value.
     */
    public void setByA(A a, B b, C c) {
        int index = elementsA.indexOf(a);

        elementsB.set(index, b);
        elementsC.set(index, c);
    }

    /**
     * Set A and C values based on B.
     *
     * @param a a value.
     * @param b b value.
     * @param c c value.
     */
    public void setByB(A a, B b, C c) {
        int index = elementsB.indexOf(b);

        elementsA.set(index, a);
        elementsC.set(index, c);
    }

    /**
     * Set A and B values based on C.
     *
     * @param a a value.
     * @param b b value.
     * @param c c value.
     */
    public void setByC(A a, B b, C c) {
        int index = elementsC.indexOf(c);

        elementsA.set(index, a);
        elementsB.set(index, b);
    }

    /**
     * Get the trio located at a specified index.
     *
     * @param index the index to find a trio based on.
     * @return the trio element located at the requested value. This trio
     * is made up of A, B, and C components and can be accessed using the
     * methods provided in the {@link Trio} class. These values can NOT
     * be modified - they're disconnected from the main component arrays.
     * Any changes you make to the array will not be reflected.
     * @see TrioMap#getByA(Object)
     * @see TrioMap#getByB(Object)
     * @see TrioMap#getByC(Object)
     */
    public Trio<A, B, C> getByIndex(int index) {
        return new Trio<>(
                elementsA.get(index),
                elementsB.get(index),
                elementsC.get(index)
        );
    }

    /**
     * Get the trio located at the index of the first occurrence of the
     * queried element.
     *
     * @param a the element to query.
     * @return the trio element located at the requested value. This trio
     * is made up of A, B, and C components and can be accessed using the
     * methods provided in the {@link Trio} class. These values can NOT
     * be modified - they're disconnected from the main component arrays.
     * Any changes you make to the array will not be reflected.
     */
    public Trio<A, B, C> getByA(A a) {
        int index = elementsA.indexOf(a);

        return getByIndex(index);
    }

    /**
     * Get the trio located at the index of the first occurrence of the
     * queried element.
     *
     * @param b the element to query.
     * @return the trio element located at the requested value. This trio
     * is made up of A, B, and C components and can be accessed using the
     * methods provided in the {@link Trio} class. These values can NOT
     * be modified - they're disconnected from the main component arrays.
     * Any changes you make to the array will not be reflected.
     */
    public Trio<A, B, C> getByB(B b) {
        int index = elementsB.indexOf(b);

        return getByIndex(index);
    }

    /**
     * Get the trio located at the index of the first occurrence of the
     * queried element.
     *
     * @param c the element to query.
     * @return the trio element located at the requested value. This trio
     * is made up of A, B, and C components and can be accessed using the
     * methods provided in the {@link Trio} class. These values can NOT
     * be modified - they're disconnected from the main component arrays.
     * Any changes you make to the array will not be reflected.
     */
    public Trio<A, B, C> getByC(C c) {
        int index = elementsC.indexOf(c);

        return getByIndex(index);
    }

    /**
     * Remove the trio located at the requested index.
     *
     * @param index the index to remove a trio from.
     */
    public void removeByIndex(int index) {
        elementsA.remove(index);
        elementsB.remove(index);
        elementsC.remove(index);
    }

    /**
     * Get a requested value by using two other components of the trio as
     * "key" elements. If these elements aren't contained in the array at the
     * same index, return null.
     *
     * @param b one of the two query keys.
     * @param c one of the two query keys.
     * @return if the index of both of the requested keys is the same, return
     * the requested value for that given index. If the index of the two
     * requested keys is not the same, return null. No exceptions thrown here.
     */
    public A getA(B b, C c) {
        final int testIndex;
        int index = (testIndex = elementsB.indexOf(b))
                == elementsC.indexOf(c) ? testIndex : -1;

        return testIndex >= 0 ? elementsA.get(index) : null;
    }

    /**
     * Get a requested value by using two other components of the trio as
     * "key" elements. If these elements aren't contained in the array at the
     * same index, return null.
     *
     * @param a one of the two query keys.
     * @param c one of the two query keys.
     * @return if the index of both of the requested keys is the same, return
     * the requested value for that given index. If the index of the two
     * requested keys is not the same, return null. No exceptions thrown here.
     */
    public B getB(A a, C c) {
        final int testIndex;
        int index = (testIndex = elementsA.indexOf(a))
                == elementsC.indexOf(c) ? testIndex : -1;

        return testIndex >= 0 ? elementsB.get(index) : null;
    }

    /**
     * Get a requested value by using two other components of the trio as
     * "key" elements. If these elements aren't contained in the array at the
     * same index, return null.
     *
     * @param a one of the two query keys.
     * @param b one of the two query keys.
     * @return if the index of both of the requested keys is the same, return
     * the requested value for that given index. If the index of the two
     * requested keys is not the same, return null. No exceptions thrown here.
     */
    public C getC(A a, B b) {
        final int testIndex;
        int index = (testIndex = elementsA.indexOf(a))
                == elementsB.indexOf(b) ? testIndex : -1;

        return testIndex >= 0 ? elementsC.get(index) : null;
    }

    /**
     * Put a value based on two other key values. If both of these key values
     * are not included in the trio map at the time this method is executed,
     * a new entry will be added to the trio map with the specified A, B, and
     * C values. If the key values are present, the desired value will be
     * updated.
     *
     * @param a one of the three A, B, C values.
     * @param b one of the three A, B, C values.
     * @param c one of the three A, B, C values.
     */
    @SuppressWarnings("DuplicatedCode")
    public void putA(A a, B b, C c) {
        int indexB = elementsB.indexOf(b);
        int indexC = elementsC.indexOf(c);

        if (indexB == indexC) {
            elementsA.set(indexB, a);
            return;
        }

        if (indexB < 0 || indexC < 0) {
            add(a, b, c);
        }
    }

    /**
     * Put a value based on two other key values. If both of these key values
     * are not included in the trio map at the time this method is executed,
     * a new entry will be added to the trio map with the specified A, B, and
     * C values. If the key values are present, the desired value will be
     * updated.
     *
     * @param a one of the three A, B, C values.
     * @param b one of the three A, B, C values.
     * @param c one of the three A, B, C values.
     */
    @SuppressWarnings("DuplicatedCode")
    public void putB(A a, B b, C c) {
        int indexA = elementsA.indexOf(a);
        int indexC = elementsC.indexOf(c);

        if (indexA == indexC) {
            elementsB.set(indexA, b);
            return;
        }

        if (indexA < 0 || indexC < 0) {
            add(a, b, c);
        }
    }

    /**
     * Put a value based on two other key values. If both of these key values
     * are not included in the trio map at the time this method is executed,
     * a new entry will be added to the trio map with the specified A, B, and
     * C values. If the key values are present, the desired value will be
     * updated.
     *
     * @param a one of the three A, B, C values.
     * @param b one of the three A, B, C values.
     * @param c one of the three A, B, C values.
     */
    @SuppressWarnings("DuplicatedCode")
    public void putC(A a, B b, C c) {
        int indexA = elementsA.indexOf(a);
        int indexB = elementsB.indexOf(b);

        if (indexA == indexB) {
            elementsC.set(indexA, c);
            return;
        }

        if (indexA < 0 || indexB < 0) {
            add(a, b, c);
        }
    }

    /**
     * Remove the trio located at the index of the first occurrence of the
     * queried value.
     *
     * @param a the value to query by.
     */
    public void removeByA(A a) {
        removeByIndex(elementsA.indexOf(a));
    }

    /**
     * Remove the trio located at the index of the first occurrence of the
     * queried value.
     *
     * @param b the value to query by.
     */
    public void removeByB(B b) {
        removeByIndex(elementsB.indexOf(b));
    }

    /**
     * Remove the trio located at the index of the first occurrence of the
     * queried value.
     *
     * @param c the value to query by.
     */
    public void removeByC(C c) {
        removeByIndex(elementsC.indexOf(c));
    }

    /**
     * Get the size of the trio map.
     *
     * @return the trio map's size.
     */
    public int size() {
        return elementsA.size();
    }

    /**
     * Reset the entire trio map. This empties the contents of each of the
     * linked component arrays and re-initializes them. This will reset
     * each of the arrays to the trio map's minimum size.
     */
    public void reset() {
        elementsA = new DynamicArray<>(minSize);
        elementsB = new DynamicArray<>(minSize);
        elementsC = new DynamicArray<>(minSize);
    }

    /**
     * Get the trio map's iterator. This iterator allows you to iterate over
     * the entire trio map.
     *
     * @return the trio map's iterator.
     */
    public ItrTrio<A, B, C> itr() {
        return _itr_internal_;
    }
}
