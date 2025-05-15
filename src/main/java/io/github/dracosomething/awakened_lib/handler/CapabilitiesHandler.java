package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.dataAttachements.ChunkObjectsProvider;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkEvent;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = Awakened_lib.MODID)
public class CapabilitiesHandler {
    @SubscribeEvent
    public static void registerLevelCaps(ChunkEvent.Load event) {
        event.getChunk().getData(DataAttachmentRegistry.OBJECTS);
    }
}
