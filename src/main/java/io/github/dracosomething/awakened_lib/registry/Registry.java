package io.github.dracosomething.awakened_lib.registry;

import net.minecraftforge.eventbus.api.IEventBus;

public class Registry {
    public static void register(IEventBus modEventBus) {
        EffectRegistry.init(modEventBus);
    }
}
