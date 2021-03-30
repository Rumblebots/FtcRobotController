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
 * Interface shared between all map implementations, such as the (sexy)
 * {@code DynamicMap} and the (equally sexy) {@code StaticMap}.
 *
 * @param <K> the type of key element in the map.
 * @param <V> the type of value element in the map.
 */
public interface Mappable<K, V> {
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
    void put(K key, V value);

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
    void put(KeyValue<K, V> keyValuePair);

    /**
     * Get the value that a specified key in the map contains.
     *
     * @param key the key to query for.
     * @return if the map contains the requested key, return the key's value.
     * If the map doesn't contain the requested key, however, return null,
     * indicating that the requested key could not be found.
     */
    V get(K key);

    /**
     * Get the key associated with a specified value.
     *
     * @param value the value to search for.
     * @return the first instance of a key that matches with the given value.
     * If there are no instances of the value in the map, return null. If there
     * are multiple instances of the requested key in the map, only return the
     * key of the first of those indexes.
     */
    K getKey(V value);

    /**
     * Check to see if the map contains a specified key.
     *
     * @param key the key to look for.
     * @return true if the map contains the requested key. Return false if the
     * map doesn't contain the requested key.
     */
    boolean containsKey(K key);

    /**
     * Check to see if the map contains a specified value.
     *
     * @param value the value to look for.
     * @return true if the map contains at least one instance of the specified
     * value, and false if it doesn't.
     */
    boolean containsValue(V value);

    /**
     * Get an {@code Arrayable} element based on all of the keys contained in
     * the map's data set.
     *
     * @return an {@code Arrayable} element composed of all of the map's keys.
     */
    Arrayable<K> getKeys();

    /**
     * Get an {@code Arrayable} element based on all of the values contained in
     * the map's data set.
     *
     * @return an {@code Arrayable} element composed of all of the map's values.
     */
    Arrayable<V> getValues();

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
    int size();

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
    void clear();

    /**
     * Get the map's iterator.
     *
     * @return the map's iterator. The specific implementation of the iterator
     * is left entirely up to the implementing class, but each iterator is an
     * implementation of the {@link ItrPair} interface, so they function near
     * identically in practice.
     */
    ItrPair<K, V> itr();
}
