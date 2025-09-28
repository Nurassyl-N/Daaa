package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class MergeSortTest {
    @Test
    void testRandomArray() {
        int[] a = new Random().ints(1000, 0, 10000).toArray();
        int[] copy = a.clone();

        Metrics m = new Metrics();
        DepthTracker d = new DepthTracker();
        MergeSort.sort(a, m, d);

        Arrays.sort(copy);
        assertArrayEquals(copy, a);
    }

    @Test
    void testAlreadySorted() {
        int[] a = {1, 2, 3, 4, 5};
        int[] copy = a.clone();

        Metrics m = new Metrics();
        DepthTracker d = new DepthTracker();
        MergeSort.sort(a, m, d);

        assertArrayEquals(copy, a);
    }

    @Test
    void testReversedArray() {
        int[] a = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};

        Metrics m = new Metrics();
        DepthTracker d = new DepthTracker();
        MergeSort.sort(a, m, d);

        assertArrayEquals(expected, a);
    }
}