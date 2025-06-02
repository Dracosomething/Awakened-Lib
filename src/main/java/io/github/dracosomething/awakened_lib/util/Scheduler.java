package io.github.dracosomething.awakened_lib.util;

/**
 * This class is an example of using the <a href="#{@link}">{@link Runner}</a> interface.
 * <p>
 * Use this class if you want to have a <a href="#{@link}">{@link Task}</a> run at specific ticks.
 *
 * @see Runner
 */
public final class Scheduler implements Runner {
    // fields
    private Task task;
    private String id;
    private int[] targetTicks;
    private int currentTick;
    private int tick;
    private boolean canceled;

    /**
     * The constructor of the class.
     * <p>
     * Automatically registers the new <code>Scheduler</code>.
     *
     * @param name          The name of the <code>Scheduler</code>
     * @param task          The <a href="#{@link}">{@link Task}</a> that should be scheduled
     * @param duration      How long the <a href="#{@link}">{@link Task}</a> should run
     * @param delay         The amount of time in between running the <a href="#{@link}">{@link Task}</a>
     * @param targetTicks   The ticks that the <a href="#{@link}">{@link Task}</a> should get run on
     */
    public Scheduler(String name, Task task, long duration, long delay, int... targetTicks) {
        this.id = name;
        this.task = task;
        this.targetTicks = targetTicks;
        this.currentTick = 0;
        this.tick = 0;
        this.canceled = false;
        TimerHelper.add(this);
        this.schedule(this.task, duration, delay);
    }

    /**
     * This method has to be filled when implementing this interface.
     * This method will get called when you want to add a task to run
     * in the timer.
     *
     * @param task      The task that will get scheduled on the <code>Scheduler</code>
     * @param duration  How long the <a href="#{@link}">{@link Task}</a> should run
     * @param delay     The amount of time in between running the <a href="#{@link}">{@link Task}</a>
     * @see Task
     */
    public void schedule(Task task, long duration, long delay) {
        task.delay = delay;
        task.duration = duration;
        task.state = Task.State.SCHEDULED;
        task.timeElapsed = 0;
        this.task = task;
    }

    /**
     * method used to cancel this <code>Scheduler</code>
     */
    public void cancel() {
        this.task = null;
        this.id = null;
        this.targetTicks = null;
        this.canceled = true;
    }

    /**
     * Use this method to get the name of the <code>Scheduler</code>
     * @return  the <code>id</code>> of the <code>Scheduler</code>.
     */
    public String getName() {
        return this.id;
    }

    /**
     * This method is used to make the <code>Scheduler</code> run it's <a href="#{@link}">{@link Task}</a>
     */
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
