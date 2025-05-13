package io.github.dracosomething.awakened_lib.util;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.library.Task;
import io.github.dracosomething.awakened_lib.library.Timer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = Awakened_lib.MODID)
public class TimerHelper {
    private static HashMap<String, Timer> timers = new HashMap<>();

    public static boolean contains(Timer timer) {
        return contains(timer.getName(), timer);
    }

    public static boolean contains(String name) {
        Timer timer = timers.get(name);
        return contains(name, timer) && timer != null;
    }

    public static boolean contains(String name, Timer timer) {
        return timers.containsKey(name) && timers.containsValue(timer);
    }

    public static boolean add(Timer timer) {
        timers.put(timer.getName(), timer);
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
            for (Timer timer : timers.values()) {
                if (!timer.canceled) {
                    if (timer.getTasks().length <= 0) continue;
                    for (Task task : timer.getTasks()) {
                        if (task != null) {
                            boolean canUpdate = (task.timeElapsed % task.duration == 0) && (task.timeElapsed >= task.delay);
                            if (canUpdate) {
                                task.run();
                                task.timeElapsed = 0;
                            }
                        }
                    }
                } else {
                    remove(timer.getName());
                    timer.clear();
                }
            }
        }
    }
