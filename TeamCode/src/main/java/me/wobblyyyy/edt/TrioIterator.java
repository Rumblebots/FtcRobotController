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
 * An iterator utility class designed for trio maps or data structures that
 * contain three iterable sets of data.
 *
 * @param <A> the A parameter object type.
 * @param <B> the B parameter object type.
 * @param <C> the C parameter object type.
 * @author Colin Robertson
 */
public class TrioIterator<A, B, C> implements ItrTrio<A, B, C> {
    /**
     * The internally-stored index. This value is updated during iteration.
     * Otherwise, it's practically invalid and should be ignored.
     */
    private int index;

    /**
     * An internal consumer for exceptions. Exceptions that have nowhere else
     * to go will be caught and passed to this consumer.
     */
    private static final Consumer<Exception> exceptionConsumer =
            Throwable::printStackTrace;

    /**
     * An internal supplier for the {@link TrioMap} that this class will
     * iterate over.
     */
    private final TrioMapSupplier<A, B, C> supplier;

    /**
     * Create a new {@code TrioIterator} instance.
     *
     * @param supplier the iterator's map structure supplier.
     */
    public TrioIterator(TrioMapSupplier<A, B, C> supplier) {
        this.supplier = supplier;
    }

    /**
     * {@inheritDoc}
     *
     * @return the previous trio element. If this element isn't contained in
     * the iterated set, a null pointer exception will be thrown.
     * @throws NullPointerException if there is no previous element to
     *                              reference.
     */
    @Override
    public Trio<A, B, C> previousElement() {
        return supplier.get().getByIndex(index - 1);
    }

    /**
     * {@inheritDoc}
     *
     * @return the previous A element. If this element isn't contained in
     * the iterated set, a null pointer exception will be thrown.
     * @throws NullPointerException if there is no previous element to
     *                              reference.
     */
    @Override
    public A previousA() {
        return supplier.get().getByIndex(index - 1).getA();
    }

    /**
     * {@inheritDoc}
     *
     * @return the previous B element. If this element isn't contained in
     * the iterated set, a null pointer exception will be thrown.
     * @throws NullPointerException if there is no previous element to
     *                              reference.
     */
    @Override
    public B previousB() {
        return supplier.get().getByIndex(index - 1).getB();
    }

    /**
     * {@inheritDoc}
     *
     * @return the previous C element. If this element isn't contained in
     * the iterated set, a null pointer exception will be thrown.
     * @throws NullPointerException if there is no previous element to
     *                              reference.
     */
    @Override
    public C previousC() {
        return supplier.get().getByIndex(index - 1).getC();
    }

    /**
     * {@inheritDoc}
     *
     * @return the current trio element. This method shouldn't ever throw
     * any exceptions.
     */
    @Override
    public Trio<A, B, C> element() {
        return supplier.get().getByIndex(index);
    }

    /**
     * {@inheritDoc}
     *
     * @return the current A element. This method shouldn't ever throw
     * any exceptions.
     */
    @Override
    public A a() {
        return supplier.get().getByIndex(index).getA();
    }

    /**
     * {@inheritDoc}
     *
     * @return the current B element. This method shouldn't ever throw
     * any exceptions.
     */
    @Override
    public B b() {
        return supplier.get().getByIndex(index).getB();
    }

    /**
     * {@inheritDoc}
     *
     * @return the current C element. This method shouldn't ever throw
     * any exceptions.
     */
    @Override
    public C c() {
        return supplier.get().getByIndex(index).getC();
    }

    /**
     * {@inheritDoc}
     *
     * @return the next element. If there is no next element, a null pointer
     * exception should be thrown.
     * @throws NullPointerException if there is no next element.
     */
    @Override
    public Trio<A, B, C> nextElement() {
        return supplier.get().getByIndex(index + 1);
    }

    /**
     * {@inheritDoc}
     *
     * @return the next A element. If there is no next element, a null pointer
     * exception should be thrown.
     * @throws NullPointerException if there is no next element.
     */
    @Override
    public A nextA() {
        return supplier.get().getByIndex(index + 1).getA();
    }

    /**
     * {@inheritDoc}
     *
     * @return the next B element. If there is no next element, a null pointer
     * exception should be thrown.
     * @throws NullPointerException if there is no next element.
     */
    @Override
    public B nextB() {
        return supplier.get().getByIndex(index + 1).getB();
    }

    /**
     * {@inheritDoc}
     *
     * @return the next C element. If there is no next element, a null pointer
     * exception should be thrown.
     * @throws NullPointerException if there is no next element.
     */
    @Override
    public C nextC() {
        return supplier.get().getByIndex(index + 1).getC();
    }

    /**
     * {@inheritDoc}
     *
     * @return the previous index. This index can be -1.
     */
    @Override
    public int previousIndex() {
        return index - 1;
    }

    /**
     * {@inheritDoc}
     *
     * @return the current index.
     */
    @Override
    public int index() {
        return index;
    }

    /**
     * {@inheritDoc}
     *
     * @return the next index. This index can be out of bounds!
     */
    @Override
    public int nextIndex() {
        return index + 1;
    }

    /**
     * {@inheritDoc}
     *
     * @param runnable the code to be executed for each of the elements.
     * @param min the minimum element in the iterated set.
     * @param max the maximum element in the iterated set.
     */
    @Override
    public void forEach(Runnable runnable, int min, int max) {
        index = min;

        while (index <= max) {
            try {
                runnable.run();
            } catch (Exception e) {
                exceptionConsumer.accept(e);
            }

            index += 1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param runnable the code to be executed for each of the elements.
     */
    @Override
    public void forEach(Runnable runnable) {
        forEach(runnable, 0, supplier.get().size() - 1);
    }

    /**
     * {@inheritDoc}
     *
     * @param consumer the code to be executed for each of the elements.
     * @param min the minimum element in the iterated set.
     * @param max the maximum element in the iterated set.
     */
    @Override
    public void forEach(TriConsumer<A, B, C> consumer, int min, int max) {
        index = min;

        while (index <= max) {
            try {
                consumer.accept(
                       a(),
                       b(),
                       c()
                );
            } catch (Exception e) {
                exceptionConsumer.accept(e);
            }

            index += 1;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param consumer the code to be executed for each of the elements.
     */
    @Override
    public void forEach(TriConsumer<A, B, C> consumer) {
        forEach(consumer, 0, supplier.get().size() - 1);
    }
}
