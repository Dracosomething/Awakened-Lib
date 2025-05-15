package io.github.dracosomething.awakened_lib.util;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.library.Task;
import io.github.dracosomething.awakened_lib.library.Runner;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;

@EventBusSubscriber(modid = Awakened_lib.MODID)
public class TimerHelper {
    private static HashMap<String, Runner> RUNNERS = new HashMap<>();

    public static boolean contains(Runner runner) {
        return contains(runner.getName(), runner);
    }

    public static boolean contains(String name) {
        Runner runner = RUNNERS.get(name);
        return contains(name, runner) && runner != null;
    }

    public static boolean contains(String name, Runner runner) {
        return RUNNERS.containsKey(name) && RUNNERS.containsValue(runner);
    }

    public static boolean add(Runner runner) {
        RUNNERS.put(runner.getName(), runner);
        return contains(runner);
    }

    public static boolean remove(String name) {
        if (!RUNNERS.containsKey(name)) return false;
        RUNNERS.remove(name);
        return !contains(name);
    }

    @SubscribeEvent
    public static void RunTimersPre(ServerTickEvent.Pre event) {
        runTasks();
    }

    @SubscribeEvent
    public static void RunTimersPost(ServerTickEvent.Post event) {
        runTasks();
    }

    private static void runTasks() {
        if (RUNNERS.isEmpty()) return;
        for (Runner runner : RUNNERS.values()) {
            runner.runTasks();
        }
    }
}
