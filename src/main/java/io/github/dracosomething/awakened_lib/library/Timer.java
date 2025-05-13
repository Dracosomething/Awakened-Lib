package io.github.dracosomething.awakened_lib.library;

import io.github.dracosomething.awakened_lib.util.TimerHelper;

import java.util.Arrays;

public final class Timer {
    private Task[] tasks;
    private String name;
    public boolean canceled;

    public Timer(String name) {
        this.name = name;
        this.tasks = new Task[128];
        canceled = false;
        TimerHelper.add(this);
    }

    public void schedule(Task task, long duration) {
        this.schedule(task, duration, 0);
    }

    public void schedule(long delay, Task task) {
        this.schedule(task, 1, delay);
    }

    public void schedule(Task task) {
        this.schedule(task, 1, 0);
    }

    public void schedule(Task task, long duration, long delay) {
        int size = size();
        task.duration = duration;
        task.delay = delay;
        task.state = Task.State.SCHEDULED;
        task.timeElapsed = 0;

        if (size + 1 >= tasks.length)
            tasks = Arrays.copyOf(tasks, tasks.length * 2);
        tasks[++size] = task;
    }

    public void cancel() {
        clear();
        canceled = true;
        TimerHelper.remove(this.name);
    }

    public void clear() {
        for (int i=1; i<=tasks.length; i++)
            tasks[i] = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Task[] getTasks() {
        return tasks;
    }

    private int size() {
        return this.tasks.length;
    }
}
