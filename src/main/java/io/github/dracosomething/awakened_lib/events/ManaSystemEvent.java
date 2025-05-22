package io.github.dracosomething.awakened_lib.events;

import io.github.dracosomething.awakened_lib.manaSystem.Systems.ManaSystem;
import net.neoforged.bus.api.Event;

import java.util.List;
import java.util.Map;

public class ManaSystemEvent extends Event {
    public static class ManaSystemGatherEvent extends ManaSystemEvent {
        private Map<String, ManaSystem> systems;

        public <T extends ManaSystem> void addSystem(String id, T system) {
            this.systems.put(id, system);
        }

        public Map<String, ManaSystem> getSystems() {
            return systems;
        }
    }

    public static class ManaSystemSetupEvent extends ManaSystemEvent {
        private final Map<String, ManaSystem> systems;

        public  ManaSystemSetupEvent(Map<String, ManaSystem> systems) {
            this.systems = systems;
        }

        public Map<String, ? extends ManaSystem> getSystems() {
            return systems;
        }

        public <T extends ManaSystem> void addSystem(String id, T system) {
            this.systems.put(id, system);
        }
    }
}
