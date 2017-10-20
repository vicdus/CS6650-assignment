package Utilities;

public class Counter {
    private static int count = 0;
    public static synchronized int increase() {
        count++;
        return count;
    }
}
