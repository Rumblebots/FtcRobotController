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
 * Iterator used and shared by all sorts of arrays.
 * <p>
 * {@inheritDoc}
 *
 * @param <E> the type of content that the iterator holds.
 * @author Colin Robertson
 */
public class ArrayIterator<E> implements ItrSingle<E> {
    /**
     * Provide a method of accessing the original arrayable element.
     */
    private final ArrayableSupplier<E> supplier;

    /**
     * Provide a consumer for exceptions. We don't want to just throw them
     * into the void, so we do something about it here. Yay!
     */
    private final Consumer<Exception> exceptionConsumer =
            Throwable::printStackTrace;

    /**
     * An internal index counter. This is reset on every iteration sequence.
     */
    private int index = 0;

    /**
     * Create a new {@code ArrayIterator} instance, using a supplier to supply
     * ourselves with an {@link Arrayable} instance.
     *
     * @param supplier a supplier for the target {@code Arrayable}.
     */
    public ArrayIterator(ArrayableSupplier<E> supplier) {
        this.supplier = supplier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E previous() {
        return supplier.get().get(index - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E element() {
        return supplier.get().get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E next() {
        return supplier.get().get(index + 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int previousIndex() {
        return index - 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int index() {
        return index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int nextIndex() {
        return index + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forEach(Consumer<E> consumer, int min, int max) {
        for (index = min; index <= max; index++) {
            try {
                consumer.accept(element());
            } catch (Exception e) {
                exceptionConsumer.accept(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forEach(Consumer<E> consumer) {
        forEach(consumer, 0, supplier.get().size() - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forEach(Runnable runnable, int min, int max) {
        for (index = min; index <= max; index++) {
            try {
                runnable.run();
            } catch (Exception e) {
                exceptionConsumer.accept(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forEach(Runnable runnable) {
        forEach(runnable, 0, supplier.get().size() - 1);
    }
}
