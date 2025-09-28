package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class ClosestPairTest {
    @Test
    void testClosestPairSmallN() {
        Random rnd = new Random();
        int n = 200;
        ClosestPairOfPoints.Point[] pts = new ClosestPairOfPoints.Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new ClosestPairOfPoints.Point(rnd.nextDouble(), rnd.nextDouble());
        }

        Metrics m = new Metrics();
        DepthTracker d = new DepthTracker();
        double fast = ClosestPairOfPoints.solve(pts, m, d);

        double slow = bruteForce(pts);

        assertEquals(slow, fast, 1e-9);
    }

    @Test
    void testTwoPoints() {
        ClosestPairOfPoints.Point[] pts = {
                new ClosestPairOfPoints.Point(0, 0),
                new ClosestPairOfPoints.Point(3, 4)
        };

        Metrics m = new Metrics();
        DepthTracker d = new DepthTracker();
        double dmin = ClosestPairOfPoints.solve(pts, m, d);

        assertEquals(5.0, dmin, 1e-9);
    }

    private double bruteForce(ClosestPairOfPoints.Point[] pts) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double dx = pts[i].x() - pts[j].x();
                double dy = pts[i].y() - pts[j].y();
                best = Math.min(best, Math.hypot(dx, dy));
            }
        }
        return best;
    }
}