package io.github.dracosomething.awakened_lib.registry;

import io.github.dracosomething.awakened_lib.registry.object.objectRegistry;
import net.minecraftforge.eventbus.api.IEventBus;

public class mainRegistry {
    public static void register(IEventBus bus) {
        objectRegistry.register(bus);
    }
}
