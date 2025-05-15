package io.github.dracosomething.awakened_lib.util;

public abstract class Task implements Runnable {
    public long duration;
    public long delay;
    public long timeElapsed;
    public State state = State.VIRGIN;

    public abstract void run();

    public boolean cancel() {
        boolean result = (state == State.SCHEDULED);
        state = State.CANCELLED;
        return result;
    }

    public long scheduledExecutionTime() {
        return (duration < 0 ? delay + duration
                : delay - duration);
    }

    enum State {
        VIRGIN,
        SCHEDULED,
        CANCELLED;
    }
}
