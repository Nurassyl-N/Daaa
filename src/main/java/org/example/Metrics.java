package org.example;

public class Metrics {
    private long comparisons;
    private long swapsOrCopies;
    private long allocations;

    public void incCmp() { comparisons++; }
    public void addCmp(long c) { comparisons += c; }

    public void incSwapOrCopy() { swapsOrCopies++; }
    public void addSwapOrCopy(long c) { swapsOrCopies += c; }

    public void incAlloc() { allocations++; }
    public void addAlloc(long a) { allocations += a; }

    public long getComparisons() { return comparisons; }
    public long getSwapsOrCopies() { return swapsOrCopies; }
    public long getAllocations() { return allocations; }

    public void reset() {
        comparisons = swapsOrCopies = allocations = 0;
    }
}
