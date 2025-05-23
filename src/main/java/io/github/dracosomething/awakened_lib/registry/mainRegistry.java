package io.github.dracosomething.awakened_lib.registry;

import io.github.dracosomething.awakened_lib.manaSystem.systems.ManaSystem;
import io.github.dracosomething.awakened_lib.manaSystem.systems.RegenOn;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import io.github.dracosomething.awakened_lib.registry.dataComponents.dataComponentsRegistry;
import io.github.dracosomething.awakened_lib.registry.items.itemRegistry;
import io.github.dracosomething.awakened_lib.registry.object.objectRegistry;
import io.github.dracosomething.awakened_lib.registry.payload.ClientPayloadRegistry;
import net.neoforged.bus.api.IEventBus;

public class mainRegistry {
    public static void register(IEventBus bus) {
        new ManaSystem("test", 100, 1, RegenOn.PLAYER, 20);
        objectRegistry.register(bus);
        DataAttachmentRegistry.register(bus);
        dataComponentsRegistry.register(bus);
        itemRegistry.register(bus);
        ClientPayloadRegistry.register();
    }
}
