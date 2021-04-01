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

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts three input arguments and returns no
 * result.  This is the three-arity specialization of {@link Consumer}.
 * Unlike most other functional interfaces, {@code TriConsumer} is expected
 * to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object, Object, Object)}.
 *
 * @param <A> the type of the first argument to the operation.
 * @param <B> the type of the second argument to the operation.
 * @param <C> the type of the third argument to the operation.
 * @author Colin Robertson
 * @see Consumer
 */
@FunctionalInterface
public interface TriConsumer<A, B, C> {
    /**
     * Perform this operation on the given arguments.
     *
     * @param a the first input argument.
     * @param b the second input argument.
     * @param c the third input argument.
     */
    void accept(A a, B b, C c);

    /**
     * Return a composed {@code TriConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation. If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation.
     * @return a composed {@code TriConsumer} that performs in sequence this
     * operation followed by the {@code after} operation.
     * @throws NullPointerException if {@code after} is null.
     */
    default TriConsumer<A, B, C> andThen(TriConsumer<? super A,
            ? super B,
            ? super C> after) {
        Objects.requireNonNull(after);

        return (aA, bB, cC) -> {
            accept(aA, bB, cC);
            after.accept(aA, bB, cC);
        };
    }
}
