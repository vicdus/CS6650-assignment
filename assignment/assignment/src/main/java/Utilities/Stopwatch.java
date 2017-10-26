package utilities;

import com.google.common.base.Preconditions;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor(staticName = "createStopwatch")
public final class Stopwatch {
    private Long t = null;

    public void start() {
        this.t = System.currentTimeMillis();
    }

    public long readAndReset() {
        long res = this.read();
        t = System.currentTimeMillis();
        return res;
    }

    public long read() {
        Preconditions.checkArgument(t != null, "use without initialization");
        return System.currentTimeMillis() - t;
    }

    public long getStartTime() {
        Preconditions.checkArgument(t != null, "use without initialization");
        return t;
    }
}
