package io.github.dracosomething.awakened_lib.util;

import java.util.Arrays;

public final class Timer implements Runner {
    private Task[] tasks;
    private String name;
    public boolean canceled;

    public Timer(String name) {
        this.name = name;
        this.tasks = new Task[128];
        canceled = false;
        TimerHelper.add(this);
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
        for (int i = 0; i <= tasks.length-1; i++)
            tasks[i] = null;
    }

    public String getName() {
        return name;
    }

    public void runTasks() {
        if (!this.canceled) {
            if (this.getTasks().length <= 0) return;
            for (Task task : this.getTasks()) {
                if (task != null) {
                    task.timeElapsed++;
                    boolean canUpdate = (task.timeElapsed % task.duration == 0) && (task.timeElapsed >= task.delay);
                    if (canUpdate) {
                        task.run();
                        task.timeElapsed = 0;
                    }
                }
            }
        } else {
            TimerHelper.remove(this.getName());
            this.cancel();
        }
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
