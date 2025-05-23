package io.github.dracosomething.awakened_lib.manaSystem.data.api;

import io.github.dracosomething.awakened_lib.events.ManaSystemEvent.*;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystem;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class ManaManager {
    private final HashMap<String, ManaSystem> SYSTEMS = new HashMap<>();

    public void start(ManaSystemSetupEvent event) {
        SYSTEMS.putAll(event.getManager().SYSTEMS);
    }
    
    public ManaSystem get(String id) {
        return this.SYSTEMS.get(id);
    }

    public void registerSystem(String id, ManaSystem system) {
        this.SYSTEMS.put(id, system);
    }

    public void foreach(BiConsumer<String, ManaSystem> consumer) {
        SYSTEMS.forEach(consumer);
    }
}
