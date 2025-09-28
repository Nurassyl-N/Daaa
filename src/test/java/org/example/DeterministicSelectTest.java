package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class DeterministicSelectTest {
    @Test
    void testSelectMatchesSorted() {
        Random rnd = new Random();
        for (int t = 0; t < 50; t++) {
            int[] a = rnd.ints(200, 0, 1000).toArray();
            int[] copy = a.clone();
            Arrays.sort(copy);

            int k = rnd.nextInt(a.length);

            Metrics m = new Metrics();
            DepthTracker d = new DepthTracker();
            int kth = DeterministicSelect.select(a.clone(), k, m, d);

            assertEquals(copy[k], kth);
        }
    }

    @Test
    void testSelectSmallArray() {
        int[] a = {10, 3, 7, 2, 15};
        int[] sorted = a.clone();
        Arrays.sort(sorted);

        Metrics m = new Metrics();
        DepthTracker d = new DepthTracker();

        assertEquals(sorted[0], DeterministicSelect.select(a.clone(), 0, m, d)); // минимум
        assertEquals(sorted[2], DeterministicSelect.select(a.clone(), 2, m, d)); // медиана
        assertEquals(sorted[4], DeterministicSelect.select(a.clone(), 4, m, d)); // максимум
    }
}