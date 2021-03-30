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
 * An implementation of the {@code Mappable} interface designed for storing
 * a set of final keys and non-final values. In practice, this means that the
 * keys of the static map cannot ever be updated after the map's construction,
 * but the values can always be changed.
 *
 * @param <K> the type of object that the keys of the map use.
 * @param <V> the type of object that the values of the map use.
 * @author Colin Robertson
 */
public class StaticMap<K, V> implements Mappable<K, V> {
    /**
     * A static array of all the keys contained in the map.
     */
    private final StaticArray<K> keys;

    /**
     * A static array of all the values contained in the map. These values can
     * be updated whenever and wherever, but the size of the array cannot
     * ever increase or decrease, aside from initializing a new map.
     */
    private StaticArray<V> values;

    /**
     * The map's internal iteration system. This field itself shouldn't be
     * accessed directly by the user.
     *
     * @see StaticMap#itr()
     */
    private final MapIterator<K, V> _itr_internal_ =
            new MapIterator<>(() -> this);

    /**
     * Create a new {@code StaticMap} instance.
     *
     * @param keys the set of keys to use for the instance. Please note that
     *             keys cannot be updated after the map has been created. The
     *             values behind these keys can always be updated, but the
     *             keys themselves cannot.
     */
    public StaticMap(K[] keys) {
        this.keys = new StaticArray<>(keys);
        this.values = new StaticArray<>(keys.length);
    }

    /**
     * Create a new {@code StaticMap} instance.
     *
     * @param keys the set of keys to use for the instance. Please note that
     *             keys cannot be updated after the map has been created. The
     *             values behind these keys can always be updated, but the
     *             keys themselves cannot.
     */
    public StaticMap(Arrayable<K> keys) {
        this.keys = new StaticArray<>(keys);
        this.values = new StaticArray<>(keys.size());
    }

    /**
     * Create a new {@code StaticMap} instance. When creating a new static
     * map with this constructor, the size of the key and value arrays must be
     * exactly the same, or an exception will be thrown.
     *
     * @param keys   the set of keys to use for the instance. Please note that
     *               keys cannot be updated after the map has been created. The
     *               values behind these keys can always be updated, but the
     *               keys themselves cannot.
     * @param values the set of values that the map should use. These values
     *               can always be updated, unlike the keys.
     */
    @SuppressWarnings("unchecked")
    public StaticMap(Arrayable<K> keys,
                     Arrayable<V> values) {
        this((K[]) keys.toArray(), (V[]) values.toArray());
    }

    /**
     * Create a new {@code StaticMap} instance. When creating a new static
     * map with this constructor, the size of the key and value arrays must be
     * exactly the same, or an exception will be thrown.
     *
     * @param keys   the set of keys to use for the instance. Please note that
     *               keys cannot be updated after the map has been created. The
     *               values behind these keys can always be updated, but the
     *               keys themselves cannot.
     * @param values the set of values that the map should use. These values
     *               can always be updated, unlike the keys.
     */
    public StaticMap(K[] keys, V[] values) {
        if (keys.length != values.length) {
            throw new ArrayIndexOutOfBoundsException(
                    "The length of the keys and values arrays must be " +
                            "exactly the same when creating a new static map " +
                            "instance!"
            );
        }

        this.keys = new StaticArray<>(keys);
        this.values = new StaticArray<>(values);
    }

    /**
     * Create a new {@code StaticMap} by using a regular map as a base.
     *
     * @param map the map that should be used as a base. The contents of this
     *            map will be copied over into the new map.
     */
    public StaticMap(Mappable<K, V> map) {
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
        int index = keys.indexOf(key);

        if (index >= 0) {
            values.set(index, value);
        } else {
            throw new ArrayIndexOutOfBoundsException("The requested key {"
                    + key + "}" + " could not be found in the StaticMap " +
                    "you're attempting to write to."
            );
        }
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
        put(keyValuePair.getKey(), keyValuePair.getValue());
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
        values = new StaticArray<>(keys.size());
    }

    /**
     * Get the {@code StaticMap}'s iteration system. For more information
     * regarding how the map's iterator works, check out the JavaDocs linked
     * in the "see" tags here.
     *
     * @return the {@code DynamicMap}'s internal iterator.
     * @see ItrPair
     */
    public ItrPair<K, V> itr() {
        return _itr_internal_;
    }
}
