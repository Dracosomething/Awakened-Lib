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
    public static void RunTimers(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            runTasks();
        }
        if (event.phase == TickEvent.Phase.END) {
            runTasks();
        }
    }

        private static void runTasks() {
            if (RUNNERS.isEmpty()) return;
            for (Runner runner : RUNNERS.values()) {
                runner.runTasks();
            }
        }
    }
