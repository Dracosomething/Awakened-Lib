package io.github.dracosomething.awakened_lib.events;

import io.github.dracosomething.awakened_lib.manaSystem.data.api.ManaManager;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystem;
import net.neoforged.bus.api.Event;

import java.util.HashMap;
import java.util.Map;

public class ManaSystemEvent extends Event {
    public static class ManaSystemGatherEvent extends ManaSystemEvent {
        private Map<String, ManaSystem> systems;

        public ManaSystemGatherEvent() {
            this.systems = new HashMap<>();
        }

        public <T extends ManaSystem> void addSystem(String id, T system) {
            this.systems.put(id, system);
        }

        public Map<String, ManaSystem> getSystems() {
            return systems;
        }
    }

    public static class ManaSystemSetupEvent extends ManaSystemEvent {
        private final ManaManager manager;

        public  ManaSystemSetupEvent(ManaManager manager) {
            this.manager = manager;
        }

        public ManaManager getManager() {
            return manager;
        }

        public <T extends ManaSystem> void addSystem(String id, T system) {
            this.manager.registerSystem(id, system);
        }
    }
}
