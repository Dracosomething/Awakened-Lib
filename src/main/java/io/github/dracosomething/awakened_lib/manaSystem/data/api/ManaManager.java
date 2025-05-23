package io.github.dracosomething.awakened_lib.manaSystem.data.api;

import io.github.dracosomething.awakened_lib.events.ManaSystemEvent.*;
import io.github.dracosomething.awakened_lib.handler.StartUpHandler;
import io.github.dracosomething.awakened_lib.manaSystem.systems.IManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.systems.XPSystem;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class ManaManager {
    private final HashMap<String, IManaSystem> SYSTEMS = new HashMap<>();

    public void start(ManaSystemSetupEvent event) {
        SYSTEMS.putAll(event.getManager().SYSTEMS);
    }
    
    public IManaSystem get(String id) {
        IManaSystem system = this.SYSTEMS.get(id);
        if (system == null) return StartUpHandler.DEFAULT;
        return system;
    }

    public void registerSystem(String id, IManaSystem system) {
        this.SYSTEMS.put(id, system);
    }

    public void foreach(BiConsumer<String, IManaSystem> consumer) {
        SYSTEMS.forEach(consumer);
    }
}
