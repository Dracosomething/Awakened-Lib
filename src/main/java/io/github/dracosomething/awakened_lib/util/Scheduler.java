package io.github.dracosomething.awakened_lib.util;

import io.github.dracosomething.awakened_lib.util.Task;
import io.github.dracosomething.awakened_lib.util.TimerHelper;

public final class Scheduler implements Runner {
    private Task task;
    private String id;
    private int[] targetTicks;
    private int currentTick;
    private int tick;
    private boolean canceled;

    public Scheduler(String name, Task task, int... targetTicks) {
        this.id = name;
        this.task = task;
        this.targetTicks = targetTicks;
        this.currentTick = 0;
        this.tick = 0;
        this.canceled = false;
    }

    public void schedule(Task task, long duration, long delay) {
        task.delay = delay;
        task.duration = duration;
        task.state = Task.State.SCHEDULED;
        task.timeElapsed = 0;
        this.task = task;
    }

    public void cancel() {
        this.task = null;
        this.id = null;
        this.targetTicks = null;
        this.canceled = true;
    }

    public String getName() {
        return this.id;
    }

    public void runTasks() {
        if (!this.canceled) {
            if (this.task != null) {
                this.task.timeElapsed++;
                boolean canUpdate = (this.task.timeElapsed % this.task.duration == 0) && (this.task.timeElapsed >= this.task.delay);
                if (canUpdate) {
                    this.currentTick++;
                    if (this.currentTick == this.targetTicks[tick]) {
                        this.task.run();
                        this.tick++;
                    }
                    this.task.timeElapsed = 0;
                }
            }

        } else {
            TimerHelper.remove(this.getName());
            this.cancel();
        }
    }
}
