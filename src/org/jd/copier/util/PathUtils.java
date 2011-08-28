/*
 * A simple Copy Mechanism to Hadoop
 * Author: Jagaran Das
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.jd.copier.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathUtils {

/**
 * Splits a list into smaller sublists.
 * The original list remains unmodified and changes on the sublists are not propagated to the original list.
 *
 *
 * @param original
 *            The list to split
 * @param maxListSize
 *            The max amount of element a sublist can hold.
 * @param listImplementation
 *            The implementation of List to be used to create the returned sublists
 * @return A list of sublists
 * @throws IllegalArgumentException
 *             if the argument maxListSize is zero or a negative number
 * @throws NullPointerException
 *             if arguments original or listImplementation are null
 */
public static final <T> List<List<T>> split(final List<T> original, final int maxListSize,
        final Class<? extends List> listImplementation) {
    if (maxListSize <= 0) {
        throw new IllegalArgumentException("maxListSize must be greater than zero");
    }

    final T[] elements = (T[]) original.toArray();
    final int maxChunks = (int) Math.ceil(elements.length / (double) maxListSize);

    final List<List<T>> lists = new ArrayList<List<T>>(maxChunks);
    for (int i = 0; i < maxChunks; i++) {
        final int from = i * maxListSize;
        final int to = Math.min(from + maxListSize, elements.length);
        final T[] range = Arrays.copyOfRange(elements, from, to);

        lists.add(createSublist(range, listImplementation));
    }

    return lists;
}

/**
 * Splits a list into smaller sublists. The sublists are of type ArrayList.
 * The original list remains unmodified and changes on the sublists are not propagated to the original list.
 *
 *
 * @param original
 *            The list to split
 * @param maxListSize
 *            The max amount of element a sublist can hold.
 * @return A list of sublists
 */
public static final <Path> List<List<Path>> split(final List<Path> original, final int maxListSize) {
    return split(original, maxListSize, ArrayList.class);
}

private static <Path> List<Path> createSublist(final Path[] elements, final Class<? extends List> listImplementation) {
    List<Path> sublist;
    final List<Path> asList = Arrays.asList(elements);
    try {
        sublist = listImplementation.newInstance();
        sublist.addAll(asList);
    } catch (final InstantiationException e) {
        sublist = asList;
    } catch (final IllegalAccessException e) {
        sublist = asList;
    }

    return sublist;
}
}