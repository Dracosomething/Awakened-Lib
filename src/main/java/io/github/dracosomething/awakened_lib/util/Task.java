package io.github.dracosomething.awakened_lib.util;

/**
 * Use this class to run specific code.
 * Based on the <a href="#{@link}">{@link java.util.TimerTask}</a> class.
 *
 * @see Runnable
 */
public abstract class Task implements Runnable {
    // fields
    public long duration;
    public long delay;
    public long timeElapsed;
    public State state = State.VIRGIN;

    /**
     * this method is what contains the code
     */
    public abstract void run();

    /**
     * cancels this <code>Task</code>
     * @return  true if the <code>Task</code> got canceled
     */
    public boolean cancel() {
        boolean result = (state == State.SCHEDULED);
        state = State.CANCELLED;
        return result;
    }

    /**
     * Gets the next time the <code>Task</code> will get called,
     * used in <a href="#{@link}">{@link Runner}</a> interface
     * and its inheritors.
     * <p>
     * @return the amount of time until this <code>Task</code> will get called next.
     */
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
