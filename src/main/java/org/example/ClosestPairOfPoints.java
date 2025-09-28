package org.example;

import java.util.*;

public class ClosestPairOfPoints {
    public static record Point(double x, double y) {}

    public static double solve(Point[] pts, Metrics m, DepthTracker d) {
        if (pts == null || pts.length < 2) return Double.POSITIVE_INFINITY;
        Point[] px = pts.clone();
        Point[] py = pts.clone();
        Arrays.sort(px, Comparator.comparingDouble(Point::x));
        Arrays.sort(py, Comparator.comparingDouble(Point::y));
        m.addAlloc(2L * pts.length);
        try (DepthTracker.DepthFrame frame = d.enter()) {
            return rec(px, py, 0, pts.length, m, d);
        }
    }

    private static double rec(Point[] px, Point[] py, int lo, int hi, Metrics m, DepthTracker d) {
        int n = hi - lo;
        if (n <= 3) {
            double best = Double.POSITIVE_INFINITY;
            for (int i = lo; i < hi; i++)
                for (int j = i + 1; j < hi; j++)
                    best = Math.min(best, dist(px[i], px[j]));
            Arrays.sort(px, lo, hi, Comparator.comparingDouble(Point::y));
            return best;
        }
        int mid = lo + n / 2;
        double midX = px[mid].x();

        ArrayList<Point> pyl = new ArrayList<>(n);
        ArrayList<Point> pyr = new ArrayList<>(n);
        for (Point p : py) {
            if (p.x() < midX || (p.x() == midX && pyl.size() < mid - lo)) pyl.add(p); else pyr.add(p);
        }

        double dl, dr;
        try (DepthTracker.DepthFrame f1 = d.enter()) {
            dl = rec(px, pyl.toArray(new Point[0]), lo, mid, m, d);
        }
        try (DepthTracker.DepthFrame f2 = d.enter()) {
            dr = rec(px, pyr.toArray(new Point[0]), mid, hi, m, d);
        }
        double delta = Math.min(dl, dr);

        ArrayList<Point> strip = new ArrayList<>();
        for (Point p : py) if (Math.abs(p.x() - midX) < delta) strip.add(p);

        double best = delta;
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && j <= i + 7; j++) {
                double val = dist(strip.get(i), strip.get(j));
                if (val < best) best = val;
            }
        }
        return best;
    }

    private static double dist(Point a, Point b) {
        double dx = a.x() - b.x(), dy = a.y() - b.y();
        return Math.hypot(dx, dy);
    }
}
