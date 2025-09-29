During the execution of this task the classical algorithms «divide and rule» were implemented and analyzed: MergeSort, QuickSort, Deterministic Select (Median-of-Medians) and Closest Pair of Points.
For each algorithm, a separate module was written and an infrastructure was created to measure the performance of the program. I
In particular, I added the Metrics class to count comparisons, copies and allocs, DepthTracker to control recursion depth and CsvLogger to save experiment results into metrics.csv file.
The sorting algorithms provide a transition to InsertionSort for small array size (cutoff) in order to avoid unnecessary overhead.
QuickSort uses the strategy of «recursive call only on smaller parts», which guarantees to limit the depth of stack to O(log n).

The theoretical analysis of recurrences confirmed the expected results.
For MergeSort the recurrency is T(n) = 2T(n/2) + Θ(n), and on the second branch of the theorem of the Wizard results in Θ(n log n).
For QuickSort, on average, pivot randomization gives balanced breakdowns, and time is also estimated as Θ(n log n), while the worst case remains Θ(n 2).
For Select with 5-element medians, the recursion T(n) = T(n/5) + T(7n/10) + Θ(n) is determined by the Acra-Bazzi method and gives a linear time of Θ(n).
For the problem of the nearest pair of points, the recursion is similar to MergeSort: T(n) = 2T(n/2) + Θ(n), which also leads to Θ(n log n).

Experiments confirmed theoretical estimates. Constructed time-of-entry graphs (time vs n) and depth of recursion (depth vs n) showed the expected trends.
In MergeSort, the recursion depth is strictly log n, in QuickSort it is slightly higher, but remains within O(log n) due to the choice of a lower half for the recursion.
In the case of Select, depth grows much slower, which is consistent with linear asymptotics.
In addition, the effect of constant factors is noticeable on small sizes of arrays: InsertionSort works faster than full-fledged recursive algorithms, and with large n the cache and work of the garbage collector begin to be affected.

As a result, we can say that theory and practice are generally the same.
All implemented algorithms have shown their asymptotic complexity in practice, and the discrepancies in constant multipliers are explained by the peculiarities of implementation and work of JVM.
QuickSort was faster than MergeSort on random data, but inferior on «evil» inputs.
The nearest pair search algorithm confirmed asymptotics Θ(n log n), but in small samples a naive algorithm with quadratic complexity sometimes won because of lower overhead.
Altogether, the work done has given a good understanding of how theoretical recursive evaluations are manifested in real measurements and how important engineering details (cutoff, optimization of the depth of recursion, caching) are for final performance.