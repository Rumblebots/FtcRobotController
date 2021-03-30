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
import java.util.Map;

/**
 * A re-implementation of the map concept, designed to leverage a pair of
 * linked {@link DynamicArray} instances to improve performance.
 *
 * <p>
 * It's very important to note that many of the performance improvements you
 * see from using a {@code DynamicMap} over something like a {@code HashMap}
 * come from smaller-sized maps. {@code HashMap} elements are very effective
 * at storing thousands upon thousands of entries. {@code DynamicMap} elements,
 * unlike {@code HashMap} elements, store entries in a pair of linked arrays
 * instead of a hash table.
 * </p>
 *
 * <p>
 * {@code DynamicMap} elements are faster than the similar {@code HashMap}
 * data structure. Well, in some situations. Specifically:
 * <ul>
 *     <li>
 *         Adding elements to the map. {@code DynamicMap} elements are nearly
 *         four times faster than comparable {@code HashMap} elements.
 *     </li>
 *     <li>
 *         Removing elements from the map. Like adding elements, removing
 *         elements with a {@code DynamicArray} is nearly four times faster
 *         than doing the same on a {@code HashMap}.
 *     </li>
 * </ul>
 * </p>
 *
 * <p>
 * One of the biggest improvements the {@code DynamicMap} class provides over
 * the {@code HashMap} class (or other similar maps) is the speed of iteration.
 * Especially over larger data sets. Although lookup times on the dynamic map
 * are rather high, in large part due to index calculations, iteration speeds
 * are typically half of that in a comparable hash map. In a simple test of
 * a map (size 100,000), dynamic maps scored an average of 14,213 NS compared
 * to hash map's 60,000 NS. Obviously, this is a huge improvement. These
 * drastic differences in execution time carry over to smaller maps as well.
 * If you're planning on iterating over your data very frequently, using a
 * dynamic map over another type of map might be a good idea.
 * </p>
 *
 * @param <K> the type used for key elements.
 * @param <V> the type used for value elements.
 * @author Colin Robertson
 */
public class DynamicMap<K, V> implements Mappable<K, V> {
    /**
     * An internal registry of key values.
     *
     * <p>
     * This array shouldn't be modified by users - the map should handle
     * almost all modification on the array.
     * </p>
     */
    private final DynamicArray<K> keys;

    /**
     * An internal registry of actual values.
     *
     * <p>
     * This array shouldn't be modified by users - the map should handle
     * almost all modification on the array.
     * </p>
     */
    private final DynamicArray<V> values;

    /**
     * The internally-stored iterator instance. This is prefixed and suffixed
     * by underscores so it would be very difficult for an end user to
     * accidentally attempt to reference this. They should instead use the
     * {@link DynamicMap#itr()} method/getter to get it.
     */
    private final MapIterator<K, V> _itr_internal_ =
            new MapIterator<>(() -> this);

    /**
     * Create a new {@code DynamicMap} without any elements. Because the map
     * has no elements, you'll need to add some later.
     *
     * @see DynamicMap#add(Object, Object)
     * @see DynamicMap#add(int, Object, Object)
     * @see DynamicMap#add(KeyValue)
     * @see DynamicMap#add(int, KeyValue)
     */
    public DynamicMap() {
        this(0);
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
    public DynamicMap(int minSize) {
        keys = new DynamicArray<>(minSize);
        values = new DynamicArray<>(minSize);
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
    public DynamicMap(KeyValue<K, V>[] values) {
        this.keys = new DynamicArray<>(values.length);
        this.values = new DynamicArray<>(values.length);

        for (KeyValue<K, V> value : values) {
            this.keys.add(value.getKey());
            this.values.add(value.getValue());
        }
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
    public DynamicMap(Map<K, V> inputMap) {
        this.keys = new DynamicArray<>(inputMap.size());
        this.values = new DynamicArray<>(inputMap.size());

        for (Map.Entry<K, V> entry : inputMap.entrySet()) {
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
    }

    /**
     * Create a new {@code DynamicMap} instance.
     *
     * <p>
     * It's generally suggested that you create a dynamic map and add elements
     * to it in array form (an array of key/value pairs) or by adding the
     * elements later. But this is here if you want it.
     * </p>
     *
     * @param k1 the first key of the {@code DynamicMap}.
     * @param v1 the first value of the {@code DynamicMap}.
     */
    @SuppressWarnings("unchecked")
    public DynamicMap(K k1, V v1) {
        this.keys = new DynamicArray<>();
        this.values = new DynamicArray<>();

        try {
            keys.add((K) new Object[]{k1});
            values.add((V) new Object[]{v1});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new {@code DynamicMap} instance.
     *
     * <p>
     * It's generally suggested that you create a dynamic map and add elements
     * to it in array form (an array of key/value pairs) or by adding the
     * elements later. But this is here if you want it.
     * </p>
     *
     * @param k1 the first key of the {@code DynamicMap}.
     * @param v1 the first value of the {@code DynamicMap}.
     * @param k2 the second key of the {@code DynamicMap}.
     * @param v2 the second value of the {@code DynamicMap}.
     */
    @SuppressWarnings("unchecked")
    public DynamicMap(K k1, V v1,
                      K k2, V v2) {
        this.keys = new DynamicArray<>();
        this.values = new DynamicArray<>();

        try {
            keys.add((K) new Object[]{k1, k2});
            values.add((V) new Object[]{v1, v2});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new {@code DynamicMap} instance.
     *
     * <p>
     * It's generally suggested that you create a dynamic map and add elements
     * to it in array form (an array of key/value pairs) or by adding the
     * elements later. But this is here if you want it.
     * </p>
     *
     * @param k1 the first key of the {@code DynamicMap}.
     * @param v1 the first value of the {@code DynamicMap}.
     * @param k2 the second key of the {@code DynamicMap}.
     * @param v2 the second value of the {@code DynamicMap}.
     * @param k3 the third key of the {@code DynamicMap}.
     * @param v3 the third value of the {@code DynamicMap}.
     */
    @SuppressWarnings("unchecked")
    public DynamicMap(K k1, V v1,
                      K k2, V v2,
                      K k3, V v3) {
        this.keys = new DynamicArray<>();
        this.values = new DynamicArray<>();

        try {
            keys.add((K) new Object[]{k1, k2, k3});
            values.add((V) new Object[]{v1, v2, v3});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new {@code DynamicMap} instance.
     *
     * <p>
     * It's generally suggested that you create a dynamic map and add elements
     * to it in array form (an array of key/value pairs) or by adding the
     * elements later. But this is here if you want it.
     * </p>
     *
     * @param k1 the first key of the {@code DynamicMap}.
     * @param v1 the first value of the {@code DynamicMap}.
     * @param k2 the second key of the {@code DynamicMap}.
     * @param v2 the second value of the {@code DynamicMap}.
     * @param k3 the third key of the {@code DynamicMap}.
     * @param v3 the third value of the {@code DynamicMap}.
     * @param k4 the fourth key of the {@code DynamicMap}.
     * @param v4 the fourth value of the {@code DynamicMap}.
     */
    @SuppressWarnings("unchecked")
    public DynamicMap(K k1, V v1,
                      K k2, V v2,
                      K k3, V v3,
                      K k4, V v4) {
        this.keys = new DynamicArray<>();
        this.values = new DynamicArray<>();

        try {
            keys.add((K) new Object[]{k1, k2, k3, k4});
            values.add((V) new Object[]{v1, v2, v3, v4});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new {@code DynamicMap} instance.
     *
     * <p>
     * It's generally suggested that you create a dynamic map and add elements
     * to it in array form (an array of key/value pairs) or by adding the
     * elements later. But this is here if you want it.
     * </p>
     *
     * @param k1 the first key of the {@code DynamicMap}.
     * @param v1 the first value of the {@code DynamicMap}.
     * @param k2 the second key of the {@code DynamicMap}.
     * @param v2 the second value of the {@code DynamicMap}.
     * @param k3 the third key of the {@code DynamicMap}.
     * @param v3 the third value of the {@code DynamicMap}.
     * @param k4 the fourth key of the {@code DynamicMap}.
     * @param v4 the fourth value of the {@code DynamicMap}.
     * @param k5 the fifth key of the {@code DynamicMap}.
     * @param v5 the fifth value of the {@code DynamicMap}.
     */
    @SuppressWarnings("unchecked")
    public DynamicMap(K k1, V v1,
                      K k2, V v2,
                      K k3, V v3,
                      K k4, V v4,
                      K k5, V v5) {
        this.keys = new DynamicArray<>();
        this.values = new DynamicArray<>();

        try {
            keys.add((K) new Object[]{k1, k2, k3, k4, k5});
            values.add((V) new Object[]{v1, v2, v3, v4, v5});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Put a given pair into the map. If the map already contains an element
     * with the same key as the key that was inputted, replace that key's
     * value. If the map doesn't contain the given key and the map can be
     * expanded to include it, insert the value into the map.
     *
     * <p>
     * This method operates by attempting to determine the index of the
     * requested key. If the index of that key is -1, the key isn't contained
     * in the map - we now respond by adding the input key/value pair to the
     * map's internal arrays. If the index of that requested key is anything
     * above -1 (non-inclusive) then the key already is included in the map's
     * set, and we can simply over-write that key's value.
     * </p>
     *
     * @param key   the key that the value should go under.
     * @param value the value the key should have.
     */
    @Override
    public void put(K key,
                    V value) {
        int index = indexOfKey(key);

        if (index >= 0) {
            /*
             * The element is already contained in the map.
             */
            values.set(index, value);
        } else {
            /*
             * The element is not contained in the map.
             */
            add(key, value);
        }
    }

    /**
     * Put a given pair into the map. If the map already contains an element
     * with the same key as the key that was inputted, replace that key's
     * value. If the map doesn't contain the given key and the map can be
     * expanded to include it, insert the value into the map.
     *
     * <p>
     * This method operates by attempting to determine the index of the
     * requested key. If the index of that key is -1, the key isn't contained
     * in the map - we now respond by adding the input key/value pair to the
     * map's internal arrays. If the index of that requested key is anything
     * above -1 (non-inclusive) then the key already is included in the map's
     * set, and we can simply over-write that key's value.
     * </p>
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
     * Add a key-value pair to the map. Adding doesn't mean setting - it really
     * does mean adding a key-value pair. If there is already the given key
     * contained in the array, another instance of that key will be added, in
     * addition to the value.
     *
     * <p>
     * If you want to set/put an element, you can use either the set or put
     * methods provided in the {@code DynamicMap} class.
     * </p>
     *
     * @param key   the value's key.
     * @param value the value's... well, the value's value.
     */
    public void add(K key,
                    V value) {
        keys.add(key);
        values.add(value);
    }

    /**
     * Add a key-value pair to the map. Adding doesn't mean setting - it really
     * does mean adding a key-value pair. If there is already the given key
     * contained in the array, another instance of that key will be added, in
     * addition to the value.
     *
     * <p>
     * If you want to set/put an element, you can use either the set or put
     * methods provided in the {@code DynamicMap} class.
     * </p>
     *
     * @param index the index to insert the value at. This index generally is
     *              not all that useful to end users.
     * @param key   the key to insert the value under.
     * @param value the value... Yeah. That's it. The value.
     */
    public void add(int index,
                    K key,
                    V value) {
        keys.add(index, key);
        values.add(index, value);
    }

    /**
     * Add a key-value pair to the map. Please note - adding doesn't mean
     * setting or putting. This method ignores the possibility that there's
     * already an instance of that key contained in the map.
     *
     * @param keyValuePair the key/value pair that should be added to the map.
     */
    public void add(KeyValue<K, V> keyValuePair) {
        add(keyValuePair.getKey(), keyValuePair.getValue());
    }

    /**
     * Add a key-value pair to the map. Please note - adding doesn't mean
     * setting or putting. This method ignores the possibility that there's
     * already an instance of that key contained in the map.
     *
     * @param index        the index to insert the value at. This index
     *                     generally is not all that useful to end users.
     * @param keyValuePair the key/value pair that should be added to the map.
     */
    public void add(int index, KeyValue<K, V> keyValuePair) {
        add(index, keyValuePair.getKey(), keyValuePair.getValue());
    }

    /**
     * Remove whatever entry is contained at the given index.
     *
     * @param index the index to remove an entry from. This index can be
     *              determined by using the index from methods. If you'd like
     *              to remove an element based on a key, you should check
     *              out the "see" tags in this doc.
     * @see DynamicMap#remove(Object)
     * @see DynamicMap#remove(Object, Object)
     * @see DynamicMap#remove(KeyValue)
     * @see DynamicMap#removeByValue(Object)
     */
    public void remove(int index) {
        keys.remove(index);
        values.remove(index);
    }

    /**
     * Remove an element from the map based on a given key.
     *
     * <p>
     * If there are multiple instances of the query, only the first instance
     * will be removed.
     * </p>
     *
     * <p>
     * If the query is not contained in the map, this method will do absolutely
     * nothing. It won't throw any warnings, but nothing will be removed.
     * </p>
     *
     * @param key the key to remove an element for.
     */
    public void remove(K key) {
        int index = indexOfKey(key);

        if (index >= 0) {
            remove(index);
        }
    }

    /**
     * Remove an element from the map based on a given value.
     *
     * <p>
     * If there are multiple instances of the query, only the first instance
     * will be removed.
     * </p>
     *
     * <p>
     * If the query is not contained in the map, this method will do absolutely
     * nothing. It won't throw any warnings, but nothing will be removed.
     * </p>
     *
     * @param value the value of the element to remove.
     */
    public void removeByValue(V value) {
        int index = indexOfValue(value);

        if (index >= 0) {
            remove(index);
        }
    }

    /**
     * Remove an element from the map based on a key and a value. If the key
     * and the value aren't in the same place in the map, this method will
     * do nothing and not remove anything.
     *
     * @param key   the key of the to-be-removed entry.
     * @param value the value of the to-be-removed entry.
     */
    public void remove(K key, V value) {
        int indexKey = indexOfKey(key);
        int indexValue = indexOfValue(value);

        if (indexKey == indexValue) remove(indexKey);
    }

    /**
     * Remove an element from the map based on a key and a value. If the key
     * and the value aren't in the same place in the map, this method will
     * do nothing and not remove anything.
     *
     * @param keyValuePair the key and value pair that should be removed.
     */
    public void remove(KeyValue<K, V> keyValuePair) {
        remove(keyValuePair.getKey(), keyValuePair.getValue());
    }

    /**
     * Get a specified value based on a key.
     *
     * <p>
     * If there is no entry with the specified parameter (key or value), this
     * method will return null instead of throwing an exception related to
     * the array index being out of bounds.
     * </p>
     *
     * @param key the key to search for.
     * @return the value that corresponds to the inputted key. If it isn't
     * contained in the array, return null instead.
     */
    public V get(K key) {
        int index = keys.indexOf(key);
        return index >= 0 ? values.get(index) : null;
    }

    /**
     * Get a specified key based on a value.
     *
     * <p>
     * If there is no entry with the specified parameter (key or value), this
     * method will return null instead of throwing an exception related to
     * the array index being out of bounds.
     * </p>
     *
     * @param value the value to search for.
     * @return the key that corresponds to the inputted value. If it isn't
     * contained in the array, return null instead.
     */
    public K getKey(V value) {
        int index = values.indexOf(value);
        return index >= 0 ? keys.get(index) : null;
    }

    /**
     * Get the index of a requested key.
     *
     * <p>
     * Using indexes to access values is generally discouraged. Rather, it's
     * suggested you make use of the get by key and get by value methods.
     * <ul>
     *     <li>
     *         {@link DynamicMap#get(Object)}
     *     </li>
     *     <li>
     *         {@link DynamicMap#getKey(Object)}
     *     </li>
     * </ul>
     * </p>
     *
     * @param key the key to get the index of.
     * @return the index of that key. If it can't be located, this method
     * will return -1. Trying to get an element at index -1 will result in
     * an {@link ArrayIndexOutOfBoundsException} being thrown.
     */
    public int indexOfKey(K key) {
        return keys.indexOf(key);
    }

    /**
     * Get the index of a requested value.
     *
     * <p>
     * Using indexes to access values is generally discouraged. Rather, it's
     * suggested you make use of the get by key and get by value methods.
     * <ul>
     *     <li>
     *         {@link DynamicMap#get(Object)}
     *     </li>
     *     <li>
     *         {@link DynamicMap#getKey(Object)}
     *     </li>
     * </ul>
     * </p>
     *
     * @param value the value to get the index of.
     * @return the index of that value. If it can't be located, this method
     * will return -1. Trying to get an element at index -1 will result in
     * an {@link ArrayIndexOutOfBoundsException} being thrown.
     */
    public int indexOfValue(V value) {
        return values.indexOf(value);
    }

    /**
     * Check to see if the {@code DynamicMap} contains a specified query.
     *
     * @param key the key that should be checked.
     * @return whether or not the dynamic map's internal key array contains
     * the requested key.
     */
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    /**
     * Check to see if the {@code DynamicMap} contains a specified query.
     *
     * @param value the value that should be checked.
     * @return whether or not the dynamic map's internal value array contains
     * the requested value.
     */
    public boolean containsValue(V value) {
        return values.contains(value);
    }

    /**
     * Get a {@link DynamicArray} of key elements stored internally.
     *
     * <p>
     * PLEASE NOTE! You shouldn't ever modify the value of this array directly.
     * You should almost always use the methods provided in the dynamic map
     * class to modify these values. You can look at these values, or use
     * any of the methods from the {@code DynamicArray} class that you'd like
     * to, but it is very strongly suggested that you don't modify them.
     * </p>
     *
     * <p>
     * Modifying these values may de-couple the linked arrays that compose
     * this type of map, thus defeating the entire purpose of the map.
     * </p>
     *
     * @return a {@code DynamicArray} of elements.
     */
    public DynamicArray<K> getKeys() {
        return keys;
    }

    /**
     * Get a {@link DynamicArray} of value elements stored internally.
     *
     * <p>
     * PLEASE NOTE! You shouldn't ever modify the value of this array directly.
     * You should almost always use the methods provided in the dynamic map
     * class to modify these values. You can look at these values, or use
     * any of the methods from the {@code DynamicArray} class that you'd like
     * to, but it is very strongly suggested that you don't modify them.
     * </p>
     *
     * <p>
     * Modifying these values may de-couple the linked arrays that compose
     * this type of map, thus defeating the entire purpose of the map.
     * </p>
     *
     * @return a {@code DynamicArray} of elements.
     */
    public DynamicArray<V> getValues() {
        return values;
    }

    /**
     * Get an array of keys.
     *
     * <p>
     * This array can be casted to whatever you'd like if you'd like to do
     * something more advanced than looking at a set of objects.
     * </p>
     *
     * @return an array of keys.
     */
    public Object[] getKeyArray() {
        return keys.toArray();
    }

    /**
     * Get an array of values.
     *
     * <p>
     * This array can be casted to whatever you'd like if you'd like to do
     * something more advanced than looking at a set of objects.
     * </p>
     *
     * @return an array of values.
     */
    public Object[] getValueArray() {
        return values.toArray();
    }

    /**
     * Get an {@link ArrayList} of the keys.
     *
     * @return an {@link ArrayList} of the keys.
     */
    public ArrayList<Object> getKeyArrayList() {
        return keys.toArrayList();
    }

    /**
     * Get an {@link ArrayList} of the values.
     *
     * @return an {@link ArrayList} of the values.
     */
    public ArrayList<Object> getValueArrayList() {
        return values.toArrayList();
    }

    /**
     * Get the size of the map.
     *
     * <p>
     * Although it probably doesn't matter from an API perspective, it's worth
     * noting that this method determines the size of the map by getting the
     * size of the internal keys array. Unless external modification happens
     * to these arrays (without the help of methods provided in this class)
     * this size value should always be accurate.
     * </p>
     *
     * @return the map's size.
     */
    public int size() {
        return keys.size();
    }

    /**
     * Get the "real" size of the map.
     *
     * <p>
     * This "real" size represents the combined allocated length of both
     * of the internally-used arrays. Generally, you should use size - NOT
     * real size, as this might not be the size you're expecting.
     * </p>
     *
     * @return the "real" size of the keys array plus the "real" size of
     * the values array.
     */
    public int realSize() {
        return keys.realSize() + values.realSize();
    }

    /**
     * Trim the active portion of the dynamic map. This method acts to free
     * up memory. It's suggested that you call this method after adding a
     * great number of elements to the map and then removing them. The larger
     * the map is, the more memory needs to be allocated to the map. Trimming
     * the map will force the re-allocation of the internal arrays, thus
     * reducing the map's memory footprint.
     *
     * <p>
     * To cite the documentation provided in the {@link DynamicArray} class:
     * </p>
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
     * @see DynamicArray#trim()
     */
    public void trim() {
        keys.trim();
        values.trim();
    }

    /**
     * Clear all of the values contained in the active portion of the map.
     *
     * <p>
     * It's important to note that this method WILL NOT re-size the map. The
     * active portion of the {@code DynamicMap} will still be whatever it was
     * before. This method will, however, set all of the elements in the active
     * portion of the map to be equal to null.
     * </p>
     *
     * @see DynamicMap#reset()
     */
    public void clear() {
        keys.clear();
        values.clear();
    }

    /**
     * Reset the entire {@code DynamicMap}. Resetting the map clears all of
     * the values in the map, resets the map's size to the defined minimum
     * size, and trims the internal array.
     *
     * <p>
     * Resetting a map is the optimal way to totally wipe the map without
     * having to create another map. If you'd just like to wipe the map's
     * values, you can use the clear() method. If you'd like to resize the map
     * to make it as small as possible, you can use the trim() method. If you'd
     * like to get a fresh start, this method right here is your guy.
     * </p>
     */
    public void reset() {
        keys.reset();
        values.reset();
    }

    /**
     * Get the {@code DynamicMap}'s iteration system. For more information
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
