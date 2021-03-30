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
 * An implementation of the {@code Mappable} interface designed exclusively
 * for applications in which the contents of the map won't ever be updated.
 *
 * <p>
 * These values and keys cannot ever be updated. There are several other
 * implementations of the {@code Mappable} interface that allow for these
 * values to be changed - see {@link DynamicMap} (completely changeable) and
 * {@link StaticMap} (only values can be changed) to learn more.
 * </p>
 *
 * <p>
 * The primary advantage to using a frozen map over a static map or a dynamic
 * map comes from the fact that you can't accidentally change a value without
 * getting some sort of exception.
 * </p>
 *
 * @param <K> the type of object the keys belong to.
 * @param <V> the type of object the values belong to.
 * @author Colin Robertson
 */
public class FrozenMap<K, V> implements Mappable<K, V> {
    /**
     * The map's internal keys registry.
     */
    private final StaticArray<K> keys;

    /**
     * The map's internal values registry.
     */
    private final StaticArray<V> values;

    /**
     * Internal reference to iterator class.
     */
    private final MapIterator<K, V> _itr_internal_ =
            new MapIterator<>(() -> this);

    /**
     * Create a new {@code FrozenMap} instance.
     *
     * <p>
     * After an instance of this map is created, the values stored in this
     * map cannot be updated whatsoever. The map is entirely frozen, or static,
     * or constant, or whatever you want to call it.
     * </p>
     *
     * <p>
     * If the size of the {@code keys} and {@code values} arrays are different,
     * this constructor will throw an exception indicating that that's the
     * case. Thankfully, there's a rather easy fix - make sure the keys and
     * values array are the same size. Shocking, I know.
     * </p>
     *
     * @param keys   all the keys that should be added to the map.
     * @param values all the values that should be added to the map.
     */
    public FrozenMap(Arrayable<K> keys, Arrayable<V> values) {
        this.keys = new StaticArray<>(keys);
        this.values = new StaticArray<>(values);
    }

    /**
     * Create a new {@code FrozenMap} instance.
     *
     * <p>
     * After an instance of this map is created, the values stored in this
     * map cannot be updated whatsoever. The map is entirely frozen, or static,
     * or constant, or whatever you want to call it.
     * </p>
     *
     * <p>
     * If the size of the {@code keys} and {@code values} arrays are different,
     * this constructor will throw an exception indicating that that's the
     * case. Thankfully, there's a rather easy fix - make sure the keys and
     * values array are the same size. Shocking, I know.
     * </p>
     *
     * @param keys   all the keys that should be added to the map.
     * @param values all the values that should be added to the map.
     */
    public FrozenMap(K[] keys, V[] values) {
        this.keys = new StaticArray<>(keys);
        this.values = new StaticArray<>(values);
    }

    /**
     * Create a new {@code FrozenMap} instance.
     *
     * <p>
     * After an instance of this map is created, the values stored in this
     * map cannot be updated whatsoever. The map is entirely frozen, or static,
     * or constant, or whatever you want to call it.
     * </p>
     *
     * <p>
     * This constructor is rather expensive, especially when compared to the
     * other constructors offered by the frozen map class. Just as a heads up.
     * </p>
     *
     * @param keysAndValues an array of keys and values. Each of these keys and
     *                      values will be added to the frozen map's internal
     *                      key and value registry.
     * @see FrozenMap#FrozenMap(Object[], Object[])
     * @see FrozenMap#FrozenMap(Arrayable, Arrayable)
     */
    @SuppressWarnings("unchecked")
    public FrozenMap(Arrayable<KeyValue<K, V>> keysAndValues) {
        DynamicArray<K> keys = new DynamicArray<>(keysAndValues.size());
        DynamicArray<V> values = new DynamicArray<>(keysAndValues.size());
        KeyValue<K, V>[] asArray = (KeyValue<K, V>[]) keysAndValues.toArray();

        for (KeyValue<K, V> kv : asArray) {
            keys.add(kv.getKey());
            values.add(kv.getValue());
        }

        this.keys = new StaticArray<>(keys);
        this.values = new StaticArray<>(values);
    }

    /**
     * Create a new {@code FrozenMap} instance.
     *
     * <p>
     * After an instance of this map is created, the values stored in this
     * map cannot be updated whatsoever. The map is entirely frozen, or static,
     * or constant, or whatever you want to call it.
     * </p>
     *
     * <p>
     * This constructor is rather expensive, especially when compared to the
     * other constructors offered by the frozen map class. Just as a heads up.
     * </p>
     *
     * @param keysAndValues an array of keys and values. Each of these keys and
     *                      values will be added to the frozen map's internal
     *                      key and value registry.
     * @see FrozenMap#FrozenMap(Object[], Object[])
     * @see FrozenMap#FrozenMap(Arrayable, Arrayable)
     */
    public FrozenMap(KeyValue<K, V>[] keysAndValues) {
        DynamicArray<K> keys = new DynamicArray<>(keysAndValues.length);
        DynamicArray<V> values = new DynamicArray<>(keysAndValues.length);

        for (KeyValue<K, V> kv : keysAndValues) {
            keys.add(kv.getKey());
            values.add(kv.getValue());
        }

        this.keys = new StaticArray<>(keys);
        this.values = new StaticArray<>(values);
    }

    /**
     * Create a new frozen map instance by using another map as a base. This,
     * in effect, "freezes" the newly created map. The newly created frozen
     * map is an immortalized and frozen clone of the input map.
     *
     * @param map the map to use as a base. The contents of this map are
     *            copied over to the new map - no mutability issues here.
     */
    public FrozenMap(Mappable<K, V> map) {
        this(map.getKeys(), map.getValues());
    }

    /**
     * Put a given pair into the map. If the map already contains an element
     * with the same key as the key that was inputted, replace that key's
     * value. If the map doesn't contain the given key and the map can be
     * expanded to include it, insert the value into the map.
     *
     * @param key   the key that the value should go under.
     * @param value the value the key should have.
     * @see Mappable#put(KeyValue)
     */
    @Override
    public void put(K key, V value) {
        throw new UnsupportedOperationException(
                "Cannot update the values of a frozen map after its " +
                        "construction! Try a static or dynamic map."
        );
    }

    /**
     * Put a given pair into the map. If the map already contains an element
     * with the same key as the key that was inputted, replace that key's
     * value. If the map doesn't contain the given key and the map can be
     * expanded to include it, insert the value into the map.
     *
     * @param keyValuePair a pair of a key and a value that should be inserted
     *                     into the map. This pair contains both a key and a
     *                     value.
     * @see Mappable#put(Object, Object)
     */
    @Override
    public void put(KeyValue<K, V> keyValuePair) {
        throw new UnsupportedOperationException(
                "Cannot update the values of a frozen map after its " +
                        "construction! Try a static or dynamic map."
        );
    }

    /**
     * Get the value that a specified key in the map contains.
     *
     * @param key the key to query for.
     * @return if the map contains the requested key, return the key's value.
     * If the map doesn't contain the requested key, however, return null,
     * indicating that the requested key could not be found.
     */
    @Override
    public V get(K key) {
        final int index;

        if ((index = keys.indexOf(key)) >= 0) return values.get(index);
        return null;
    }

    /**
     * Get the key associated with a specified value.
     *
     * @param value the value to search for.
     * @return the first instance of a key that matches with the given value.
     * If there are no instances of the value in the map, return null. If there
     * are multiple instances of the requested key in the map, only return the
     * key of the first of those indexes.
     */
    @Override
    public K getKey(V value) {
        final int index;

        if ((index = values.indexOf(value)) >= 0) return keys.get(index);
        return null;
    }

    /**
     * Check to see if the map contains a specified key.
     *
     * @param key the key to look for.
     * @return true if the map contains the requested key. Return false if the
     * map doesn't contain the requested key.
     */
    @Override
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    /**
     * Check to see if the map contains a specified value.
     *
     * @param value the value to look for.
     * @return true if the map contains at least one instance of the specified
     * value, and false if it doesn't.
     */
    @Override
    public boolean containsValue(V value) {
        return values.contains(value);
    }

    /**
     * Get an {@code Arrayable} element based on all of the keys contained in
     * the map's data set.
     *
     * @return an {@code Arrayable} element composed of all of the map's keys.
     */
    @Override
    public StaticArray<K> getKeys() {
        return keys;
    }

    /**
     * Get an {@code Arrayable} element based on all of the values contained in
     * the map's data set.
     *
     * @return an {@code Arrayable} element composed of all of the map's values.
     */
    @Override
    public StaticArray<V> getValues() {
        return values;
    }

    /**
     * Get the size of the map.
     *
     * <p>
     * This attribute is based on the quantity of entries contained in the map.
     * Lone keys and lone values (if those exist) are not counted towards the
     * total size of the map. In the case of map implementations that use
     * dynamic arrays, size should still represent the total ACTIVE size, not
     * the total allocated size.
     * </p>
     *
     * @return the map's size.
     */
    @Override
    public int size() {
        return keys.size();
    }

    /**
     * Clear all of the values from a map.
     *
     * <p>
     * Clearing a map will <strong>not</strong> reset the entire map to a new
     * map. Rather, it will clear each of the values from the map. Each of the
     * key elements should remain entirely intact. The value data structure
     * should not be de-allocated - rather, it should remain allocated and
     * ready to receive new information.
     * </p>
     *
     * <p>
     * If you're interested in "starting fresh," so to speak, you can create a
     * new instance of the map you're using. Otherwise, this lovely clear
     * method should get the job done fairly well.
     * </p>
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException(
                "Cannot update the values of a frozen map after its " +
                        "construction! Try a static or dynamic map."
        );
    }

    /**
     * Get the map's iterator.
     *
     * @return the map's iterator. The specific implementation of the iterator
     * is left entirely up to the implementing class, but each iterator is an
     * implementation of the {@link ItrPair} interface, so they function near
     * identically in practice.
     */
    @Override
    public ItrPair<K, V> itr() {
        return _itr_internal_;
    }
}
