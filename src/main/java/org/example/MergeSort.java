package org.example;

public class MergeSort {
    private static final int CUTOFF = 32;

    public static void sort(int[] a, Metrics m, DepthTracker d) {
        int[] buf = new int[a.length];
        m.addAlloc(a.length);
        try (DepthTracker.DepthFrame frame = d.enter()) {
            sort(a, 0, a.length, buf, m, d);
        }
    }

    private static void sort(int[] a, int lo, int hi, int[] buf, Metrics m, DepthTracker d) {
        int n = hi - lo;
        if (n <= 1) return;
        if (n <= CUTOFF) { insertionSort(a, lo, hi, m); return; }
        int mid = lo + n / 2;
        try (DepthTracker.DepthFrame f1 = d.enter()) { sort(a, lo,  mid, buf, m, d); }
        try (DepthTracker.DepthFrame f2 = d.enter()) { sort(a, mid, hi, buf, m, d); }
        merge(a, lo, mid, hi, buf, m);
    }

    private static void merge(int[] a, int lo, int mid, int hi, int[] buf, Metrics m) {
        int i = lo, j = mid, k = 0;
        while (i < mid && j < hi) {
            m.incCmp();
            if (a[i] <= a[j]) { buf[k++] = a[i++]; m.incSwapOrCopy(); }
            else { buf[k++] = a[j++]; m.incSwapOrCopy(); }
        }
        while (i < mid) { buf[k++] = a[i++]; m.incSwapOrCopy(); }
        while (j < hi)  { buf[k++] = a[j++]; m.incSwapOrCopy(); }
        System.arraycopy(buf, 0, a, lo, k);
        m.addSwapOrCopy(k);
    }

    private static void insertionSort(int[] a, int lo, int hi, Metrics m) {
        for (int i = lo + 1; i < hi; i++) {
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