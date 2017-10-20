package Utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UninterruptibleCyclicBarrier {
    public static void await(CyclicBarrier cb) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    cb.await();
                    return;
                } catch (InterruptedException | BrokenBarrierException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
