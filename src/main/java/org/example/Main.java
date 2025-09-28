package org.example;

import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nВыберите алгоритм:");
            System.out.println("1 - MergeSort");
            System.out.println("2 - QuickSort");
            System.out.println("3 - Select (Median-of-Medians)");
            System.out.println("4 - Closest Pair of Points");
            System.out.println("0 / q - Выход");
            System.out.print("Ваш выбор: ");

            String input = sc.next().trim();
            if (input.equals("0") || input.equalsIgnoreCase("q") || input.equalsIgnoreCase("exit")) {
                System.out.println("Завершение программы...");
                break;
            }
            if (!input.matches("\\d+")) {
                System.out.println("Ошибка: нужно ввести число (1–4) или 0/q для выхода.");
                continue;
            }
            int choice = Integer.parseInt(input);
            if (choice < 1 || choice > 4) {
                System.out.println("Ошибка: число должно быть от 1 до 4.");
                continue;
            }

            System.out.print("Введите размер n: ");
            int n = sc.hasNextInt() ? sc.nextInt() : 1000;
            sc.nextLine(); // очистка буфера

            Metrics m = new Metrics();
            DepthTracker d = new DepthTracker();
            long t0, t1;

            try (CsvLogger log = new CsvLogger(Path.of("metrics.csv"))) {
                switch (choice) {
                    case 1 -> {
                        int[] a = randomArray(n);
                        t0 = System.nanoTime();
                        MergeSort.sort(a, m, d);
                        t1 = System.nanoTime();
                        log.log("mergesort", n, t1 - t0, d.maxDepth(),
                                m.getComparisons(), m.getSwapsOrCopies(), m.getAllocations());
                        System.out.println("MergeSort завершён.");
                    }
                    case 2 -> {
                        int[] a = randomArray(n);
                        t0 = System.nanoTime();
                        QuickSort.sort(a, m, d);
                        t1 = System.nanoTime();
                        log.log("quicksort", n, t1 - t0, d.maxDepth(),
                                m.getComparisons(), m.getSwapsOrCopies(), m.getAllocations());
                        System.out.println("QuickSort завершён.");
                    }
                    case 3 -> {
                        int[] a = randomArray(n);
                        int k = n / 2;
                        t0 = System.nanoTime();
                        int kth = DeterministicSelect.select(a.clone(), k, m, d);
                        t1 = System.nanoTime();
                        Arrays.sort(a);
                        if (kth != a[k]) throw new AssertionError("Select неверный результат!");
                        log.log("select_mom5", n, t1 - t0, d.maxDepth(),
                                m.getComparisons(), m.getSwapsOrCopies(), m.getAllocations());
                        System.out.println("Select завершён. k=" + k + " → " + kth);
                    }
                    case 4 -> {
                        ClosestPairOfPoints.Point[] pts = new ClosestPairOfPoints.Point[n];
                        Random rnd = new Random(1);
                        for (int i = 0; i < n; i++) {
                            pts[i] = new ClosestPairOfPoints.Point(rnd.nextDouble(), rnd.nextDouble());
                        }
                        t0 = System.nanoTime();
                        double ans = ClosestPairOfPoints.solve(pts, m, d);
                        t1 = System.nanoTime();
                        log.log("closest_pair", n, t1 - t0, d.maxDepth(),
                                m.getComparisons(), m.getSwapsOrCopies(), m.getAllocations());
                        System.out.println("Closest Pair завершён. Расстояние = " + ans);
                    }
                }
            }
        }
    }

    private static int[] randomArray(int n) {
        Random rnd = new Random(42);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt(100_000);
        return a;
    }
}