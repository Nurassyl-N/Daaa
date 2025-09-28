package org.example;

import java.util.concurrent.ThreadLocalRandom;

public class QuickSort {
    private static final int CUTOFF = 16;

    public static void sort(int[] a, Metrics m, DepthTracker d) {
        try (DepthTracker.DepthFrame frame = d.enter()) {
            quickSort(a, 0, a.length - 1, m, d);
        }
    }

    private static void quickSort(int[] a, int lo, int hi, Metrics m, DepthTracker d) {
        while (lo < hi) {
            int n = hi - lo + 1;
            if (n <= CUTOFF) { insertionSort(a, lo, hi, m); return; }
            int p = randomizedPartition(a, lo, hi, m);
            int left = p - lo, right = hi - p;
            if (left < right) {
                try (DepthTracker.DepthFrame f = d.enter()) { quickSort(a, lo, p - 1, m, d); }
                lo = p + 1;
            } else {
                try (DepthTracker.DepthFrame f = d.enter()) { quickSort(a, p + 1, hi, m, d); }
                hi = p - 1;
            }
        }
    }

    private static int randomizedPartition(int[] a, int lo, int hi, Metrics m) {
        int r = ThreadLocalRandom.current().nextInt(lo, hi + 1);
        swap(a, r, hi, m);
        return partition(a, lo, hi, m);
    }

    private static int partition(int[] a, int lo, int hi, Metrics m) {
        int pivot = a[hi];
        int i = lo;
        for (int j = lo; j < hi; j++) {
            m.incCmp();
            if (a[j] <= pivot) swap(a, i++, j, m);
        }
        swap(a, i, hi, m);
        return i;
    }

    private static void swap(int[] a, int i, int j, Metrics m) {
        if (i == j) return;
        int t = a[i]; a[i] = a[j]; a[j] = t; m.incSwapOrCopy();
    }

    private static void insertionSort(int[] a, int lo, int hi, Metrics m) {
        for (int i = lo + 1; i <= hi; i++) {
            int key = a[i], j = i - 1;
            while (j >= lo) {
                m.incCmp();
                if (a[j] > key) { a[j + 1] = a[j]; m.incSwapOrCopy(); j--; }
                else break;
            }
            a[j + 1] = key; m.incSwapOrCopy();
        }
    }
}
