package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.Awakened_lib;
import io.github.dracosomething.awakened_lib.registry.dataAttachment.DataAttachmentRegistry;
import io.github.dracosomething.awakened_lib.registry.object.objectRegistry;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = Awakened_lib.MODID)
public class ObjectsHandler {
    @SubscribeEvent
    public static void create(PlayerInteractEvent.LeftClickEmpty event) {
        objectRegistry.EXAMPLE.get().spawn(100, event.getLevel(), event.getPos());
        System.out.println("Succesfully created new object");
    }
}
