package util;

public class Stopwatch {
    private long start;

    public Stopwatch() {
        start = java.lang.System.nanoTime();
    }

    public void marker(String marker) {
        long duration = java.lang.System.nanoTime() - start;
        System.out.println(marker +": " + (float)duration / 1000000000 + " seconds");
    }
}
