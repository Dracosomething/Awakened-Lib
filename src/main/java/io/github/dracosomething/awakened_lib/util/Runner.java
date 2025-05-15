package io.github.dracosomething.awakened_lib.util;

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

    void schedule(Task task, long duration, long delay);

    void cancel();

    String getName();

    void runTasks();
}
