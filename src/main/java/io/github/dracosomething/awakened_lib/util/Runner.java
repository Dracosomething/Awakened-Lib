package io.github.dracosomething.awakened_lib.util;

/**
 * Implement this interface for <code>Timer</code> esk classes,
 * use this interface if you want to create something akin to a
 * timer in your mod.
 * <p>
 * @see java.util.Timer
 */
public interface Runner {
    default void schedule(Task task, long duration) {
        this.schedule(task, duration, 0);
    }

    default void schedule(long delay, Task task) {
        this.schedule(task, 1, delay);
    }

    default void schedule(Task task) {
        this.schedule(task, 1, 0);
    }

    /**
     * This method has to be filled when implementing this interface.
     * This method will get called when you want to add a <a href="#{@link}">{@link Task}</a> to run
     * in the <code>Runner</code>.
     *
     * @param task      The <a href="#{@link}">{@link Task}</a> that will get scheduled on the <code>Runner</code>
     * @param duration  How long the <a href="#{@link}">{@link Task}</a> should run
     * @param delay     The amount of time in between running the <a href="#{@link}">{@link Task}</a>
     * @see Task
     */
    void schedule(Task task, long duration, long delay);

    /**
     * method used to cancel a <code>Runner</code>
     */
    void cancel();

    /**
     * Use this method to get the name of the <code>Runner</code>
     * @return  The name of the <code>Runner</code>
     */
    String getName();

    /**
     * This method is used to make the <code>Runner</code> run it's tasks
     */
    void runTasks();
}
