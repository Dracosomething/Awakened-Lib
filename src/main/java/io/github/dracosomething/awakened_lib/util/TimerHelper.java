package io.github.dracosomething.awakened_lib.util;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.library.Task;
import io.github.dracosomething.awakened_lib.library.Runner;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = Awakened_lib.MODID)
public class TimerHelper {
    private static HashMap<String, Runner> timers = new HashMap<>();

    public static boolean contains(Runner timer) {
        return contains(Runner.getName(), timer);
    }

    public static boolean contains(String name) {
        Runner timer = timers.get(name);
        return contains(name, timer) && timer != null;
    }

    public static boolean contains(String name, Runner timer) {
        return timers.containsKey(name) && timers.containsValue(timer);
    }

    public static boolean add(Runner timer) {
        timers.put(Runner.getName(), timer);
        return contains(timer);
    }

    public static boolean remove(String name) {
        timers.remove(name);
        return !contains(name);
    }

    @SubscribeEvent
    public static void RunTimers(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            runTasks();
        }
        if (event.phase == TickEvent.Phase.END) {
            runTasks();
        }
    }

        private static void runTasks() {
            if (timers.isEmpty()) return;
            for (Runner runner : timers.values()) {
                runner.runTasks();
            }
        }
    }
