package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class CsvLogger implements Closeable {
    private final BufferedWriter out;

    public CsvLogger(Path path) throws IOException {
        boolean newFile = !Files.exists(path);
        out = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        if (newFile) {
            out.write("algo,n,nanos,ms,depth,comparisons,copies,allocations\n");
        }
    }

    public synchronized void log(String algo, int n, long nanos, int depth,
                                 long cmp, long copies, long alloc) throws IOException {
        long ms = nanos / 1_000_000L;
        out.write(String.join(",",
                algo,
                Integer.toString(n),
                Long.toString(nanos),
                Long.toString(ms),
                Integer.toString(depth),
                Long.toString(cmp),
                Long.toString(copies),
                Long.toString(alloc)));
        out.write("\n");
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
