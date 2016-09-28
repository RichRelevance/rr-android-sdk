package com.richrelevance.internal;

public class BusyLock {

    public interface Evaluator {
        public boolean isUnlocked();
    }

    public static boolean wait(long interval, long timeout, Evaluator evaluator) {
        return new BusyLock(evaluator).waitUntilUnlocked(interval, timeout);
    }

    private Evaluator evaluator;

    public BusyLock(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public boolean waitUntilUnlocked(long interval, long timeout) {
        long startTime = System.currentTimeMillis();
        boolean unlocked = evaluator.isUnlocked();

        while (!unlocked && (System.currentTimeMillis() - startTime < timeout)) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                // Don't care
            }

            unlocked = evaluator.isUnlocked();
        }

        return unlocked;
    }
}
