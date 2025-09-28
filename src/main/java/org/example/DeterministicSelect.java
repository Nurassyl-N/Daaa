package org.example;

public class DeterministicSelect {

    public static int select(int[] a, int k, Metrics m, DepthTracker d) {
        if (a == null) throw new IllegalArgumentException("array is null");
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("k out of range");
        try (DepthTracker.DepthFrame frame = d.enter()) {
            return select(a, 0, a.length - 1, k, m, d);
        }
    }

    private static int select(int[] a, int lo, int hi, int k, Metrics m, DepthTracker d) {
        while (true) {
            if (lo == hi) return a[lo];
            int pivotIndex;
            try (DepthTracker.DepthFrame frame = d.enter()) {
                pivotIndex = pivotByMediansOf5(a, lo, hi, m, d);
            }
            pivotIndex = partition(a, lo, hi, pivotIndex, m);
            if (k == pivotIndex) return a[k];

            if (k < pivotIndex) { hi = pivotIndex - 1; }
            else { lo = pivotIndex + 1; }
        }
    }

    private static int partition(int[] a, int lo, int hi, int p, Metrics m) {
        int pivot = a[p];
        swap(a, p, hi, m);
        int store = lo;
        for (int i = lo; i < hi; i++) {
            m.incCmp();
            if (a[i] < pivot) swap(a, store++, i, m);
        }
        swap(a, store, hi, m);
        return store;
    }

    private static int pivotByMediansOf5(int[] a, int lo, int hi, Metrics m, DepthTracker d) {
        int n = hi - lo + 1;
        if (n <= 5) { insertionSort(a, lo, hi, m); return lo + n / 2; }

        int numGroups = 0;
        for (int i = lo; i <= hi; i += 5) {
            int r = Math.min(i + 4, hi);
            insertionSort(a, i, r, m);
            int median = i + (r - i) / 2;
            swap(a, lo + numGroups, median, m);
            numGroups++;
        }
        int midRank = lo + numGroups / 2;
        return selectIndex(a, lo, lo + numGroups - 1, midRank, m, d);
    }

    private static int selectIndex(int[] a, int lo, int hi, int kIndex, Metrics m, DepthTracker d) {
        if (lo == hi) return lo;
        int p = pivotByMediansOf5(a, lo, hi, m, d);
        p = partition(a, lo, hi, p, m);
        if (kIndex == p) return p;
        else if (kIndex < p) return selectIndex(a, lo, p - 1, kIndex, m, d);
        else return selectIndex(a, p + 1, hi, kIndex, m, d);
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

    private static void swap(int[] a, int i, int j, Metrics m) {
        if (i == j) return;
        int t = a[i]; a[i] = a[j]; a[j] = t; m.incSwapOrCopy();
    }
}