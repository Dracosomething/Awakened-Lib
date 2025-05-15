package io.github.dracosomething.awakened_lib.registry.registries;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.registry.object.objectRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = Awakened_lib.MODID, bus = EventBusSubscriber.Bus.MOD)
public class registryRegistry {
    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event) {
        event.register(objectRegistry.OBJECTS_REGISTRY);
    }
}
