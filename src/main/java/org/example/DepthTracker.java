package org.example;

public class DepthTracker {
    private int current = 0;
    private int max = 0;

    public DepthFrame enter() {
        current++;
        if (current > max) max = current;
        return new DepthFrame(this);
    }

    void exit() { current--; }

    public int maxDepth() { return max; }

    public static final class DepthFrame implements AutoCloseable {
        private final DepthTracker tracker;
        private boolean closed = false;

        DepthFrame(DepthTracker t) { this.tracker = t; }

        @Override
        public void close() {
            if (!closed) {
                tracker.exit();
                closed = true;
            }
        }
    }
}
