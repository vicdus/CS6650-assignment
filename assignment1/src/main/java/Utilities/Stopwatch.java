package Utilities;

public final class Stopwatch {
    private Long t = null;

    public void start() {
        this.t = System.currentTimeMillis();
    }

    public long readAndReset() {
        if (t == null) {
            throw new RuntimeException("use without initialization");
        }
        long res = System.currentTimeMillis() - t;
        t = System.currentTimeMillis();
        return res;
    }
}
