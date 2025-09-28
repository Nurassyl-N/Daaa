package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class QuickSortTest {
    @Test
    void testRandomArray() {
        int[] a = new Random().ints(1000, 0, 10000).toArray();
        int[] copy = a.clone();

        Metrics m = new Metrics();
        DepthTracker d = new DepthTracker();
        QuickSort.sort(a, m, d);

        Arrays.sort(copy);
        assertArrayEquals(copy, a);

        int maxDepth = d.maxDepth();
        int bound = 2 * (int)(Math.log(a.length) / Math.log(2)) + 5;
        assertTrue(maxDepth <= bound);
    }

    @Test
    void testAllEqualElements() {
        int[] a = new int[50];
        Arrays.fill(a, 7);
        int[] copy = a.clone();

        Metrics m = new Metrics();
        DepthTracker d = new DepthTracker();
        QuickSort.sort(a, m, d);

        assertArrayEquals(copy, a);
    }
}