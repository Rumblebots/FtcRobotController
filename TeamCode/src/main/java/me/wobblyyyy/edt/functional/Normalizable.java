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

import me.wobblyyyy.edt.*;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Normalize pair sets (or maps) of data based. Normalization is intended to
 * proportionally bring down a set of values based on a set of keys and the
 * proprtion of a given key to the minimum possible key.
 *
 * <p>
 * Normalization can be rather expensive due to the boxing neccesary for
 * the current approach of normalization. Note that there are alternative
 * methods to normalization that utilize primitives and use less boxing -
 * however, many of the methods provided in this class, and especially the
 * static ones, provide a fair bit of utility and usability in contexts where
 * EDT data structures are common.
 * </p>
 *
 * @author Colin Robertson
 */
public class Normalizable extends DynamicMap<
        Supplier<Double>, Supplier<Double>> {
    /**
     * Create a new {@code DynamicMap} without any elements. Because the map
     * has no elements, you'll need to add some later.
     *
     * @see DynamicMap#add(Object, Object)
     * @see DynamicMap#add(int, Object, Object)
     * @see DynamicMap#add(KeyValue)
     * @see DynamicMap#add(int, KeyValue)
     */
    public Normalizable() {
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
    public Normalizable(int minSize) {
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
    public Normalizable(KeyValue<Supplier<Double>, Supplier<Double>>[] values) {
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
    public Normalizable(Map<Supplier<Double>, Supplier<Double>> inputMap) {
        super(inputMap);
    }

    /**
     * Normalize the values of the map and convert the newly-normalized values
     * into an {@link Arrayable} instance. This instance is specifically of the
     * type {@code Analyzable}, but it can be used anywhere the implemented
     * {@code Arrayable} interface can be.
     *
     * <p>
     * Normalization is the process of ensuring that each value is under the
     * minimum value, but each value is still proportional to another. As a
     * (rather specific) example: say you have a robot that has four wheels.
     * Each of these wheels is powered by a motor. All four of these motors
     * spin at a different speed. In order to rotate properly, these wheels
     * need to be spinning at proportional speeds all relative to each other.
     * To accomplish this, you can utilize this normalization technique - each
     * of the values will be appropriately scaled down until they fit under the
     * defined maximum.
     * </p>
     *
     * @return a normalized analyzable array of values based on the contents
     * of the map. Normalization is handled in a way that can initially be
     * rather confusing - read the API docs here to learn more.
     */
    public Analyzable<Double> normalize() {
        return normalizeSuppliers(getKeys(), getValues());
    }

    /**
     * Normalize the values of the map and convert the newly-normalized values
     * into an {@link Arrayable} instance. This instance is specifically of the
     * type {@code Analyzable}, but it can be used anywhere the implemented
     * {@code Arrayable} interface can be.
     *
     * <p>
     * Normalization is the process of ensuring that each value is under the
     * minimum value, but each value is still proportional to another. As a
     * (rather specific) example: say you have a robot that has four wheels.
     * Each of these wheels is powered by a motor. All four of these motors
     * spin at a different speed. In order to rotate properly, these wheels
     * need to be spinning at proportional speeds all relative to each other.
     * To accomplish this, you can utilize this normalization technique - each
     * of the values will be appropriately scaled down until they fit under the
     * defined maximum.
     * </p>
     *
     * @param keys   an array of key elements.
     * @param values an array of value elements.
     * @return a normalized analyzable array of values based on the contents
     * of the map. Normalization is handled in a way that can initially be
     * rather confusing - read the API docs here to learn more.
     */
    public static Analyzable<Double> normalize(Arrayable<Double> keys,
                                               Arrayable<Double> values) {
        AtomicReference<Double> min =
                new AtomicReference<>(Math.abs(keys.get(0)));

        Analyzable<Double> normalized = new Analyzable<>(keys.size());

        keys.itr().forEach(v -> {
            final double abs;
            min.set(Math.min(min.get(), (abs = Math.abs(v))));
            keys.set(keys.itr().index(), abs);
        });

        FrozenMap<Double, Double> pairs = new FrozenMap<>(keys, values);

        pairs.itr().forEach((key, value) ->
                normalized.add(value * (min.get() / Math.abs(key))));

        return normalized;
    }

    /**
     * Normalize the values of the map and convert the newly-normalized values
     * into an {@link Arrayable} instance. This instance is specifically of the
     * type {@code Analyzable}, but it can be used anywhere the implemented
     * {@code Arrayable} interface can be.
     *
     * <p>
     * Normalization is the process of ensuring that each value is under the
     * minimum value, but each value is still proportional to another. As a
     * (rather specific) example: say you have a robot that has four wheels.
     * Each of these wheels is powered by a motor. All four of these motors
     * spin at a different speed. In order to rotate properly, these wheels
     * need to be spinning at proportional speeds all relative to each other.
     * To accomplish this, you can utilize this normalization technique - each
     * of the values will be appropriately scaled down until they fit under the
     * defined maximum.
     * </p>
     *
     * @param keys   an array of suppliers for key elements.
     * @param values an array of suppliers for value elements.
     * @return a normalized analyzable array of values based on the contents
     * of the map. Normalization is handled in a way that can initially be
     * rather confusing - read the API docs here to learn more.
     */
    public static Analyzable<Double> normalizeSuppliers(
            Arrayable<Supplier<Double>> keys,
            Arrayable<Supplier<Double>> values) {
        StaticArray<Double> newKeys = new StaticArray<>(keys.size());
        StaticArray<Double> newValues = new StaticArray<>(keys.size());

        keys.itr().forEach(k -> newKeys.set(keys.itr().index(), k.get()));
        values.itr().forEach(v -> newValues.set(values.itr().index(), v.get()));

        return normalize(newKeys, newValues);
    }

    /**
     * Normalize the values of the map and convert the newly-normalized values
     * into an {@link Arrayable} instance. This instance is specifically of the
     * type {@code Analyzable}, but it can be used anywhere the implemented
     * {@code Arrayable} interface can be.
     *
     * <p>
     * Normalization is the process of ensuring that each value is under the
     * minimum value, but each value is still proportional to another. As a
     * (rather specific) example: say you have a robot that has four wheels.
     * Each of these wheels is powered by a motor. All four of these motors
     * spin at a different speed. In order to rotate properly, these wheels
     * need to be spinning at proportional speeds all relative to each other.
     * To accomplish this, you can utilize this normalization technique - each
     * of the values will be appropriately scaled down until they fit under the
     * defined maximum.
     * </p>
     *
     * @param keys   an array of key elements.
     * @param values an array of value elements.
     * @return a normalized analyzable array of values based on the contents
     * of the map. Normalization is handled in a way that can initially be
     * rather confusing - read the API docs here to learn more.
     */
    public static Analyzable<Double> normalize(Double[] keys,
                                               Double[] values) {
        return normalize(
                new StaticArray<>(keys),
                new StaticArray<>(values)
        );
    }

    /**
     * Normalize the values of the map and convert the newly-normalized values
     * into an {@link Arrayable} instance. This instance is specifically of the
     * type {@code Analyzable}, but it can be used anywhere the implemented
     * {@code Arrayable} interface can be.
     *
     * <p>
     * Normalization is the process of ensuring that each value is under the
     * minimum value, but each value is still proportional to another. As a
     * (rather specific) example: say you have a robot that has four wheels.
     * Each of these wheels is powered by a motor. All four of these motors
     * spin at a different speed. In order to rotate properly, these wheels
     * need to be spinning at proportional speeds all relative to each other.
     * To accomplish this, you can utilize this normalization technique - each
     * of the values will be appropriately scaled down until they fit under the
     * defined maximum.
     * </p>
     *
     * @param keys   an array of key elements.
     * @param values an array of value elements.
     * @return a normalized analyzable array of values based on the contents
     * of the map. Normalization is handled in a way that can initially be
     * rather confusing - read the API docs here to learn more.
     */
    public static double[] normalize(double[] keys,
                                     double[] values) {
        Analyzable<Double> normalized = normalize(
                Arrays.stream(keys).boxed().toArray(Double[]::new),
                Arrays.stream(values).boxed().toArray(Double[]::new)
        );

        double[] unboxed = new double[normalized.size()];
        normalized.itr().forEach(v -> {
            unboxed[normalized.itr().index()] = v;
        });

        return unboxed;
    }

    /**
     * Normalize the values of the map and convert the newly-normalized values
     * into an {@link Arrayable} instance. This instance is specifically of the
     * type {@code Analyzable}, but it can be used anywhere the implemented
     * {@code Arrayable} interface can be.
     *
     * <p>
     * Normalization is the process of ensuring that each value is under the
     * minimum value, but each value is still proportional to another. As a
     * (rather specific) example: say you have a robot that has four wheels.
     * Each of these wheels is powered by a motor. All four of these motors
     * spin at a different speed. In order to rotate properly, these wheels
     * need to be spinning at proportional speeds all relative to each other.
     * To accomplish this, you can utilize this normalization technique - each
     * of the values will be appropriately scaled down until they fit under the
     * defined maximum.
     * </p>
     *
     * @param keys   an array of suppliers for key elements.
     * @param values an array of suppliers for value elements.
     * @return a normalized analyzable array of values based on the contents
     * of the map. Normalization is handled in a way that can initially be
     * rather confusing - read the API docs here to learn more.
     */
    public static Analyzable<Double> normalizeSuppliers(
            Supplier<Double>[] keys,
            Supplier<Double>[] values) {
        return normalizeSuppliers(
                new StaticArray<>(keys),
                new StaticArray<>(values)
        );
    }
}
