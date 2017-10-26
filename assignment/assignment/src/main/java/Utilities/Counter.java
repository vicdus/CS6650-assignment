package utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Counter {
    private static int count = 0;

    public static synchronized int increase() {
        count++;
        return count;
    }
}
