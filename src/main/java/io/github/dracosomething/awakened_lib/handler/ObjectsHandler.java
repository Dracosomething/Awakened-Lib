package io.github.dracosomething.awakened_lib.handler;

import io.github.dracosomething.awakened_lib.dataAttachements.ObjectsCapability;
import io.github.dracosomething.awakened_lib.registry.object.objectRegistry;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber
public class ObjectsHandler {
    @SubscribeEvent
    public static void onJoin(LevelEvent.Load event) {
        if (event.getLevel().getServer() != null) {
            MinecraftServer server = event.getLevel().getServer();
            server.getAllLevels().forEach((level) -> {
                level.getCapability(ObjectsCapability.CAPABILITY).ifPresent((cap) -> {
                    cap.deserializeNBT(level.registryAccess(), cap.serializeNBT(level.registryAccess()));
                });
            });
        }
    }

    @SubscribeEvent
    public static void onSave(LevelEvent.Save event) {
        if (event.getLevel().getServer() != null) {
            MinecraftServer server = event.getLevel().getServer();
            server.getAllLevels().forEach((level) -> {
                ObjectsCapability.getFrom(level).ifPresent((data) -> {
                    data.deserializeNBT(level.registryAccess(), data.serializeNBT(level.registryAccess()));
                });
            });
        }
    }

    @SubscribeEvent
    public static void onUnload(LevelEvent.Unload event) {
        if (event.getLevel().getServer() != null) {
            MinecraftServer server = event.getLevel().getServer();
            server.getAllLevels().forEach((level) -> {
                ObjectsCapability.getFrom(level).ifPresent((data) -> {
                    data.deserializeNBT(level.registryAccess(), data.serializeNBT(level.registryAccess()));
                });
            });
        }
    }

    @SubscribeEvent
    public static void create(PlayerInteractEvent.LeftClickEmpty event) {
        objectRegistry.EXAMPLE.get().spawn(100, event.getLevel(), event.getPos());
        System.out.println("Succesfully created new object");
    }
}
