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

import java.util.function.Consumer;

/**
 * An interface for iterable types that only have a single supplier.
 *
 * <p>
 * The iterator class seeks to provide iteration functionality, allowing
 * users to quickly and effectively iterate over the entirety of the
 * dynamic array's contents.
 * </p>
 *
 * @param <E> the type which will be iterated over.
 * @author Colin Robertson
 * @see ItrSingle#previous()
 * @see ItrSingle#element()
 * @see ItrSingle#next()
 * @see ItrSingle#forEach(Runnable)
 * @see ItrSingle#forEach(Consumer)
 */
public interface ItrSingle<E> {
    /**
     * Get the previous element of the {@code DynamicArray}.
     *
     * @return the previous element of the {@code DynamicArray}. If there
     * is no previous element, meaning that the element that was just
     * used was the first element in the array, an array out of bounds
     * exception is thrown. By default, this is handled by the iterators
     * internal exception handler. To silence these exceptions, you can
     * surround your code in try/catch blocks.
     */
    E previous();

    /**
     * Get the current element of the {@code DynamicArray}'s array.
     *
     * @return the array's current element. This method won't ever throw
     * any exceptions, unlike {@link ItrSingle#previous()} and {@link ItrSingle#next()}.
     */
    E element();

    /**
     * Get the next element in the {@code DynamicArray}'s data set.
     *
     * @return the {@code DynamicArray}'s next element. If there is no
     * next element, meaning we've reached the end of the data set, an
     * {@link ArrayIndexOutOfBoundsException} is thrown, indicating that
     * the requested index could not be found. This exception can be
     * handled by try/catch code if you'd like to silence the exception.
     * Otherwise, the exception's stack trace will be printed.
     */
    E next();

    /**
     * Get the previous index.
     *
     * <p>
     * If this index is out of bounds, an exception is thrown. This
     * exception can be handled by try/catch code if you so desire.
     * </p>
     *
     * @return previous index, or current index minus one.
     */
    int previousIndex();

    /**
     * Get the current index of the array.
     *
     * @return the array iterator's current index.
     */
    int index();

    /**
     * Get the iterator's next index.
     *
     * <p>
     * If this index is out of bounds, an exception is thrown. This
     * exception can be handled by try/catch code if you so desire.
     * </p>
     *
     * @return next index, or current index plus one.
     */
    int nextIndex();

    /**
     * Iterate over the {@code DynamicArray}'s data set. Any potential
     * exceptions that occur during the loop's executions are handled
     * by the {@code Itr} class' default exception handler, which will
     * print the exception's stack trace.
     *
     * <p>
     * For-each iteration isn't very expensive. As long as the array is
     * not too large, running a for loop over each of the dynamic array's
     * elements doesn't take very long at all.
     * </p>
     *
     * <p>
     * As the for loop goes through its iteration and execution, the
     * index and current element will be updated. In addition to these
     * values being updated, the {@code previous()} and {@code next()}
     * methods will have their values changed. During loop execution, your
     * consumer can access information about the array it's iterating over,
     * such as previous, current, next elements and indexes.
     * <ul>
     *     <li>
     *         Get next element: {@link ItrSingle#next()}.
     *     </li>
     *     <li>
     *         Get previous element: {@link ItrSingle#previous()}.
     *     </li>
     *     <li>
     *         Get current element: {@link ItrSingle#element()}. Please note:
     *         it's advised that you make use of the consumerized methods
     *         provided by the iterator class to take advantage of the
     *         consumer framework in Java.
     *     </li>
     *     <li>
     *         Get next index: {@link ItrSingle#nextIndex()}
     *     </li>
     *     <li>
     *         Get previous index: {@link ItrSingle#previousIndex()}
     *     </li>
     *     <li>
     *         Get current index: {@link ItrSingle#index()}
     *     </li>
     * </ul>
     * </p>
     *
     * @param consumer the consumer that will consume the current element
     *                 of iteration. This element is updated every
     *                 iteration. Each element comes from the dynamic
     *                 array's ordered set of elements.
     * @param min      the minimum value that the for each loop should
     *                 iterate over. Anything below this index value will
     *                 be ignored by the loop.
     * @param max      the maximum value that the for each loop should
     *                 iterate over. Anything above this index value will
     *                 be ignored by the loop.
     */
    void forEach(Consumer<E> consumer, int min, int max);

    /**
     * Iterate over the {@code DynamicArray}'s data set. Any potential
     * exceptions that occur during the loop's executions are handled
     * by the {@code Itr} class' default exception handler, which will
     * print the exception's stack trace.
     *
     * <p>
     * For-each iteration isn't very expensive. As long as the array is
     * not too large, running a for loop over each of the dynamic array's
     * elements doesn't take very long at all.
     * </p>
     *
     * <p>
     * As the for loop goes through its iteration and execution, the
     * index and current element will be updated. In addition to these
     * values being updated, the {@code previous()} and {@code next()}
     * methods will have their values changed. During loop execution, your
     * consumer can access information about the array it's iterating over,
     * such as previous, current, next elements and indexes.
     * <ul>
     *     <li>
     *         Get next element: {@link ItrSingle#next()}.
     *     </li>
     *     <li>
     *         Get previous element: {@link ItrSingle#previous()}.
     *     </li>
     *     <li>
     *         Get current element: {@link ItrSingle#element()}. Please note:
     *         it's advised that you make use of the consumerized methods
     *         provided by the iterator class to take advantage of the
     *         consumer framework in Java.
     *     </li>
     *     <li>
     *         Get next index: {@link ItrSingle#nextIndex()}
     *     </li>
     *     <li>
     *         Get previous index: {@link ItrSingle#previousIndex()}
     *     </li>
     *     <li>
     *         Get current index: {@link ItrSingle#index()}
     *     </li>
     * </ul>
     * </p>
     *
     * @param consumer the consumer that will consume the current element
     *                 of iteration. This element is updated every
     *                 iteration. Each element comes from the dynamic
     *                 array's ordered set of elements.
     * @see ItrSingle#forEach(Consumer, int, int)
     * @see ItrSingle#forEach(Runnable)
     * @see ItrSingle#forEach(Runnable, int, int)
     */
    void forEach(Consumer<E> consumer);

    /**
     * Iterate over the {@code DynamicArray}'s data set. Any potential
     * exceptions that occur during the loop's executions are handled
     * by the {@code Itr} class' default exception handler, which will
     * print the exception's stack trace.
     *
     * <p>
     * For-each iteration isn't very expensive. As long as the array is
     * not too large, running a for loop over each of the dynamic array's
     * elements doesn't take very long at all.
     * </p>
     *
     * <p>
     * As the for loop goes through its iteration and execution, the
     * index and current element will be updated. In addition to these
     * values being updated, the {@code previous()} and {@code next()}
     * methods will have their values changed. During loop execution, your
     * consumer can access information about the array it's iterating over,
     * such as previous, current, next elements and indexes.
     * <ul>
     *     <li>
     *         Get next element: {@link ItrSingle#next()}.
     *     </li>
     *     <li>
     *         Get previous element: {@link ItrSingle#previous()}.
     *     </li>
     *     <li>
     *         Get current element: {@link ItrSingle#element()}. Please note:
     *         it's advised that you make use of the consumerized methods
     *         provided by the iterator class to take advantage of the
     *         consumer framework in Java.
     *     </li>
     *     <li>
     *         Get next index: {@link ItrSingle#nextIndex()}
     *     </li>
     *     <li>
     *         Get previous index: {@link ItrSingle#previousIndex()}
     *     </li>
     *     <li>
     *         Get current index: {@link ItrSingle#index()}
     *     </li>
     * </ul>
     * </p>
     *
     * @param runnable the {@code Runnable} code that will be executed
     *                 for each and every one of the requested range's
     *                 values. If you're only planning on using the current
     *                 element value, it is suggested that you make use of
     *                 the {@code Consumer}-based for each loops provided
     *                 in the iterator class. For more information on these
     *                 consumer based loops, look at the see tags here.
     * @param min      the minimum value that the for each loop should
     *                 iterate over. Anything below this index value will
     *                 be ignored by the loop.
     * @param max      the maximum value that the for each loop should
     *                 iterate over. Anything above this index value will
     *                 be ignored by the loop.
     * @see ItrSingle#forEach(Consumer)
     * @see ItrSingle#forEach(Consumer, int, int)
     */
    void forEach(Runnable runnable, int min, int max);

    /**
     * Iterate over the {@code DynamicArray}'s data set. Any potential
     * exceptions that occur during the loop's executions are handled
     * by the {@code Itr} class' default exception handler, which will
     * print the exception's stack trace.
     *
     * <p>
     * For-each iteration isn't very expensive. As long as the array is
     * not too large, running a for loop over each of the dynamic array's
     * elements doesn't take very long at all.
     * </p>
     *
     * <p>
     * As the for loop goes through its iteration and execution, the
     * index and current element will be updated. In addition to these
     * values being updated, the {@code previous()} and {@code next()}
     * methods will have their values changed. During loop execution, your
     * consumer can access information about the array it's iterating over,
     * such as previous, current, next elements and indexes.
     * <ul>
     *     <li>
     *         Get next element: {@link ItrSingle#next()}.
     *     </li>
     *     <li>
     *         Get previous element: {@link ItrSingle#previous()}.
     *     </li>
     *     <li>
     *         Get current element: {@link ItrSingle#element()}. Please note:
     *         it's advised that you make use of the consumerized methods
     *         provided by the iterator class to take advantage of the
     *         consumer framework in Java.
     *     </li>
     *     <li>
     *         Get next index: {@link ItrSingle#nextIndex()}
     *     </li>
     *     <li>
     *         Get previous index: {@link ItrSingle#previousIndex()}
     *     </li>
     *     <li>
     *         Get current index: {@link ItrSingle#index()}
     *     </li>
     * </ul>
     * </p>
     *
     * @param runnable the {@code Runnable} code that will be executed
     *                 for each and every one of the requested range's
     *                 values. If you're only planning on using the current
     *                 element value, it is suggested that you make use of
     *                 the {@code Consumer}-based for each loops provided
     *                 in the iterator class. For more information on these
     *                 consumer based loops, look at the see tags here.
     * @see ItrSingle#forEach(Consumer)
     * @see ItrSingle#forEach(Consumer, int, int)
     */
    void forEach(Runnable runnable);
}
